
from py2neo import Graph, Node, Relationship, NodeMatcher, RelationshipMatcher
import csv
import json

# 连接neo4j数据库，输入地址、用户名、密码

graph = Graph("http://121.5.8.40:7474", auth=("neo4j", "nekoCoin"))

matcher = NodeMatcher(graph)
relationshipMatcher = RelationshipMatcher(graph)


def buildRelations():

    
    properties={'graphId':'forQuestion'}
    with open('./figure.json', 'r',encoding='UTF-8') as f2:
        data = json.load(f2)
        for item in data:
            _id = ''
            gender = ''
            attachToDynasty  = ''
            biography = ''
            deadTime = ''
            anotherName = ''
            causeOfDeath = ''
            surname = ''
            name = ''
            nativePlace = ''
            figurePeriod = ''
            birthday = ''
            officialTitle = []
            alias = ''
            feudaTitle = []
            student = []
            spouse = []
            son = []
            supporter = []
            classmates = [] # 3个
            host = ''
            supportee = []
            guest = []
            mother = ''
            friend = []
            father = ''
            colleague = [] #只有两个人有这个字段 考虑不加入
            daughter = []
            teacher = []
            attachToMembershipGroup = ''

            lianzuo = []
            jianzhu = []
            guli = ''
            guzhu = ''
            beijuren = []

            if "lianzuo" in item:
                lianzuo = item["lianzuo"]

            if "beijuren" in item:
                beijuren = item["beijuren"]

            if "jianju" in item:
                jianzhu = item["jianju"]
            
            if "guli" in item:
                guli = item["guli"]
            
            if "guzhu" in item:
                guzhu = item["guzhu"]
            
            if "classmates" in item:
                classmates = item["classmates"]
            
            if "colleague" in item:
                colleague = item["colleague"]

            if "id" in item :
                _id = item["id"]

            if "gender" in item :
                gender = item["gender"]

            if "attachToDynasty" in item:
                dynasty = item["attachToDynasty"]

            if "biography" in item :
                biography = item["biography"]

            if "anothername" in item :
                anotherName = item["anothername"]

            if "causeOfDeath" in item:
                causeOfDeath = item["causeOfDeath"]

            if "surname" in item:
                surname = item["surname"]

            if "name" in item:
                name = item["name"]

            if "nativePlace" in item:
                nativePlace = item["nativePlace"]

            if "figurePeriod" in item:
                figurePeriod = item["figurePeriod"]

            #if len(item["birthday"]) > 0:
            if "birthday" in item:
                birthday = item["birthday"]

            if "officialTitle" in item :
                officialTitle = item["officialTitle"]

            if "alias" in item :
                alias = item["alias"]

            if "feudaTitle" in item :
                feudaTitle = item["feudaTitle"]

            if "attachToMembershipGroup" in item :
                attachToMembershipGroup = item["attachToMembershipGroup"]

            if "student" in item :
                student = item["student"]

            if "deadTime" in item :
                deadTime = item["deadTime"]

            if "spouse" in item :
                spouse = item["spouse"]

            if "son" in item :
                son = item["son"]

            if "supporter" in item :
                supporter = item["supporter"]

            if "host" in item :
                host = item["host"]

            if "guest" in item :
                guest = item["guest"]

            if "mother" in item :
                mother = item["mother"]

            if "friend" in item :
                friend = item["friend"]

            if "father" in item :
                father = item["father"]

            if "daughter" in item :
                daughter = item["daughter"]

            if "teacher" in item :
                teacher = item["teacher"]


            #if 1==1:
            node1 = matcher.match("人物", _id = _id).first()
            if node1:
                if nativePlace != '':
                    nodePlace = matcher.match("地点", _id = nativePlace).first()
                    if nodePlace:
                        relationship = Relationship(node1, "籍贯", nodePlace, **properties)
                        graph.merge(relationship)
                if birthday != '':
                    nodeBirthday = matcher.match("时间", _id = birthday).first()
                    if nodeBirthday:
                        relationship = Relationship(node1, "出生时间", nodeBirthday, **properties)
                        graph.merge(relationship)
                if deadTime != '':  
                    nodeDeadTime = matcher.match("时间", _id = deadTime).first()
                    if nodeDeadTime:
                        relationship = Relationship(node1, "死亡时间", nodeDeadTime, **properties)
                        graph.merge(relationship)
                if mother != '':
                    node2 = matcher.match("人物", _id = mother).first()
                    if node2:
                        relationship = Relationship(node2, "母亲", node1, **properties)
                        graph.merge(relationship)
                if father != '':
                    node2 = matcher.match("人物", _id = father).first()
                    if node2:
                        relationship = Relationship(node2, "父亲", node1, **properties)
                        graph.merge(relationship)
                if host != '':
                    node2 = matcher.match("人物", _id = host).first()
                    if node2:
                        relationship = Relationship(node2, "主人", node1, **properties)
                        graph.merge(relationship)
                if len(son) > 0:
                    for s in son:
                        node2 = matcher.match("人物", 姓名 = s).first()
                        if node2:
                            relationship = Relationship(node2, "儿子", node1, **properties)
                            graph.merge(relationship)
                if len(daughter) > 0:
                    for s in daughter:
                        node2 = matcher.match("人物", _id = s).first()
                        if node2:
                            relationship = Relationship(node2, "女儿", node1, **properties)
                            graph.merge(relationship)
                if len(teacher) > 0:
                    for s in teacher:
                        node2 = matcher.match("人物", _id = s).first()
                        if node2:
                            relationship = Relationship(node2, "老师", node1, **properties)
                            graph.merge(relationship)
                if len(student) > 0:
                    for s in student:
                        node2 = matcher.match("人物", _id = s).first()
                        if node2:
                            relationship = Relationship(node2, "学生", node1, **properties)
                            graph.merge(relationship)
                if len(spouse) > 0:
                    for s in spouse:
                        node2 = matcher.match("人物", _id = s).first()
                        if node2:
                            relationship = Relationship(node1, "配偶", node2, **properties)
                            graph.merge(relationship)
                if len(friend) > 0:
                    for s in friend:
                        node2 = matcher.match("人物", _id = s).first()
                        if node2:
                            relationship = Relationship(node1, "朋友", node2, **properties)
                            graph.merge(relationship)
                if len(guest) > 0:
                    for s in guest:
                        node2 = matcher.match("人物", _id = s).first()
                        if node2:
                            relationship = Relationship(node2, "门客", node1 ,**properties)
                            graph.merge(relationship)
                if len(supporter) > 0:
                    for s in supporter:
                        node2 = matcher.match("人物", _id = s).first()
                        if node2:
                            relationship = Relationship(node2, "支持者", node1, **properties)
                            graph.merge(relationship)
                
                if len(beijuren) > 0:
                    for s in beijuren:
                        node2 = matcher.match("人物", 姓名 = s).first()
                        if node2:
                            relationship = Relationship(node2, "被举人", node1, **properties)
                            graph.merge(relationship)
    
                if len(jianzhu) > 0:
                    for s in jianzhu:
                        node2 = matcher.match("人物", 姓名 = s).first()
                        if node2:
                            relationship = Relationship(node2, "荐主", node1, **properties)
                            graph.merge(relationship)

                if len(classmates) > 0:
                    for s in classmates:
                        node2 = matcher.match("人物", 姓名 = s).first()
                        if node2:
                            relationship = Relationship(node1, "同学", node2, **properties)
                            graph.merge(relationship)

                if len(lianzuo) > 0:
                    for s in lianzuo:
                        node2 = matcher.match("人物", 姓名 = s).first()
                        if node2:
                            relationship = Relationship(node1, "连坐", node2, **properties)
                            graph.merge(relationship)
                # if guzhu != '':  
                #     node2 = matcher.match("人物", _id = guzhu).first()
                #     if node2:
                #         relationship = Relationship(node2, "雇主", node1, **properties)
                #         graph.merge(relationship)
        
                # if guli != '':  
                #     node2 = matcher.match("人物", _id = guli).first()
                #     if node2:
                #         relationship = Relationship(node2, "雇吏", node1, **properties)
                #         graph.merge(relationship)

                if colleague != '':  
                    node2 = matcher.match("人物", _id = colleague).first()
                    if node2:
                        relationship = Relationship(node1, "同事", node2, **properties)
                        graph.merge(relationship)
                if len(supportee) > 0:
                    for s in supportee:
                        node2 = matcher.match("人物", _id = s).first()
                        if node2:
                            relationship = Relationship(node1, "支持者", node2, **properties)
                            graph.merge(relationship)
                
    with open('./event.json', 'r',encoding='UTF-8') as f1:
        data = json.load(f1)
        for item in data:
            _id = ''
            cause = ''
            influence = ''
            process = ''
            where = ''
            when = ''
            participateFigure = []
            relatedEvent = []
            if "id" in item:
                _id = item["id"]
            if "cause" in item:
                cause = item["cause"]
            if "influence" in item:
                influence = item["influence"]
            if "process" in item:
                process = item["process"]
            if "where" in item:
                where = item["where"]
            if "when" in item:
                when = item["when"]["id"]
            if "participateFigure" in item:
                participateFigure = item["participateFigure"]
            if "relatedEvent" in item:
                relatedEvent = item["relatedEvent"]
            node1 = matcher.match("事件",_id = _id).first()
            if node1:
        
                if len(participateFigure) > 0:
                    for figure in participateFigure:
                        nodeFigure = matcher.match("人物", _id = figure).first()
                        if nodeFigure:
                            relationship = Relationship(nodeFigure, "参与", node1, **properties)
                            graph.merge(relationship)

                if len(relatedEvent) > 0:
                    for event in relatedEvent:
                        nodeEvent = matcher.match("事件", _id = event).first()
                        if nodeEvent:
                            relationship = Relationship(node1, "相关", nodeEvent, **properties)
                            graph.merge(relationship)
                if when != '':
                    nodeTime = matcher.match("时间", _id = when).first()
                    if nodeTime:
                        relationship = Relationship(node1, "发生时间", nodeTime, **properties)
                        graph.merge(relationship)
                if len(where) > 0:
                    for place in where:
                        nodePlace = matcher.match("地点", _id = place).first()
                        if nodePlace:
                            relationship = Relationship(node1, "发生地点", nodePlace, **properties)
                            graph.merge(relationship)
if __name__ == '__main__':
    buildRelations()
