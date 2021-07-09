package org.njuse.nekocoin.nekocoin.serviceimpl;

import org.njuse.nekocoin.nekocoin.Utils.DbExceptionMessage;
import org.njuse.nekocoin.nekocoin.dao.NodeDao;
import org.njuse.nekocoin.nekocoin.dao.RelationshipDao;
import org.njuse.nekocoin.nekocoin.entity.Category;
import org.njuse.nekocoin.nekocoin.entity.Graph;
import org.njuse.nekocoin.nekocoin.entity.Entity;
import org.njuse.nekocoin.nekocoin.entity.Relation;
import org.njuse.nekocoin.nekocoin.service.GraphService;
//import org.njuse.nekocoin.nekocoin.service.NodeService;
//import org.njuse.nekocoin.nekocoin.utils.Neo4jCQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphServiceImpl implements GraphService {

    private final NodeDao nodeDao;
    private final RelationshipDao relationshipDao;

    @Autowired
    public GraphServiceImpl(NodeDao nodeDao, RelationshipDao relationshipDao) {
        this.nodeDao = nodeDao;
        this.relationshipDao = relationshipDao;
    }


    /**
     * 从json文件导入预定义的知识图谱
     *
     * @param graph 前端传入的json文件转化后的graph对象
     * @return
     */
    @Override
    public Graph importGraphFromFile(Graph graph) {
        List<Entity> entities = graph.getEntities();
        List<Relation> relations = graph.getLinks();
        List<Category> categories = graph.getCategories();
        for (Category category : categories) {
            nodeDao.insert(category);
        }
        for (Entity entity : entities) {
            nodeDao.insert(entity);
        }
        for (Relation relation : relations) {
            relationshipDao.buildRelation(relation.getSource(), relation.getTarget(), relation.getValue(), relation.getGraphId());
        }
        Map<String, Object> graphBasicInfo = new HashMap<>();
        graphBasicInfo.put("entitiesCnt", graph.getEntities().size());
        graphBasicInfo.put("linksCnt", graph.getLinks().size());
        graphBasicInfo.put("categoriesCnt", graph.getCategories().size());
        graph.setGraphBasicInfo(graphBasicInfo);
        return graph;
    }

    @Override
    public List<Graph> getGraphList(){
        List<Graph> graphList = new ArrayList<>();
        graphList = nodeDao.retrieveAllGraphs();
        return graphList;
    }

    @Override
    public void deleteGraphById(String graphId){
        nodeDao.deleteGraphById(graphId);
    }
}

//    public Graph importGraphFromFile(MultipartFile multipartFile){
//        Graph graph = new Graph();
//        try{
//            InputStream inputStream = multipartFile.getInputStream();
//
//            InputStreamReader inputStreamReader;
//            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String str = null;
//
//            while((str = bufferedReader.readLine()) != null){
//                String[] Triple = str.split(",");
//
//                nodeDao.insert();
//
//                nodeDao.insert(Triple[1]);
//
//                relationshipDao.buildRelation(Triple[0],Triple[1],Triple[2]);
//
//            }
//
//            graph = nodeDao.getAll();
//            bufferedReader.close();
//            inputStreamReader.close();
//            inputStream.close();
//        }
//        catch (IndexOutOfBoundsException e){
//            e.printStackTrace();
//        }
//        catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//        return graph;
//
//    }


