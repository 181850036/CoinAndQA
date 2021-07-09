package org.njuse.nekocoin.nekocoin.daoimpl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.njuse.nekocoin.nekocoin.Utils.TestCaseAutoGenerate;
import org.njuse.nekocoin.nekocoin.dao.NodeDao;
import org.njuse.nekocoin.nekocoin.dao.RelationshipDao;
import org.njuse.nekocoin.nekocoin.entity.Category;
import org.njuse.nekocoin.nekocoin.entity.Entity;
import org.njuse.nekocoin.nekocoin.entity.Graph;
import org.njuse.nekocoin.nekocoin.entity.Relation;
import org.njuse.nekocoin.nekocoin.serviceimpl.RelationshipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RelationshipDaoImplTest {
    @Autowired
    RelationshipDao relationshipDao;
    @Autowired
    NodeDao nodeDao;
    @InjectMocks
    RelationshipServiceImpl relationshipServiceImpl;



    @Test
    void testRelation() {
        Graph testGraph = TestCaseAutoGenerate.generateGraph();
        // 插入一个graph，while循环保证数据库中不存在相同id的graph
        while(nodeDao.findGraphById(testGraph.getGraphId())){
            testGraph = TestCaseAutoGenerate.generateGraph();
        }
        nodeDao.insert(testGraph);
        Entity entity1 = TestCaseAutoGenerate.generateEntity(testGraph.getGraphId());
        Entity entity2 = TestCaseAutoGenerate.generateEntity(testGraph.getGraphId());
        nodeDao.insert(entity1);
        nodeDao.insert(entity2);
        // 测试查找不存在的关系的逻辑
        Assertions.assertFalse(relationshipDao.findRelation(entity1.getName(),
                entity2.getName(),
                TestCaseAutoGenerate.faker.artist().toString(),
                testGraph.getGraphId()));
        String testRelName = "11";
        Relation relation = new Relation(entity1.getName(), entity2.getName(), testRelName, testGraph.getGraphId());
        relationshipDao.buildRelation(entity1.getName(), entity2.getName(), testRelName, testGraph.getGraphId());
        Assertions.assertTrue(relationshipDao.findRelation(entity1.getName(), entity2.getName(), testRelName, testGraph.getGraphId()));
        Relation getResult = relationshipDao.getRelation(entity1.getName(), entity2.getName(), testRelName, testGraph.getGraphId());
        Assertions.assertEquals(relation.getValue(), getResult.getValue());
        Assertions.assertEquals(relation.getSource(), getResult.getSource());
        Assertions.assertEquals(relation.getTarget(), getResult.getTarget());

        String testNewRelName = "22";
        Relation newRel = new Relation(entity2.getName(), entity1.getName(), testNewRelName, testGraph.getGraphId());
        // 测试关系更新逻辑
        relationshipDao.updateRelation(newRel, entity1.getName(), entity2.getName(), testGraph.getGraphId());
        Assertions.assertFalse(relationshipDao.findRelation(entity1.getName(), entity2.getName(), testRelName, testGraph.getGraphId()));
        Assertions.assertTrue(relationshipDao.findRelation(entity2.getName(), entity1.getName(), testNewRelName, testGraph.getGraphId()));
        Relation newGetResult = relationshipDao.getRelation(entity2.getName(), entity1.getName(), testNewRelName, testGraph.getGraphId());
        Assertions.assertEquals(newRel.getValue(), newGetResult.getValue());
        Assertions.assertEquals(newRel.getSource(), newGetResult.getSource());
        Assertions.assertEquals(newRel.getTarget(), newGetResult.getTarget());
        nodeDao.deleteGraphById(testGraph.getGraphId());
    }
}

