package org.njuse.nekocoin.nekocoin.service;

import org.njuse.nekocoin.nekocoin.Utils.DbExceptionMessage;
import org.njuse.nekocoin.nekocoin.entity.Category;
import org.njuse.nekocoin.nekocoin.entity.Entity;
import org.njuse.nekocoin.nekocoin.entity.Graph;
import org.njuse.nekocoin.nekocoin.entity.Node;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NodeService {
    public Node insert(Node node) throws DbExceptionMessage;
    public void deleteNodeByName(String name, String graphId);
    public void updateNode(Node newNode, String oldName, String graphId);
    public boolean findByName(String name, String graphId);
    public Node getByName(String name, String graphId);
    public List<Entity> retrieveAllEntitiesByGraph(String graphId);
    public List<Category> retrieveAllCategories(String graphId);
    public void deleteAll();
    public Graph retrieveGraphById(String graphId);

}
