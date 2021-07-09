package org.njuse.nekocoin.nekocoin.dao;



import org.njuse.nekocoin.nekocoin.entity.Category;
import org.njuse.nekocoin.nekocoin.entity.Entity;
import org.njuse.nekocoin.nekocoin.entity.Graph;
import org.njuse.nekocoin.nekocoin.entity.Node;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeDao {
    /**
     * 持久化一个普通节点
     * @param node 待持久化的节点对象
     * @return 若数据库中存在同名
     */
    public Node insert(Node node);
    public void deleteNodeByName(String name, String graphId);
    public void updateNode(Node newNode, String oldName, String graphId);
    public boolean findEntityByName(String name, String graphId);
    public boolean findGraphByName(String name);
    public boolean findCategoryByName(String name, String graphId);
    public boolean findGraphById(String graphId);
    public Node getByName(String name, String graphId);
    public Graph retrieveGraphById(String graphId);
    public List<Entity> retrieveAllEntitiesByGraph(String graphId);
    public List<Category> retrieveAllCategoriesByGraph(String graphId);
    public List<Graph> retrieveAllGraphs();
    public void deleteAll();
    public void deleteGraphById(String graphId);
}
