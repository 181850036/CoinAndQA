from py2neo import Graph, Node, Relationship, NodeMatcher, RelationshipMatcher
import csv
import json

graph = Graph("http://121.5.8.40:7474", auth=("neo4j", "nekoCoin"))

matcher = NodeMatcher(graph)
relationshipMatcher = RelationshipMatcher(graph)


def handleLocationJson():
    with open('./location.json', 'r',encoding='UTF-8') as f:
        data = json.load(f)
        for item in data:
            _id = ''
            locNameNow = ''
            locatedin = ''
            existingPeriod = ''
            relatedSentences = ''
            province = ''
            otherName = ''
            sameAs = ''
            if "id" in item:
                _id = item["id"]
            if "locNameNow" in item:
                locNameNow = item["locNameNow"]
            if "locatedin" in item:
                locatedin = item["locatedin"]
            if "existingPeriod" in item:
                existingPeriod = item["existingPeriod"]
            if "relatedSentences" in item:
                relatedSentences = item["relatedSentences"]
            if "province" in item:
                province = item["province"]
            if "otherName" in item:
                otherName = item["otherName"]
            if "sameAs" in item:
                sameAs = item["sameAs"]
            if len(matcher.match(_id=_id)) == 0:
                node = Node('地点', graphId = "forQuestion", _id = _id, 地名 = _id, 现在位置 = locNameNow, 地点 = locatedin, 存在时期 = existingPeriod, 
                相关句子 = relatedSentences, 省份 = province, 其他名字 = otherName, 同于 = sameAs)
                graph.create(node)


if __name__ == '__main__':
    handleLocationJson()
