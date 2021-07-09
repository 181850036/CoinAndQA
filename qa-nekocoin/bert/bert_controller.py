from flask import Flask

app = Flask(__name__)

from BERT_CRF import BertCrf
from NER_main import NerProcessor, CRF_LABELS
from SIM_main import SimProcessor,SimInputFeatures
from transformers import BertTokenizer, BertConfig, BertForSequenceClassification
from torch.utils.data import DataLoader, RandomSampler, SequentialSampler, TensorDataset
import torch
import pymysql
from tqdm import tqdm, trange
from py2neo import Graph
from py2neo import Node
from py2neo.matching import *
import json


device = torch.device("cpu")
character_other_name = {"雇主": ["雇主", "老板"],
              "父亲": ["父亲", "爸爸", "爸", "老爹", "老爸", "爹", "老豆"],
              "母亲": ["母亲", "妈妈", "妈", "老妈", "老娘", "娘"],
              "传记": ["传记", "生平", "经历"],
              "死因": ["死因", "死法", "咋死的", "怎么死的", "去世原因", "死亡方式", "死亡原因", "辞世原因", "离世原因", "怎样死的",
                     "为何死去", "怎样死去", "如何死去", "如何死的", "死去原因"],
              "荐主": ["荐主", "举荐", "荐引", "推荐"],
              "生日": ["什么时候出生", "生日"],
              "男性配偶代词": ["配偶", "妻子", "老婆"],
              "女性配偶代词": ["配偶", "丈夫", "老公"]
              }
event_other_name = {
              "影响": ["影响", "意义"],
              "时间": ["时间", "哪年"],
              "参与人物": ["参与人物", "人物"]
}
place_other_name = {
              "现在位置": ["现在位置", "当前位置", "目前位置" ,"现在", "当前", "目前", "现在在哪", "当前在哪", "目前在哪"]
}

def get_ner_model(config_file,pre_train_model,label_num = 2):
    model = BertCrf(config_name=config_file,num_tags=label_num, batch_first=True)
    model.load_state_dict(torch.load(pre_train_model, map_location='cpu'), strict=False)
    return model.to(device)


def get_sim_model(config_file,pre_train_model,label_num = 2):
    bert_config = BertConfig.from_pretrained(config_file)
    bert_config.num_labels = label_num
    model = BertForSequenceClassification(bert_config)
    model.load_state_dict(torch.load(pre_train_model, map_location='cpu'), strict=False)
    return model


def get_entity(model,tokenizer,sentence,max_len = 64):
    pad_token = 0
    sentence_list = list(sentence.strip().replace(' ',''))
    text = " ".join(sentence_list)
    inputs = tokenizer.encode_plus(
        text,
        add_special_tokens=True,
        max_length=max_len,
        truncate_first_sequence=True  # We're truncating the first sequence in priority if True
    )
    input_ids, token_type_ids = inputs["input_ids"], inputs["token_type_ids"]
    attention_mask = [1] * len(input_ids)
    padding_length = max_len - len(input_ids)
    input_ids = input_ids + ([pad_token] * padding_length)
    attention_mask = attention_mask + ([0] * padding_length)
    token_type_ids = token_type_ids + ([0] * padding_length)
    labels_ids = None

    assert len(input_ids) == max_len, "Error with input length {} vs {}".format(len(input_ids), max_len)
    assert len(attention_mask) == max_len, "Error with input length {} vs {}".format(len(attention_mask), max_len)
    assert len(token_type_ids) == max_len, "Error with input length {} vs {}".format(len(token_type_ids), max_len)

    input_ids = torch.tensor(input_ids).reshape(1,-1).to(device)
    attention_mask = torch.tensor(attention_mask).reshape(1,-1).to(device)
    token_type_ids = torch.tensor(token_type_ids).reshape(1,-1).to(device)
    labels_ids = labels_ids

    model = model.to(device)
    model.eval()
    # 由于传入的tag为None，所以返回的loss 也是None
    ret = model(input_ids = input_ids,
                  tags = labels_ids,
                  attention_mask = attention_mask,
                  token_type_ids = token_type_ids)
    pre_tag = ret[1][0]
    assert len(pre_tag) == len(sentence_list) or len(pre_tag) == max_len - 2

    pre_tag_len = len(pre_tag)
    b_loc_idx = CRF_LABELS.index('B-LOC')
    i_loc_idx = CRF_LABELS.index('I-LOC')
    o_idx = CRF_LABELS.index('O')

    if b_loc_idx not in pre_tag and i_loc_idx not in pre_tag:
        print("没有在句子[{}]中发现实体".format(sentence))
        return ''
    if b_loc_idx in pre_tag:

        entity_start_idx = pre_tag.index(b_loc_idx)
    else:

        entity_start_idx = pre_tag.index(i_loc_idx)
    entity_list = []
    entity_list.append(sentence_list[entity_start_idx])
    for i in range(entity_start_idx+1,pre_tag_len):
        if pre_tag[i] == i_loc_idx:
            entity_list.append(sentence_list[i])
        else:
            break
    return "".join(entity_list)


