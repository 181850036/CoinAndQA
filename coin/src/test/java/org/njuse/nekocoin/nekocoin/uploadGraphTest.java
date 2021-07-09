package org.njuse.nekocoin.nekocoin;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.njuse.nekocoin.nekocoin.Utils.TestCaseAutoGenerate;
import org.njuse.nekocoin.nekocoin.dao.NodeDao;
import org.njuse.nekocoin.nekocoin.dao.RelationshipDao;
import org.njuse.nekocoin.nekocoin.entity.Category;
import org.njuse.nekocoin.nekocoin.entity.Graph;


import org.njuse.nekocoin.nekocoin.entity.Entity;
import org.njuse.nekocoin.nekocoin.entity.Relation;
import org.njuse.nekocoin.nekocoin.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;
//
@SpringBootTest

public class uploadGraphTest {

    @Autowired
    GraphService graphService;
    @Autowired
    NodeDao nodeDao;
    @Autowired
    RelationshipDao relationshipDao;
    @Test
    public void testUploadGraph(){
        Graph graph = TestCaseAutoGenerate.generateTestGraphWithEntities();
        Graph temp = new Graph();
        temp.setGraphId(graph.getGraphId());
        temp.setName(graph.getName());
        nodeDao.insert(temp);
        graphService.importGraphFromFile(graph);
        Graph getRes = nodeDao.retrieveGraphById(graph.getGraphId());
        Assertions.assertEquals(100, getRes.getEntities().size());
        Assertions.assertEquals(10, getRes.getCategories().size());
        Assertions.assertEquals(500, getRes.getLinks().size());
        nodeDao.deleteGraphById(getRes.getGraphId());
    }
//    @Test
//    public void aTEST(){
//        nodeController.deleteAllNodes();
//        assertEquals(0,nodeDao.getAll().getNodes().size());
//    }
//    @Test
//    public void bTEST(){
//        Response res = nodeController.getByName("不可能存在的节点名字");
//        assertEquals("未找到节点!",res.getMessage());
//
//    }
//    @Test
//    public void cTEST(){
//        String name  = "test";
//        Response se = new Response();
//        se = nodeController.addNode(name);
//        Graph g = nodeDao.getAll();
//        assertEquals(1,g.getNodes().size());
//        assertEquals(true,);
//    }
//    @Test void dTEST(){
//        String name  = "test";
//        Response re = new Response();
//        re = nodeController.addNode(name);
//        Graph g = nodeDao.getAll();
//        assertEquals(false,re.getSuccess());
//    }
//    @Test void eTEST(){
//        String name  = "test";
//        nodeController.deleteNode(name);
//        assertEquals(0,nodeDao.getAll().getNodes().size());
//    }
//    @Test void fTEST(){
//        String name  = "old";
//        nodeController.addNode(name);
//        Response re = nodeController.renameNode("old","new");
//        assertEquals(true,re.getSuccess());
//    }
}
