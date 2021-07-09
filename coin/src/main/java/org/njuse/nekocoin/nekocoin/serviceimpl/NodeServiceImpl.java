package org.njuse.nekocoin.nekocoin.serviceimpl;

import org.njuse.nekocoin.nekocoin.dao.NodeDao;

import org.njuse.nekocoin.nekocoin.entity.Category;
import org.njuse.nekocoin.nekocoin.entity.Entity;
import org.njuse.nekocoin.nekocoin.entity.Graph;
import org.njuse.nekocoin.nekocoin.entity.Node;
import org.njuse.nekocoin.nekocoin.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class NodeServiceImpl implements NodeService {
    private final NodeDao nodeDao;

    @Autowired
    public NodeServiceImpl(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }

    @Override
    public Node insert(Node node) { return nodeDao.insert(node);}
    @Override
    public void deleteNodeByName(String name, String graphId){
        nodeDao.deleteNodeByName(name, graphId);
    }
    @Override
    public void updateNode(Node newNode, String oldName, String graphId){  nodeDao.updateNode(newNode, oldName, graphId); }
    @Override
    public boolean findByName(String name, String graphId){
        return nodeDao.findEntityByName(name, graphId);
    }
    @Override
    public Node getByName(String name, String graphId){
        return nodeDao.getByName(name, graphId);
    }
    @Override
    public List<Entity> retrieveAllEntitiesByGraph(String graphId){return nodeDao.retrieveAllEntitiesByGraph(graphId);}
    @Override
    public List<Category> retrieveAllCategories(String graphId) {return nodeDao.retrieveAllCategoriesByGraph(graphId);};

    @Override
    public void deleteAll(){
        nodeDao.deleteAll();
    }
    @Override
    public Graph retrieveGraphById(String graphId){return nodeDao.retrieveGraphById(graphId);}
}
