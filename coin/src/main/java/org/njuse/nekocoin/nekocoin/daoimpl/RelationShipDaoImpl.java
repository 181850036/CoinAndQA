package org.njuse.nekocoin.nekocoin.daoimpl;


import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.njuse.nekocoin.nekocoin.Utils.DbExceptionMessage;
import org.njuse.nekocoin.nekocoin.dao.RelationshipDao;

import org.njuse.nekocoin.nekocoin.entity.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.LinkedList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;
@Repository
public class RelationShipDaoImpl implements RelationshipDao {
    private final Driver driver;
    private static final String findQuery = "MATCH (a:entity)-[r]"
            + "->(b:entity) WHERE a.name = $aname AND a.graphId = $graphId AND b.name = $bname AND r.value = $value RETURN a,b,r";
    @Autowired
    public RelationShipDaoImpl(Driver driver) {
        this.driver = driver;
    }

    /**
     * 用于导入预定义json的关系，无需使节点value自增
     * @param source
     * @param target
     * @param value
     * @param graphId
     */
    @Override
    public void buildRelation(String source, String target, String value, String graphId){
        Session session = driver.session();

        String buildQuery = "MATCH (a), (b) WHERE a.graphId = $graphId AND" +
                " a.name = $aname AND b.graphId = $graphId AND b.name = $bname" +
                " CREATE (a) - [r:rel] -> (b) SET r.value = $value, r.graphId = $graphId";


                try {

                    session.writeTransaction(new TransactionWork<Void>() {
                        @Override

                        public Void execute(Transaction tx) {
                            if (tx.run(findQuery, parameters("aname", source, "bname"
                                    , target, "graphId", graphId, "value", value)).hasNext()) {
                                return null;
                            }
                            tx.run(buildQuery, parameters("aname", source, "bname", target, "value", value, "graphId", graphId));
                            return null;
                        }
                    });
                }
                finally {
                    session.close();
                }



    }
    /**
     * 用于
     * @param source 起始节点名
     * @param target 目标节点名
     * @param value 关系名
     * @param graphId
     */
    @Override
    public void buildRelationIncValue(String source, String target, String value, String graphId){
        Session session = driver.session();

        String buildQuery = "MATCH(a:entity), (b:entity) WHERE a.name = $aname AND b.name = $bname CREATE (a)-[r:rel" +
                            "]->(b) SET a.value = a.value + 1, b.value = b.value + 1, r.value = $value, r.graphId = $graphId" +
                " RETURN r";
        boolean res = false;
        try{

                res = session.writeTransaction(new TransactionWork<Boolean>() {
                    @Override
                    public Boolean execute(Transaction tx){
                        if(tx.run(findQuery, parameters("aname", source, "bname"
                                , target, "graphId", graphId, "value", value)).hasNext()){
                            return false;
                        }
                        Result result = tx.run(buildQuery, parameters("aname", source, "bname", target, "value", value, "graphId", graphId));
                        return result.hasNext();
                    }
                });

        }
        finally {
//            if(!res){
//                throw new DbExceptionMessage("build Relation failure!");
//            }
            session.close();
        }

    }
    /**
     * 根据关系名删除关系
     * @param value 关系名
     * @param graphId
     */
    @Override
    public void deleteRelationByValue(String value, String graphId){
        Session session = driver.session();
        String deleteQuery = "MATCH (a) - [r] -> (b) WHERE r.graphId = $graphId AND r.value = $value SET a.value = a.value - 1, b.value = b.value - 1 DELETE r";
        try{
            session.writeTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx){
                    tx.run(deleteQuery, parameters("graphId", graphId, "value", value));
                    return null;
                }
            });
        }
        finally {
            session.close();
        }
    }

    /**
     * @param source
     * @param target
     * @param graphId
     */
    @Override
    public void deleteRelationByNodes(String source, String target, String graphId){
        Session session = driver.session();
        String deleteQuery = "MATCH (a) - [r] -> (b) WHERE a.name = $sourceName AND a.graphId = $graphId AND b.name = $targetName  SET a.value = a.value - 1, b.value = b.value - 1 DELETE r";
        try{
            session.writeTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx){
                    tx.run(deleteQuery, parameters("sourceName", source,"graphId", graphId, "targetName", target));
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
     * @param newRelation
     * @param source
     * @param target
     * @param graphId
     * @return
     */
    @Override
    public boolean updateRelation(Relation newRelation, String source, String target, String graphId){
        Session session = driver.session();
        String deleteQuery = "MATCH (a) - [r] -> (b) WHERE a.name = $source AND b.name = $target AND a.graphId = $graphId" +
                " DELETE r SET a.value = a.value - 1, b.value = b.value - 1";

        String createQuery = "MATCH (a), (b) WHERE a.name = $newSource AND b.name = $newTarget AND a.graphId = $graphId AND b.graphId = $graphId " +
                "CREATE (a) - [r:rel] -> (b) SET a.value = a.value + 1, b.value = b.value + 1, r.value = $value, r.graphId = $graphId return r";
        boolean res = false;
        try{
            res = session.writeTransaction(new TransactionWork<Boolean>() {
            @Override
            public Boolean execute(Transaction tx){
                tx.run(deleteQuery, parameters("source", source, "target", target, "graphId", graphId));
                Result result = tx.run(createQuery, parameters("newSource", newRelation.getSource(),
                        "newTarget", newRelation.getTarget(), "graphId", graphId, "value", newRelation.getValue()));
                return result.hasNext();
            }
            });
        }finally {
            session.close();
        }
        return res;
    }

    /**
     *
     * @param source 起始节点名
     * @param target 目标节点名
     * @param value 关系名
     * @param graphId
     * @return 若存在 (source) - [relationName] -> (target)的关系，返回true,否则false
     */
    @Override
    public boolean findRelation(String source, String target, String value, String graphId){
        Session session = driver.session();
        String findQuery = "MATCH (a:entity)-[r]"
                        + "->(b:entity) WHERE a.name = $aname AND a.graphId = $graphId AND b.name = $bname AND r.value = $value RETURN a,b,r";
        boolean res = false;
        try{
            res = session.readTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx){
                    Result result = tx.run(findQuery, parameters("aname", source,
                            "bname", target, "graphId", graphId, "value", value));
                    return result.hasNext();
                }
            });
        }
        finally {
            session.close();
        }
        return res;

    }

    /**
     * 查找并获取关系
     * @param source
     * @param target
     * @param relationName
     * @param graphId
     * @return 若关系存在，返回Relation，否则返回null
     */
    @Override
    public Relation getRelation(String source, String target, String relationName, String graphId){
        Session session = driver.session();
        String getQuery = "MATCH(a:entity)-[r"
                        + "]->(b:entity) WHERE a.graphId = $graphId AND" +
                "  a.name = $aname AND b.name = $bname AND r.value = $value RETURN a.name, b.name, r.value, r.graphId";
        Relation relation = new Relation();
        try{
            return session.readTransaction(new TransactionWork<Relation>() {
                @Override
                public Relation execute(Transaction tx){
                    Result res = tx.run(getQuery, parameters("graphId", graphId, "aname", source, "bname", target
                    , "value", relationName));
                    Record record = res.next();
                    return new Relation(record.get(0).asString(), record.get(1).asString()
                            , record.get(2).asString(), record.get(3).asString());
                }
            });
        }
        finally {
            session.close();
        }
    }



    /**
     * 获取所有关系
     * @return
     * @param graphId
     */
    @Override
    public List<Relation> retrieveAllRelations(String graphId){
        List<Relation> relations = new LinkedList<>();
        Session session = driver.session();
        String retrieveQuery = "MATCH (a) - [r] -> (b) WHERE a.graphId = $graphId return  a.name, b.name, r.value, a.graphId";
        try{
            relations = session.readTransaction(new TransactionWork<List<Relation>>() {
                @Override
                public List<Relation> execute(Transaction tx) {
                    List<Relation> res = new LinkedList<>();
                    Result result = tx.run(retrieveQuery, parameters("graphId", graphId));
                    List<Record> records = result.list();
                    for(Record record: records){
                        res.add(new Relation(record.get(0).asString(), record.get(1).asString(),
                                record.get(2).asString(), record.get(3).asString()));
                    }
                    return res;
                }
            });
        }
        finally {
            session.close();
        }
        return relations;
    }
}
