package org.njuse.nekocoin.nekocoin.controller;


import org.njuse.nekocoin.nekocoin.Utils.DbExceptionMessage;
import org.njuse.nekocoin.nekocoin.entity.Relation;
import org.njuse.nekocoin.nekocoin.service.NodeService;
import org.njuse.nekocoin.nekocoin.service.RelationshipService;
import org.njuse.nekocoin.nekocoin.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/relationship")
public class RelationshipController {


    private final RelationshipService relationshipService;
    private static String targetStr = "target";
    private static String sourceStr = "source";
    private static String valueStr = "value";
    private static String graphIdStr = "graphId";
    @Autowired
    public RelationshipController(NodeService nodeService, RelationshipService relationshipService) {

        this.relationshipService = relationshipService;
    }

    @PostMapping("/buildRelation")
    public Response buildRelation(@RequestBody Map<String, Object> buildRelationParams){
        try{relationshipService.buildRelationIncValue((String)buildRelationParams.get(sourceStr),
                (String)buildRelationParams.get(targetStr), (String)buildRelationParams.get(valueStr), (String)buildRelationParams.get(graphIdStr));}
        catch (DbExceptionMessage e){
            return Response.buildFailure(e.getMessage());
        }
        return  Response.buildSuccess(true);
    }
    @PostMapping("/deleteByValue")
    public Response deleteByValue(@RequestParam(value = "value") String type, @RequestParam(value = "graphId") String graphId){
        relationshipService.deleteRelationByValue(type, graphId);
        return Response.buildSuccess(true);
    }

    @PostMapping("/deleteByName")
    public Response deleteByNodes(@RequestParam(value = "sourceName") String source, @RequestParam(value = "targetName") String target,
                                  @RequestParam(value = "graphId") String graphId){
        relationshipService.deleteRelationByNodes(source, target, graphId);
        return Response.buildSuccess(true);
    }

    @PostMapping("/editRelationInfo")
    public Response updateRelation(@RequestBody Map<String, Object> data){
        Map<String, Object> myMap = (LinkedHashMap)data.get("newVal");
        String graphId = data.get(graphIdStr).toString();
        Relation relation = new Relation((String)myMap.get(sourceStr),
                (String)myMap.get(targetStr),
                (String)myMap.get(valueStr));

        return Response.buildSuccess( relationshipService.updateRelation(relation,
                (String)data.get(sourceStr), (String)data.get(targetStr), graphId));
    }


}
