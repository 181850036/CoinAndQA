package org.njuse.nekocoin.nekocoin.daoimpl;

import org.neo4j.driver.*;

import org.njuse.nekocoin.nekocoin.dao.NodeDao;
import org.njuse.nekocoin.nekocoin.dao.RelationshipDao;
import org.njuse.nekocoin.nekocoin.entity.Category;
import org.njuse.nekocoin.nekocoin.entity.Graph;
import org.njuse.nekocoin.nekocoin.entity.Entity;
import org.njuse.nekocoin.nekocoin.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.neo4j.driver.Values.parameters;

@Repository
public class NodeDaoImpl implements NodeDao {
    private final Driver driver;
    private final RelationshipDao relationShipDaoImpl;
    private static String graphLabel = "graph";
    private static String entityLabel = "entity";
    private static String categoryLabel = "category";
    @Autowired
    public NodeDaoImpl(Driver driver, RelationshipDao relationShipDaoImpl) {
        this.driver = driver;
        this.relationShipDaoImpl = relationShipDaoImpl;
    }

    /**
     * 当数据库中不存在同名节点时，插入节点
     * @param node
     * @return
     */
    @Override
    public Node insert(Node node){
        Session session = driver.session();
        String label = "";


        if(node instanceof Graph){
            label = "graph";
            if(findGraphById(node.getGraphId())){
                return null;
            }
        }

        else if(node instanceof Category){
            label = "category";
            if(!findGraphById(node.getGraphId())){
                return null;
            }
            if(findCategoryByName(node.getName(), node.getGraphId())){
                return null;
            }
        }
        else{
            label = "entity";
            Entity entity = (Entity)node;
            if(!findGraphById(entity.getGraphId())){
                return null;
            }
            // 前端会对类目节点存在性进行检查，若不存在调用insertCategory进行插入，后端不再需要插入操作
//            if(!findEntityByName("类目" + entity.getEntitiesPropertiesByName("category"), entity.getGraphId())){
//                insert(new Category("类目"+ entity.getEntitiesPropertiesByName("category"), entity.getGraphId()));
//                return null;
//            }
            if(findEntityByName(entity.getName(), entity.getGraphId())){
                return null;
            }
        }
        String insertQuery = "CREATE (a:" + label + ") SET " + node.getPropertiesAsString("a");
        try{
                session.writeTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx){
                    tx.run(insertQuery, parameters());
                    return null;
                }
            });
        }
        finally {
            session.close();
        }
        return node;
    }


    /**
     * 根据节点名删除节点及其对应关系
     * @param name
     * @param graphId
     */
    @Override
    public void deleteNodeByName(String name, String graphId){
        Session session = driver.session();
        String deleteRelQuery1 = "MATCH (a) - [r] -> (b) WHERE a.graphId = $graphId AND a.name = $name" +
                " " + "SET b.value = b.value - 1 DELETE r ";
        String deleteRelQuery2 = "MATCH (a) - [r] -> (b) WHERE b.graphId = $graphId AND b.name = $name" +
                " " + "SET a.value = a.value -1 DELETE r";
        String deleteNodeQuery = "MATCH (a) WHERE a.graphId = $graphId AND a.name = $name DELETE a";

        try{
            session.writeTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx){
                    tx.run(deleteRelQuery1, parameters("name", name, "graphId", graphId));
                    tx.run(deleteRelQuery2, parameters("name", name, "graphId", graphId));
                    tx.run(deleteNodeQuery, parameters("name", name, "graphId", graphId));
                    return null;
                }
            });
        }
        finally {
            session.close();
        }
    }


    /**
     *
     * @param newNode
     * @param oldName
     * @param graphId
     */
    @Override
    public void updateNode(Node newNode, String oldName, String graphId){
        Session session = driver.session();
        String updateQuery = "MATCH (a) WHERE a.graphId = $graphId AND a.name = $name SET"
                + " " + newNode.getPropertiesAsString("a");
        try{
            session.writeTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx){
                    tx.run(updateQuery, parameters("graphId", graphId, "name", oldName));
                    return null;
                }
            });
        }
        finally {
            session.close();
        }
    }

    /**
     *
     * @param name 节点名
     * @return 若节点存在，返回true，否则false
     */
    @Override
    public boolean findEntityByName(String name, String graphId){
        Session session = driver.session();
        String findQuery = "MATCH(a:entity) WHERE a.graphId = $graphId AND a.name = $name return a";
        try{
            return session.readTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx){
                    Result result = tx.run(findQuery, parameters("graphId", graphId, "name", name));
                    return result.hasNext();
                }
            });
        }
        finally {
            session.close();
        }
    }

    @Override
    public boolean findGraphByName(String name){
        Session session = driver.session();
        String findQuery = "MATCH(a:graph) WHERE a.name = $name  return a";
        try{
            return session.readTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx){
                    Result result = tx.run(findQuery, parameters( "name", name));
                    return result.hasNext();
                }
            });
        }
        finally {
            session.close();
        }
    }

    @Override
    public boolean findCategoryByName(String name, String graphId){
        Session session = driver.session();
        String findQuery = "MATCH(a:category) WHERE a.graphId = $graphId AND a.name = $name  return a";
        try{
            return session.readTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx){
                    Result result = tx.run(findQuery, parameters( "graphId", graphId, "name", name));
                    return result.hasNext();
                }
            });
        }
        finally {
            session.close();
        }
    }

    /**
     *
     * @param graphId
     * @return 若graphId对应的图谱存在，返回true， 否则false
     */
    @Override
    public boolean findGraphById(String graphId){
        Session session = driver.session();
        String findQuery = "MATCH(a:graph) WHERE a.graphId = $graphId return a";
        try{
            return session.readTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx){
                    Result result = tx.run(findQuery, parameters("graphId", graphId));
                    return result.hasNext();
                }
            });
        }
        finally {
            session.close();
        }
    }
    /**
     * 按名查找节点
     * @param name 节点名
     * @return 返回节点，若不存在，返回null
     */
    @Override
    public Node getByName(String name, String graphId) {
        Session session = driver.session();
        String getQuery = "MATCH(a:entity) WHERE a.graphId = $graphId AND a.name = $name RETURN a";

        try {
            return session.readTransaction(new TransactionWork<Node>() {
                @Override
                public Node execute(Transaction tx) {
                    Result result = tx.run(getQuery, parameters("graphId", graphId, "name", name));
                    if (result.hasNext()) {
                        org.neo4j.driver.types.Node node = result.next().get(0).asNode();
                        Map<String, Object> properties = node.asMap();
                        return new Entity(properties);
                    } else {
                        return null;
                    }
                }
            });
        }finally{
                session.close();
            }
    }

    /**
     * 获取所有节点和关系
     * @return 返回包含所有节点和关系的graph对象
     */
    @Override
    public Graph retrieveGraphById(String graphId){

        Graph graph = new Graph();

        graph.setEntities(retrieveAllEntitiesByGraph(graphId));
        graph.setLinks(relationShipDaoImpl.retrieveAllRelations(graphId));
        graph.setCategories(retrieveAllCategoriesByGraph(graphId));
        graph.setGraphId(graphId);
        Session session = driver.session();
        Map<String, Object> graphBasicInfo = new HashMap<>();
        graphBasicInfo.put("entitiesCnt", graph.getEntities().size());
        graphBasicInfo.put("LinksCnt", graph.getLinks().size());
        graphBasicInfo.put("categoriesCnt", graph.getCategories().size());
        String getGraphQuery = "MATCH(a:graph) WHERE a.graphId = $graphId  RETURN a";
        try{
            session.readTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx){
                    Result result = tx.run(getGraphQuery, parameters("graphId", graphId));
                    if(result.hasNext()){
                        org.neo4j.driver.types.Node node = result.next().get(0).asNode();
                        graphBasicInfo.putAll(node.asMap());
                    }
                    return null;
                }
            });
        }finally {
            graph.setGraphBasicInfo(graphBasicInfo);
            session.close();
        }
        return graph;
    }

    /**
     * 取出数据库中所有节点
     * @return 节点列表
     */
    @Override
    public List<Entity> retrieveAllEntitiesByGraph(String graphId){
        List<Entity> entities = new LinkedList<>();
        Session session = driver.session();
        String getQuery = "MATCH(a:entity) WHERE a.graphId = $graphId  RETURN a";
        try {
                session.readTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx) {
                    Result result = tx.run(getQuery, parameters("graphId", graphId));
                    while(result.hasNext()) {
                        org.neo4j.driver.types.Node node = result.next().get(0).asNode();
                        Map<String, Object> properties = node.asMap();
                        entities.add(new Entity(properties));
                    }
                    return null;
            }});
        }finally{
            session.close();
        }
        return entities;
    }

    @Override
    public List<Category> retrieveAllCategoriesByGraph(String graphId){
        List<Category> categories = new LinkedList<>();
        Session session = driver.session();
        String getQuery = "MATCH(a:category)  WHERE a.graphId = $graphId RETURN a";
        try {
            session.readTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx) {
                    Result result = tx.run(getQuery, parameters("graphId", graphId));
                    while(result.hasNext()) {
                        org.neo4j.driver.types.Node node = result.next().get(0).asNode();
                        Map<String, Object> properties = node.asMap();
                        categories.add(new Category(properties));
                    }
                    return null;
                }});
        }finally{
            session.close();
        }
        return categories;
    }




    @Override
    public List<Graph> retrieveAllGraphs() {
        List<Graph> graphList = new LinkedList<>();
        Session session = driver.session();
        String getQuery = "MATCH(a:graph) RETURN a";
        try {
            session.readTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx) {
                    Result result = tx.run(getQuery, parameters());
                    while(result.hasNext()) {
                        org.neo4j.driver.types.Node node = result.next().get(0).asNode();
                        Map<String, Object> properties = node.asMap();
                        graphList.add(new Graph(properties));
                    }
                    return null;
                }});
        }finally{
            session.close();
        }
        return graphList;
    }

    /**
     * 清空数据库
     */
    @Override
    public void deleteAll(){
        Session session = driver.session();
        session.run("MATCH(n) DETACH DELETE n");
        session.close();
    }

    /**
     * 删除图谱
     * @param graphId
     */
    @Override
    public void deleteGraphById(String graphId){
        Session session = driver.session();
        String deleteRelationQuery = "MATCH ()-[r]->() WHERE r.graphId = $graphId delete r";
        String deleteNodesQuery = "MATCH (a) WHERE a.graphId = $graphId delete a";
        try {
//            session.readTransaction(new TransactionWork<Void>() {
//                @Override
//                public Void execute(Transaction tx) {
//                    tx.run(deleteRelationQuery, parameters("graphId", graphId));
//                    tx.run(deleteNodesQuery, parameters("graphId", graphId));
//                    return null;
//                }});
            session.run(deleteRelationQuery, parameters("graphId", graphId));
            session.run(deleteNodesQuery, parameters("graphId", graphId));
        }finally{
            session.close();
        }
    }



}



