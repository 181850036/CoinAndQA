package org.njuse.nekocoin.nekocoin.service;

import org.njuse.nekocoin.nekocoin.entity.Graph;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GraphService {
    public Graph importGraphFromFile(Graph graph);
    public List<Graph> getGraphList();
    public void deleteGraphById(String graphId);
}
