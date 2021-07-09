from py2neo import Graph, Node, Relationship, NodeMatcher, RelationshipMatcher
import csv
import json

graph = Graph("http://121.5.8.40:7474", auth=("neo4j", "nekoCoin"))

matcher = NodeMatcher(graph)
relationshipMatcher = RelationshipMatcher(graph)


def handleEventJson():
    with open('./event.json', 'r',encoding='UTF-8') as f:
        data = json.load(f)
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
            if len(matcher.match(_id=_id)) == 0:
                node = Node('事件', graphId = "forQuestion", _id = _id, 原因 = cause, 影响 = influence, 经过 = process, 地点 = where, 
                    时间 = when, 参与人物 = participateFigure, 相关 = relatedEvent)
                graph.create(node)


if __name__ == '__main__':
    handleEventJson()
