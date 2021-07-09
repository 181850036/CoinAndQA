from py2neo import Graph, Node, Relationship, NodeMatcher, RelationshipMatcher
import csv
import json

graph = Graph("http://121.5.8.40:7474", auth=("neo4j", "nekoCoin"))

matcher = NodeMatcher(graph)
relationshipMatcher = RelationshipMatcher(graph)


def handleTimeJson():
    with open('./time.json', 'r',encoding='UTF-8') as f:
        data = json.load(f)
        for item in data:
            _id = ''
            start = ''
            end = ''
            _id = item["id"]
            if "start" in item:
                start = item["start"]
            if "end" in item:
                end = item["end"]
            if len(matcher.match(_id=_id)) == 0:
                node = Node('时间', graphId = "forQuestion", _id = _id, 年份 = _id, 开始时间 = start, 结束时间 = end)
                graph.create(node)


if __name__ == '__main__':
    handleTimeJson()