def semantic_matching(model,tokenizer,question,attribute_list,answer_list,max_length):

    assert len(attribute_list) == len(answer_list)

    pad_token = 0
    pad_token_segment_id = 1
    features = []
    for (ex_index, attribute) in enumerate(attribute_list):
        inputs = tokenizer.encode_plus(
            text = question,
            text_pair = attribute,
            add_special_tokens = True,
            max_length = max_length,
            truncate_first_sequence = True
        )
        input_ids, token_type_ids = inputs["input_ids"], inputs["token_type_ids"]
        attention_mask = [1] * len(input_ids)

        padding_length = max_length - len(input_ids)
        input_ids = input_ids + ([pad_token] * padding_length)
        attention_mask = attention_mask + ([0] * padding_length)
        token_type_ids = token_type_ids + ([pad_token_segment_id] * padding_length)

        assert len(input_ids) == max_length, "Error with input length {} vs {}".format(len(input_ids), max_length)
        assert len(attention_mask) == max_length, "Error with input length {} vs {}".format(len(attention_mask),
                                                                                            max_length)
        assert len(token_type_ids) == max_length, "Error with input length {} vs {}".format(len(token_type_ids),
                                                                                            max_length)
        features.append(
            SimInputFeatures(input_ids = input_ids,attention_mask = attention_mask,token_type_ids = token_type_ids)
        )
    all_input_ids = torch.tensor([f.input_ids for f in features], dtype=torch.long)
    all_attention_mask = torch.tensor([f.attention_mask for f in features], dtype=torch.long)
    all_token_type_ids = torch.tensor([f.token_type_ids for f in features], dtype=torch.long)

    assert all_input_ids.shape == all_attention_mask.shape
    assert all_attention_mask.shape == all_token_type_ids.shape


    dataset = TensorDataset(all_input_ids, all_attention_mask, all_token_type_ids)
    sampler = SequentialSampler(dataset)
    dataloader = DataLoader(dataset, sampler=sampler,batch_size=128)

    data_num = all_attention_mask.shape[0]
    batch_size = 128

    all_logits = None
    for i in range(0,data_num,batch_size):
        model.eval()
        with torch.no_grad():
            inputs = {'input_ids': all_input_ids[i:i+batch_size].to(device),
                      'attention_mask': all_attention_mask[i:i+batch_size].to(device),
                      'token_type_ids': all_token_type_ids[i:i+batch_size].to(device),
                      'labels': None
                      }
            outputs = model(**inputs)
            logits = outputs[0]
            logits = logits.softmax(dim = -1)

            if all_logits is None:
                all_logits = logits.clone()
            else:
                all_logits = torch.cat([all_logits,logits],dim = 0)
    pre_rest = all_logits.argmax(dim = -1)
    if 0 == pre_rest.sum():
        return torch.tensor(-1)
    else:
        return pre_rest.argmax(dim = -1)



def select_neo4j(entity):
    graph = Graph("neo4j://121.5.8.40:7687", auth = ("neo4j", "nekoCoin"))
    nodes = NodeMatcher(graph)

    results = nodes.match(_id = entity).first()
    if results is None:
        return None
    keys = results.keys()
    resList = []
    if "人物" in results._labels:
        resList = buildCharacterProperties(results)
    elif "事件" in results._labels:
        resList = buildEventProperties(results)
    elif "地点" in results._labels:
        resList = buildPlaceProperties(results)
    return resList

def buildCharacterProperties(result):
    resList = []
    keys = result.keys()
    for key in keys:
        val = result.get(key)
        # if len(val) == 0:
        #     continue
        if key in character_other_name:
            temp = (character_other_name[key], val)
        elif key == "配偶":
            if result.get("性别") == "男":
                temp = (character_other_name["男性配偶代词"], val)
            else:
                temp = (character_other_name["女性配偶代词"], val)
        else:
            temp = (key, val)
        resList.append(temp)
    return resList

