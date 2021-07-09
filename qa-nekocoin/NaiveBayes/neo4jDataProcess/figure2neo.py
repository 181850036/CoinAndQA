
from py2neo import Graph, Node, Relationship, NodeMatcher, RelationshipMatcher
import csv
import json

# 连接neo4j数据库，输入地址、用户名、密码

graph = Graph("http://121.5.8.40:7474", auth=("neo4j", "nekoCoin"))
matcher = NodeMatcher(graph)
relationshipMatcher = RelationshipMatcher(graph)


def handleFigureJson():
    with open('./figure.json', 'r',encoding='UTF-8') as f:
        data = json.load(f)
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
            colleague = '' #只有两个人有这个字段 考虑不加入
            daughter = []
            teacher = []
            attachToMembershipGroup = ''

            lianzuo = []
            jianzhu = []
            # guli = ''
            # guzhu = ''
            beijuren = []

            if "supportee" in item:
                supportee = item["supportee"]

            if "lianzuo" in item:
                lianzuo = item["lianzuo"]

            if "beijuren" in item:
                beijuren = item["beijuren"]

            if "jianju" in item:
                jianzhu = item["jianju"]
            
            # if "guli" in item:
            #     guli = item["guli"]
            
            # if "guzhu" in item:
            #     guzhu = item["guzhu"]
            
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


            if len(matcher.match(_id=_id)) == 0:
                node = Node('人物', graphId = "forQuestion",  _id = _id, 姓名 = _id, 性别 = gender, 所属朝代 = dynasty, 传记 = biography, 字 = anotherName, 
                    死因 = causeOfDeath, 姓 = surname, 名 = name, 籍贯 = nativePlace, 时期 = figurePeriod, 生日 = birthday, 去世年份 = deadTime, 官衔 = officialTitle,
                    别名 = alias, 封号 = feudaTitle, 国家 = attachToMembershipGroup, 学生 = student, 配偶 = spouse, 儿子 = son, 
                    支持者 = supporter, 门客 = guest, 母亲 = mother, 朋友 = friend, 父亲 = father, 女儿 = daughter, 老师 = teacher,
                    同学 = classmates, 被举人 = beijuren, 荐主 = jianzhu, 同事 = colleague, 连坐 = lianzuo, 支持 = supportee)
                graph.create(node)

if __name__ == '__main__':
    handleFigureJson()
