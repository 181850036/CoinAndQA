# -*- coding: UTF-8 -*-
# @Time    : 2019/4/14 13:21
# @Author  : xiongzongyang
# @Site    : 
# @File    : question_template.py
# @Software: PyCharm

'''



9:nnt 参演评分 小于 x
10:nnt 电影类型
11:nnt nnr 合作 电影列表
12:nnt 电影数量
13:nnt 出生日期
'''
from query import Query
import json
import re


class QuestionTemplate():
    def __init__(self):
        self.q_template_dict = {
            0: self.get_location_time_happen_what_event,
            1: self.get_location_time_born_what_figure,
            2: self.get_figure_what_relatives,
            3: self.get_figure_what_social,
            4: self.get_figure_infos,
            5: self.get_location_infos,
        }

        # 连接数据库
        self.graph = Query()
        # 测试数据库是否连接上
        # result=self.graph.run("match (m:Movie)-[]->() where m.title='卧虎藏龙' return m.rating")
        # print(result)
        # exit()

    def get_question_answer(self, question, template):
        # 如果问题模板的格式不正确则结束
        # template的格式：分类下标\t抽象问题
        assert len(str(template).strip().split("\t")) == 2
        template_id, template_str = int(str(template).strip().split("\t")[0]), str(template).strip().split("\t")[1]
        self.template_id = template_id
        self.template_str2list = str(template_str).split()

        # 预处理问题
        question_word, question_flag = [], []
        # question的格式:['杜陵/ns', '公元222年/ta', '有/v', '谁/r', '出生/v', '了/ul']
        for one in question:
            word, flag = one.split("/")
            question_word.append(str(word).strip())
            question_flag.append(str(flag).strip())
        assert len(question_flag) == len(question_word)
        self.question_word = question_word
        self.question_flag = question_flag
        self.raw_question = question
        # 根据问题模板来做对应的处理，获取答案
        answer = self.q_template_dict[template_id]()
        return answer

    # 获取人物名字
    def get_figure_name(self):
        ## 获取nm在原问题中的下标
        tag_index = self.question_flag.index("nr")
        ## 获取电影名称
        figure_name = self.question_word[tag_index]
        return figure_name

    # 获取地名
    def get_location_name(self):
        tag_index = self.question_flag.index("ns")
        location_name = self.question_word[tag_index]
        return location_name

    # 获取时间
    def get_time_name(self):
        tag_index = self.question_flag.index("ta")
        time_name = self.question_word[tag_index]
        return time_name

    def deal_cql(self, cql):
        event_node_list = list(set(self.graph.run(cql)))
        ans = []
        for e in event_node_list:
            nodesStr = json.dumps(e, ensure_ascii=False)
            nodes = json.loads(nodesStr)  # json对象TODO
            ans.append(nodes['_id'])
        return ans

    def deal_ret(self, prefix, ans):
        ret = dict()
        ret['prefix'] = prefix
        ret['ans'] = ans
        if isinstance(ret['ans'], dict):
            for key in list(ret['ans'].keys()):
                if len(ret['ans'].get(key)) == 0:
                    ret['ans'].pop(key)
        return ret

    # 0:ns ta 发生事件
    def get_location_time_happen_what_event(self):
        location_name = self.get_location_name()
        time_name = self.get_time_name()
        cql = f"MATCH (:`时间`{{_id:'{time_name}'}})< -[:`发生时间`]-(n:`事件`)-[:`发生地点`]- >(l:`地点`) where l._id contains '{location_name}'  return n"
        print(cql)
        ans = self.deal_cql(cql)
        return [self.deal_ret(time_name + '在' + location_name + '发生的事件有',ans)]

    # 1:ns ta 出生人物
    def get_location_time_born_what_figure(self):
        location_name = self.get_location_name()
        time_name = self.get_time_name()
        cql = f"MATCH (:`时间`{{_id:'{time_name}'}})< -[:`出生时间`]-(n:`人物`)-[:`籍贯`]- >(l:`地点`) where l._id contains '{location_name}' return n"
        print(cql)
        ans = self.deal_cql(cql)
        return [self.deal_ret(time_name + '在' + location_name + '出生的人物有', ans)]

    def deal_multi_figure(self, relations):
        figure_name = self.get_figure_name()
        ans = dict()
        for r in relations:
            cql = f"MATCH (f:`人物`)<-[:`{r}`]-(n:`人物`) where f._id contains '{figure_name}' return n"
            ans[r] = self.deal_cql(cql)
        return ans

    # 2:nr 亲属
    def get_figure_what_relatives(self):
        relations = ['父亲', '母亲', '儿子', '女儿', '配偶']
        ans = self.deal_multi_figure(relations)
        # 双向找配偶
        figure_name = self.get_figure_name()
        spouseCql = f"MATCH (f:`人物`)-[:`配偶`]->(n:`人物`) where f._id contains '{figure_name}' return n"
        ans['配偶'].extend(self.deal_cql(spouseCql))
        return [self.deal_ret(figure_name + '的亲属有：', ans)]

    # 3:nr 社会关系
    def get_figure_what_social(self):
        # '同事','同学','朋友','连坐'均是双向关系，找单边即可
        relations = ['主人', '门客', '荐主', '被举人', '支持者', '同事', '同学', '朋友', '连坐']
        ans = self.deal_multi_figure(relations)
        # 找支持的人
        figure_name = self.get_figure_name()
        supCql = f"MATCH (f:`人物`)-[:`支持者`]->(n:`人物`) where f._id contains '{figure_name}' return n"
        ans['支持'] = self.deal_cql(supCql)
        return [self.deal_ret(figure_name + '的社会关系有：', ans)]

    # 4:nr 人物信息
    def get_figure_infos(self):
        figure_name = self.get_figure_name()
        ans = dict()
        cql = f"MATCH (n:`人物`) where n._id contains '{figure_name}' return n"
        print(cql)
        event_node_list = list(set(self.graph.run(cql)))
        for e in event_node_list:
            nodesStr = json.dumps(e, ensure_ascii=False)
            nodes = json.loads(nodesStr)  # json对象TODO
            for k in nodes.keys():
                if not re.match("_|[a-zA-Z]", k):
                    if len(nodes[k]) > 0:
                        ans[k] = nodes[k]
        return [self.deal_ret(figure_name + '的详细介绍：', ans)]

    # 5:ns 地理信息
    def get_location_infos(self):
        location_name = self.get_location_name()
        bigans = dict()
        cql = f"MATCH (n:`地点`) where n._id contains '{location_name}' return n"
        event_node_list = list(set(self.graph.run(cql)))
        for e in event_node_list:
            ans = dict()
            nodesStr = json.dumps(e, ensure_ascii=False)
            nodes = json.loads(nodesStr)  # json对象TODO
            for k in nodes.keys():
                if not re.match("_|[a-zA-Z]", k):
                    if len(nodes[k]) > 0:
                        ans[k] = nodes[k]
            bigans[nodes['_id']] = ans

        bigret = list()
        for k in bigans.keys():
            ret = self.deal_ret(k + '的详细介绍：', bigans[k])
            bigret.append(ret)
        return bigret