def buildEventProperties(result):
    resList = []
    keys = result.keys()
    for key in keys:
        val = result.get(key)
        if key in event_other_name:
            temp = (event_other_name[key], val)
        else:
            temp = (key, val)
        resList.append(temp)
    return resList

def buildPlaceProperties(result):
    resList = []
    keys = result.keys()
    for key in keys:
        val = result.get(key)
        if key in place_other_name:
            temp = (place_other_name[key], val)
        else:
            temp = (key, val)
        resList.append(temp)
    return resList

# 文字直接匹配，看看属性的词语在不在句子之中
def text_match(attribute_list,answer_list,sentence):

    assert len(attribute_list) == len(answer_list)

    idx = -1
    for i,attribute in enumerate(attribute_list):
        if isinstance(attribute, list):
            for oth_name in attribute:
                if oth_name in sentence:
                    idx = i
                    # return attribute_list[idx], answer_list[idx]
        elif attribute in sentence:
            idx = i
            break
    if -1 != idx:
        return attribute_list[idx], answer_list[idx]
    else:
        return "",""

def trans2Json(prefix, ans):
    return json.dumps({'success': True, 'content': {'prefix': prefix, 'ans': ans}}, sort_keys=True, indent=4, separators=(',', ': '))

@app.route('/question/<string:query>')
def answer(query):
    query = query.strip()
    entity = get_entity(model=ner_model, tokenizer=tokenizer, sentence=query, max_len=64)
    print("实体:", entity)
    if '' == entity:
        # return json.dumps({'prefix': query, 'ans': "未发现实体"}, sort_keys=True, indent=4, separators=(',', ': '))
        return trans2Json(query, "未发现实体")

    triple_list = select_neo4j(entity)
    if triple_list is None:
        # return json.dumps({'prefix': query, 'ans': "数据库中未找到" + entity}, sort_keys=True, indent=4, separators=(',', ': '))
        return trans2Json(query, "数据库中未找到" + entity)
    triple_list = list(triple_list)
    if 0 == len(triple_list):
        # return json.dumps({'prefix': query, 'ans': "未找到{}相关信息".format(entity)}, sort_keys=True, indent=4, separators=(',', ': '))
        return trans2Json(query, "未找到{}相关信息".format(entity))
    triple_list = list(zip(*triple_list))
    attribute_list = triple_list[0]
    answer_list = triple_list[1]
    attribute, answer = text_match(attribute_list, answer_list, query)
    if attribute != '' and answer != '':
        ret = "{}的{}是{}".format(entity, attribute, answer)

    elif len(answer) == 0 and len(attribute) != 0:
        # return json.dumps({'prefix': query, 'ans': "数据库未记录" + entity + "的" + str(attribute)}, sort_keys=True, indent=4, separators=(',', ': '))
        return trans2Json(query, "数据库未记录" + entity + "的" + attribute[0])
    else:
        attribute_idx = semantic_matching(sim_model, tokenizer, query, attribute_list, answer_list, 64).item()
        if -1 == attribute_idx:
            ret = ''
        else:
            attribute = attribute_list[attribute_idx]
            answer = answer_list[attribute_idx]
            ret = "{}的{}是{}".format(entity, attribute, answer)
    if '' == ret:
        # return json.dumps({'prefix': query, 'ans': "未找到{}相关信息".format(entity)}, sort_keys=True, indent=4, separators=(',', ': '))
        return trans2Json(query, "未找到{}相关信息".format(entity))
    else:
        # return json.dumps({'prefix': query, 'ans': answer}, sort_keys=True, indent=4, separators=(',', ': '))
        return trans2Json(query, answer)


with torch.no_grad():
        print("model loading-----")
        tokenizer_inputs = ()
        tokenizer_kwards = {'do_lower_case': False,
                            'max_len': 64,
                            'vocab_file': './input/config/bert-base-chinese-vocab.txt'}
        ner_processor = NerProcessor()
        sim_processor = SimProcessor()
        tokenizer = BertTokenizer(*tokenizer_inputs, **tokenizer_kwards)


        ner_model = get_ner_model(config_file = './input/config/bert-base-chinese-config.json',
                                  pre_train_model = './output/best_ner.bin',label_num = len(ner_processor.get_labels()))
        ner_model = ner_model.to(device)
        ner_model.eval()

        sim_model = get_sim_model(config_file='./input/config/bert-base-chinese-config.json',
                                  pre_train_model='./output/best_sim.bin',
                                  label_num=len(sim_processor.get_labels()))

        sim_model = sim_model.to(device)
        sim_model.eval()

        print("model load finished")

