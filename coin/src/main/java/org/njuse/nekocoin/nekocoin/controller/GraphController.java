package org.njuse.nekocoin.nekocoin.controller;

import org.njuse.nekocoin.nekocoin.entity.Graph;
import org.njuse.nekocoin.nekocoin.service.GraphService;

import org.njuse.nekocoin.nekocoin.service.NodeService;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.njuse.nekocoin.nekocoin.Utils.Response;

import java.util.List;

@RestController()
@RequestMapping("/graph")

public class GraphController {
    private final NodeService nodeService;
    private final GraphService graphService;

    @Autowired
    public GraphController(NodeService nodeService, GraphService graphService) {
        this.nodeService = nodeService;
        this.graphService = graphService;
    }

    /**
     *
     * @param graph 一个符合Graph格式的json
     * @return
     */
    @PostMapping("/uploadGraphFile")
    @ResponseBody
    public Response importFile(@RequestBody Graph graph){
        if(graph.getCategories().size() <= 0){
            return Response.buildFailure("类目列表不能为空！");
        }
//        graph.setGraphId(String.valueOf(graph.getGraphId().hashCode()));
        return Response.buildSuccess(graphService.importGraphFromFile(graph));

}

    @GetMapping("/getAllNodesAndRelations")
    public Response getAllNodesAndRelations(@RequestParam(value = "graphId") String graphId){
        return Response.buildSuccess(nodeService.retrieveGraphById(graphId));
    }
    @PostMapping("/deleteGraph")
    public Response deleteGraphById(@RequestParam(value = "graphId") String graphId){
        graphService.deleteGraphById(graphId);
        return Response.buildSuccess(true);
    }
    @GetMapping("/getGraphList")
    public Response getGraphList(){
        return  Response.buildSuccess(graphService.getGraphList());
    }
}
