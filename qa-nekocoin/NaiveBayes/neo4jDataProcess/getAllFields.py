from py2neo import Graph, Node, Relationship, NodeMatcher, RelationshipMatcher
import csv
import json

graph = Graph("http://127.0.0.1:7474", auth=("neo4j", "neo4j"))

matcher = NodeMatcher(graph)
relationshipMatcher = RelationshipMatcher(graph)


def handleTimeJson():
    fields = set()
    with open('./figure.json', 'r',encoding='UTF-8') as f:
        data = json.load(f)
        for item in data:
            words = item.keys()
            for i in words:
                fields.add(i)
    print(len(fields))

if __name__ == '__main__':
    handleTimeJson()
