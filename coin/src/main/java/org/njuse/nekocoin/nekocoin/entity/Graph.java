package org.njuse.nekocoin.nekocoin.entity;


import org.njuse.nekocoin.nekocoin.Utils.EntityProperties;

import java.util.*;

import static org.njuse.nekocoin.nekocoin.Utils.StringTools.deleteEndingComma;
import static org.njuse.nekocoin.nekocoin.Utils.StringTools.transProperties2Str;

public class Graph extends Node{




    private List<Entity> entities;
    private List<Relation> links;
    private List<Category> categories;

    private Map<String, Object> graphProperties;


    public Graph(){
        this.graphProperties = new HashMap<>();
        this.entities = new LinkedList<>();
        this.links = new LinkedList<>();
        this.categories = new LinkedList<>();
//        this.setLabel("graph");
    }

    public Graph(Map<String, Object> properties){
        this.graphProperties = new HashMap<>();
        this.entities = new LinkedList<>();
        this.links = new LinkedList<>();
        this.categories = new LinkedList<>();
        for(Map.Entry<String, Object> entry: properties.entrySet()){
            if("name".equals(entry.getKey())){
                super.setName(entry.getValue().toString());
            }
            else if("graphId".equals(entry.getKey())){
                super.setGraphId(entry.getValue().toString());
            }
            else{
                graphProperties.put(entry.getKey(), entry.getValue());
            }
        }
    }
    @Override
    public String getPropertiesAsString(String variable) {
        StringBuilder myProperties = new StringBuilder();
        Map<String, Object> viewProperties = new HashMap<>();
        viewProperties.put("name", this.getName());
        viewProperties.put("graphId", this.getGraphId());
        transProperties2Str(variable,myProperties, viewProperties);
        transProperties2Str(variable,myProperties, graphProperties);
        return deleteEndingComma(myProperties);
    }
    public void setGraphBasicInfo(Map<String, Object> graphBasicInfo){
        for(Map.Entry<String, Object> entry: graphBasicInfo.entrySet()){

            if("name".equals(entry.getKey())){
                super.setName(entry.getValue().toString());
            }
            else if("graphId".equals(entry.getKey())){
                super.setGraphId(entry.getValue().toString());
            }
            else{
                this.graphProperties.put(entry.getKey(), entry.getValue());
            }
        }
    }
    /**
     * 向graph中插入节点，节点不允许为null
     * @param node
     */
    public boolean addEntities(Entity node){
        return this.entities.add(node);
    }
    public boolean addRelations(Relation relation){
        return this.links.add(relation);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> nodes) {
        this.entities = nodes;
    }

    public List<Relation> getLinks() {
        return links;
    }

    public void setLinks(List<Relation> links) {
        this.links = links;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }


    public Map<String, Object> getGraphProperties() {
        return graphProperties;
    }

    public void setGraphProperties(Map<String, Object> graphProperties) {
        this.graphProperties = graphProperties;
    }

}
