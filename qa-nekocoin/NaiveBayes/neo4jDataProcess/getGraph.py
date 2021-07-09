import json

# #91cc75蓝色 #91cc75绿色 #fac858黄色 #ee6666红色 #9a60b4紫色
retGraph = dict()
retGraph['name'] = '赤壁图谱'
retGraph['graphId'] = 'tK'
retGraph['entities'] = []
retGraph['links'] = []
retGraph['categories'] = [
    {"graphId": "tK",
     "name": "蜀",
     "categoryProperties": {
         "itemStyle": {"color": "#ee6666"}}},
    {"graphId": "tK",
     "name": "魏",
     "categoryProperties": {
         "itemStyle": {"color": "#91cc75"}}},
    {"graphId": "tK",
     "name": "吴",
     "categoryProperties": {
         "itemStyle": {"color": "#91cc75"}}},
    {"graphId": "tK",
     "name": "地点",
     "categoryProperties": {
         "itemStyle": {"color": "#fac858"}}},
    {"graphId": "tK",
     "name": "时间",
     "categoryProperties": {
        "itemStyle": {"color": "#9a60b4"}}},
    {"graphId": "tK",
     "name": "事件",
     "categoryProperties": {
         "itemStyle": {"color": "#ea7ccc"}}},
]

with open('./event.json', 'r', encoding='utf8')as fp1:
    json_data = json.load(fp1)
events=[]
chibi=dict()
for event in json_data:
    if event['id'] == '赤壁之战':
        chibi=event
        events.append(event)
for event in json_data:
    if 'relatedEvent' in event.keys():
        if event['id'] in chibi['relatedEvent'] or '赤壁之战' in event['relatedEvent']:
            events.append(event)
figs=chibi['participateFigure']
times=chibi['when']['id']
ptr=0
for e in events:
    node=dict()
    node['graphId']='tK'
    node['name']=e['id']
    pr=dict()
    if e['id']=='赤壁之战':
        pr['symbolSize'] = 30
        pr['value']=30
    else:
        pr['symbolSize'] = 23
        pr['value']=23
    pr['x']=(ptr%8)*30
    pr['y']=(ptr/8)*30
    pr['category']=5
    node['entityProperties']=pr
    node['contextProperties']=dict()
    for k in e.keys():
        if k in ['cause','influence','process']:
            node['contextProperties'][k]=e[k]
    retGraph['entities'].append(node)
    ptr+=1
with open('./figure.json', 'r', encoding='utf8')as fp2:
    json_data = json.load(fp2)
for fig in json_data:
    if fig['id'] in figs:
        node=dict()
        node['graphId'] = 'tK'
        node['name'] = fig['id']
        pr = dict()
        pr['symbolSize']=18
        pr['value'] = 18
        pr['x'] = (ptr % 8) * 30
        pr['y'] = (ptr / 8) * 30
        if 'attachToMembershipGroup' in fig.keys():
            if fig['attachToMembershipGroup'] == '魏国':
                pr['category'] = 1
            elif fig['attachToMembershipGroup'] == '蜀汉' or fig['attachToMembershipGroup']=='蜀国':
                pr['category'] = 0
            else:
                pr['category'] = 2
        else:
            if fig['attachToDynasty']=='三国魏':
                pr['category']=1
            elif fig['attachToDynasty']=='三国蜀':
                pr['category']=0
            else:
                pr['category']=2
        node['entityProperties']=pr
        node['contextProperties']=dict()
        for k in fig.keys():
            if k!='id'  and k!= 'name' and k!='surname':
                node['contextProperties'][k]=fig[k]
        retGraph['entities'].append(node)
        ptr+=1
with open('./location.json', 'r', encoding='utf8')as fp3:
    json_data = json.load(fp3)
locs=[]
for loc in json_data:
    if loc['id'] in chibi['where']:
        locs.append(loc['id'])
        node = dict()
        node['graphId'] = 'tK'
        node['name'] = loc['id']
        pr = dict()
        pr['symbolSize'] = 15
        pr['value'] = 15
        pr['x'] = (ptr % 8) * 30
        pr['y'] = (ptr / 8) * 30
        pr['category'] = 3
        node['entityProperties'] = pr
        node['contextProperties'] = dict()
        for k in loc.keys():
            if k != 'id':
                node['contextProperties'][k] = loc[k]
        retGraph['entities'].append(node)
        ptr += 1
with open('./time.json', 'r', encoding='utf8')as fp4:
    json_data = json.load(fp4)
for t in json_data:
    if t['id']=='公元208年':
        node = dict()
        node['graphId'] = 'tK'
        node['name'] = t['id']
        pr = dict()
        pr['symbolSize'] = 10
        pr['value'] = 10
        pr['x'] = (ptr % 8) * 30
        pr['y'] = (ptr / 8) * 30
        pr['category'] = 4
        node['entityProperties'] = pr
        node['contextProperties'] = dict()
        for k in t.keys():
            if k != 'id':
                node['contextProperties'][k] = t[k]
        retGraph['entities'].append(node)
        ptr += 1
for event in events:
    if 'relatedEvent' in event.keys():
        if event['id'] in chibi['relatedEvent']:
            lk=dict()
            lk['graphId']='tK'
            lk['source']='赤壁之战'
            lk['target']=event['id']
            lk['value']='相关'
            retGraph['links'].append(lk)
        elif  '赤壁之战' in event['relatedEvent']:
            lk=dict()
            lk['graphId']='tK'
            lk['source']=event['id']
            lk['target']='赤壁之战'
            lk['value']='相关'
            retGraph['links'].append(lk)
    for lc in event['where']:
        if lc in locs:
            lk = dict()
            lk['graphId'] = 'tK'
            lk['source'] = event['id']
            lk['target'] = lc
            lk['value'] = '发生地点'
            retGraph['links'].append(lk)
    if 'participateFigure' in event.keys():
        for fg in event['participateFigure']:
            if fg in figs:
                lk = dict()
                lk['graphId'] = 'tK'
                lk['source'] = fg
                lk['target'] = event['id']
                lk['value'] = '参与'
                retGraph['links'].append(lk)
    if event['when']['id'] == '公元208年':
        lk = dict()
        lk['graphId'] = 'tK'
        lk['source'] = event['id']
        lk['target'] = '公元208年'
        lk['value'] = '发生时间'
        retGraph['links'].append(lk)
with open('./figure.json', 'r', encoding='utf8')as fp2:
    json_data = json.load(fp2)
for fig in json_data:
    if fig['id'] in figs:
        if 'friend'in fig.keys():
            for fr in fig['friend']:
                if fr in figs:
                    lk = dict()
                    lk['graphId'] = 'tK'
                    lk['source'] = fig['id']
                    lk['target'] = fr
                    lk['value'] = '朋友'
                    retGraph['links'].append(lk)
        if 'beijuren' in fig.keys():
            for bj in fig['beijuren']:
                if bj in figs:
                    lk = dict()
                    lk['graphId'] = 'tK'
                    lk['source'] = fig['id']
                    lk['target'] = bj
                    lk['value'] = '被举人'
                    retGraph['links'].append(lk)
        if 'jianju' in fig.keys():
            for jz in fig['jianju']:
                if jz in figs:
                    lk = dict()
                    lk['graphId'] = 'tK'
                    lk['source'] = fig['id']
                    lk['target'] = jz
                    lk['value'] = '荐主'
                    retGraph['links'].append(lk)
print(retGraph)
