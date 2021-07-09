package org.njuse.nekocoin.nekocoin.controller;

import org.njuse.nekocoin.nekocoin.Utils.DbExceptionMessage;
import org.njuse.nekocoin.nekocoin.Utils.MyTime;
import org.njuse.nekocoin.nekocoin.entity.Category;
import org.njuse.nekocoin.nekocoin.entity.Entity;
import org.njuse.nekocoin.nekocoin.entity.Graph;
import org.njuse.nekocoin.nekocoin.entity.Node;
import org.njuse.nekocoin.nekocoin.service.NodeService;
import org.njuse.nekocoin.nekocoin.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/node")

public class NodeController {

    private final NodeService nodeService;

    @Autowired
    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @PostMapping("/insertEntity")
    public Response insertEntity(@RequestBody Entity node){
        try {
            Node res = nodeService.insert(node);
            return Response.buildSuccess(res);
        }
        catch (DbExceptionMessage e){
            return Response.buildFailure(e.getMessage());
        }
    }
    @PostMapping("/insertGraph")
    public Response insertGraph(@RequestBody Graph node){
        try {
            node.getGraphProperties().put("createTime", MyTime.getSystemTime());
//            node.setGraphId(String.valueOf(node.getGraphId().hashCode()));
            Node res = nodeService.insert(node);
            return Response.buildSuccess(res);
        }
        catch (DbExceptionMessage e){
            return Response.buildFailure(e.getMessage());
        }
    }
    @PostMapping("/insertCategory")
    public Response insertCategory(@RequestBody Category node){
        try {
            Node res = nodeService.insert(node);
            return Response.buildSuccess(res);
        }
        catch (DbExceptionMessage e){
            return Response.buildFailure(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public Response deleteNode(@RequestParam(value = "name") String name, @RequestParam(value = "graphId") String graphId){
        nodeService.deleteNodeByName(name, graphId);
        return Response.buildSuccess(true);
    }

    @PostMapping("/deleteAll")
    public Response deleteAllNodes() {
        nodeService.deleteAll();
        return Response.buildSuccess(true);
    }

    @PostMapping("/editNodeInfo")
    public Response updateNode(@RequestBody Map<String, Object> editNodeInfoParams){
        Map<String, Object> myMap = (LinkedHashMap)editNodeInfoParams.get("entity");
        Map<String, Object> properties = new LinkedHashMap<>();
        if(myMap.get("contextProperties") != null){
            properties.putAll((LinkedHashMap)myMap.get("contextProperties"));
        }
        properties.putAll((LinkedHashMap)myMap.get("entityProperties"));
        properties.put("graphId", myMap.get("graphId"));
        properties.put("name",myMap.get("name"));
        Entity entity = new Entity(properties);
        nodeService.updateNode(entity, (String)editNodeInfoParams.get("name"), entity.getGraphId());

        return Response.buildSuccess(true);
    }

    @PostMapping("/editNodes")
    public Response updateNodes(@RequestBody List<Entity> nodes){
        for(Entity entity: nodes){
            nodeService.updateNode(entity, entity.getName(),entity.getGraphId());
        }
        return Response.buildSuccess(true);
    }

    @GetMapping("/getByName")
    public Response getByName(@RequestParam(value = "name") String name, @RequestParam(value = "graphId") String graphId){
        Node node = nodeService.getByName(name, graphId);
        if(node != null) {
            return Response.buildSuccess(node);
        }
        else {
            return Response.buildFailure("node not found");
        }
    }



}
