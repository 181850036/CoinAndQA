package org.njuse.nekocoin.nekocoin.daoimpl;

import org.junit.jupiter.api.Assertions;
import org.njuse.nekocoin.nekocoin.dao.NodeDao;
import org.junit.jupiter.api.Test;
import org.njuse.nekocoin.nekocoin.Utils.TestCaseAutoGenerate;
import org.njuse.nekocoin.nekocoin.entity.Category;
import org.njuse.nekocoin.nekocoin.entity.Entity;
import org.njuse.nekocoin.nekocoin.entity.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
 class NodeDaoImplTest {
    @Autowired
    NodeDao nodeDao;
    @Test
    void testInsertGraph(){
        Graph testGraph = TestCaseAutoGenerate.generateGraph();
        // 插入一个graph，while循环保证数据库中不存在相同id的graph
        while(nodeDao.findGraphById(testGraph.getGraphId())){
            testGraph = TestCaseAutoGenerate.generateGraph();
        }
        // 测试findGraphById的查询未找到逻辑
        Assertions.assertFalse(nodeDao.findGraphById(testGraph.getGraphId()));
        nodeDao.insert(testGraph);
        // 若正确找到graph，说明插入成功
        Assertions.assertTrue(nodeDao.findGraphById(testGraph.getGraphId()));
        //测试重复插入是否能正确识别
        Assertions.assertEquals(null, nodeDao.insert(testGraph));
        // 测试删除图谱节点的逻辑
        nodeDao.deleteGraphById(testGraph.getGraphId());
        Assertions.assertFalse(nodeDao.findGraphById(testGraph.getGraphId()));
}
//    @Autowired
//    RelationshipDao relationshipDao;
//    @Autowired
//    NodeServiceImpl nodeServiceImpl;
//
//
//
    @Test
    void testInsertCategory() {
            Graph testGraph = TestCaseAutoGenerate.generateGraph();
            // 插入一个graph，while循环保证数据库中不存在相同id的graph
            while(nodeDao.findGraphById(testGraph.getGraphId())){
                testGraph = TestCaseAutoGenerate.generateGraph();
            }
            nodeDao.insert(testGraph);
            Category testCategory = TestCaseAutoGenerate.generateCategory(testGraph.getGraphId());
            nodeDao.insert(testCategory);
            Assertions.assertTrue(nodeDao.findCategoryByName(testCategory.getName(), testCategory.getGraphId()));
            nodeDao.deleteGraphById(testCategory.getGraphId());
            Assertions.assertFalse(nodeDao.findGraphById(testGraph.getGraphId()));
            Assertions.assertFalse(nodeDao.findCategoryByName(testCategory.getName(), testCategory.getGraphId()));

        }
        @Test
    void testInsertEntity(){
            Graph testGraph = TestCaseAutoGenerate.generateGraph();
            // 插入一个graph，while循环保证数据库中不存在相同id的graph
            while(nodeDao.findGraphById(testGraph.getGraphId())){
                testGraph = TestCaseAutoGenerate.generateGraph();
            }
            nodeDao.insert(testGraph);
            // 生成并插入entity
            Entity testEntity = TestCaseAutoGenerate.generateEntity(testGraph.getGraphId());
            nodeDao.insert(testEntity);
            // 判断节点是否插入成功
            Assertions.assertTrue(nodeDao.findEntityByName(testEntity.getName(), testEntity.getGraphId()));
            Assertions.assertEquals(testEntity, nodeDao.getByName(testEntity.getName(), testEntity.getGraphId()));
            // 测试删除节点逻辑
            nodeDao.deleteNodeByName(testEntity.getName(), testEntity.getGraphId());
            // 判断删除是否成功
            Assertions.assertFalse(nodeDao.findEntityByName(testEntity.getName(), testEntity.getGraphId()));
            nodeDao.deleteGraphById(testGraph.getGraphId());
            Assertions.assertFalse(nodeDao.findGraphById(testGraph.getGraphId()));
        }
        @Test
    void  testUpdateEntity(){
            Graph testGraph = TestCaseAutoGenerate.generateGraph();
            // 插入一个graph，while循环保证数据库中不存在相同id的graph
            while(nodeDao.findGraphById(testGraph.getGraphId())){
                testGraph = TestCaseAutoGenerate.generateGraph();
            }
            nodeDao.insert(testGraph);
            // 生成并插入entity
            Entity testEntity = TestCaseAutoGenerate.generateEntity(testGraph.getGraphId());
            nodeDao.insert(testEntity);
            // 判断节点是否插入成功
            Assertions.assertTrue(nodeDao.findEntityByName(testEntity.getName(), testEntity.getGraphId()));
            Assertions.assertEquals(testEntity, nodeDao.getByName(testEntity.getName(), testEntity.getGraphId()));
            String preName = testEntity.getName();
            Entity newEntity;
            nodeDao.updateNode(newEntity = TestCaseAutoGenerate.generateEntity(testGraph.getGraphId()), preName, testGraph.getGraphId());
            Assertions.assertEquals(newEntity, nodeDao.getByName(newEntity.getName(), newEntity.getGraphId()));
            Assertions.assertFalse(nodeDao.findEntityByName(testEntity.getName(), testEntity.getGraphId()));
            nodeDao.deleteGraphById(testGraph.getGraphId());
            Assertions.assertFalse(nodeDao.findGraphById(testGraph.getGraphId()));
        }
}


//
//    @Test
//    void testUpdateNode() {
//        try {
//            Entity testNode = new Entity("testNode",0.901, 1.23, 3.2, 0L,0);
//
//            nodeDao.insert(testNode);
//
//            Entity temp = nodeDao.getByName("testNode");
//            Assertions.assertEquals("testNode",temp.getName());
//            Assertions.assertEquals(0, temp.getCategory());
//
//            nodeDao.updateNode(new Entity("edoNtset", 32131232),"testNode");
//
//            temp = nodeDao.getByName("edoNtset");
//            Assertions.assertEquals("edoNtset",temp.getName());
//            Assertions.assertEquals(32131232, temp.getCategory());
//
//        } finally {
//            nodeDao.deleteNodeByName("edoNtset");
//            nodeDao.deleteNodeByName("testNode");
//        }
//    }
//
//    @Test
//    void testDeleteNodeByName() {
//        try {
//            Entity testNode = new Entity("testNode",0.901, 1.23, 3.2, 0L,0);
//
//            nodeDao.insert(testNode);
//
//            Assertions.assertEquals(true,nodeDao.findEntityByName("testNode"));
//
//            nodeDao.deleteNodeByName("testNode");
//
//            Assertions.assertEquals(false,nodeDao.findEntityByName("testNode"));
//        } finally {
//            nodeDao.deleteNodeByName("testNode");
//        }
//
//    }
//
//
//
//
//


//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme