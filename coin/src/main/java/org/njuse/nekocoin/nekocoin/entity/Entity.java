package org.njuse.nekocoin.nekocoin.entity;


import org.njuse.nekocoin.nekocoin.Utils.EntityProperties;

import java.util.HashMap;
import java.util.Map;

import static org.njuse.nekocoin.nekocoin.Utils.StringTools.deleteEndingComma;
import static org.njuse.nekocoin.nekocoin.Utils.StringTools.transProperties2Str;

public class Entity extends Node {


//    private int category;
//    private double symbolSize;
//    private double x;
//    private double y;
//    private long value;
    private Map<String, Object> entityProperties;
    private Map<String, Object> contextProperties;
    private static String contextLabel = "entity";



    public Entity(){}
    public Entity(String name, int category){
        this.setEntitiesProperties("value", 1);
        this.setEntitiesProperties("category", category);
        this.setEntitiesProperties("symbol","circle");
        super.setName(name);
        this.setEntitiesProperties("symbolSize",
                10.0 + Double.parseDouble(String.valueOf(this.getEntitiesPropertiesByName("value"))) / 2 );

    }
//    public Entity(String name, double symbolSize, double x, double y, long value, int category){
//        super.setName(name);
//        this.setEntitiesProperties("symbolSize", symbolSize);
//        this.setEntitiesProperties("x", x);
//        this.setEntitiesProperties("y", y);
//        this.setEntitiesProperties("value", value);
//        this.setEntitiesProperties("category", category);
//        this.setEntitiesProperties("symbol","circle");
//    }
    public Entity(Map<String, Object> properties){
        this.entityProperties = new HashMap<>();
        this.contextProperties = new HashMap<>();
        for(Map.Entry<String, Object> entry: properties.entrySet()){
            if(EntityProperties.isNodeStyle(entry.getKey())){
                this.entityProperties.put(entry.getKey(), entry.getValue());
            }
            else if("name".equals(entry.getKey())){
                super.setName(entry.getValue().toString());
            }
            else if("graphId".equals(entry.getKey())){
                super.setGraphId(entry.getValue().toString());
            }
            else{
                this.contextProperties.put(entry.getKey(), entry.getValue());
            }
        }
    }
    @Override
    public String getPropertiesAsString(String variable){
        StringBuilder myProperties = new StringBuilder();
        Map<String, Object> viewProperties = new HashMap<>();
        viewProperties.put("name", this.getName());
        viewProperties.put("graphId", this.getGraphId());

        transProperties2Str(variable,myProperties,viewProperties);
        transProperties2Str(variable, myProperties, contextProperties);
        transProperties2Str(variable, myProperties, entityProperties);
        return deleteEndingComma(myProperties);
    }



    @Override
    public boolean equals(Object obj){
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(obj instanceof Entity) {
            Entity temp = (Entity) obj;
            return this.getName().equals(temp.getName()) && this.getGraphId().equals(temp.getGraphId());
        }
        return false;
    }
    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + (this.getName() == null ? 0 : this.getName().hashCode());
        result = 31 * result + (this.getGraphId() == null ? 0 : this.getGraphId().hashCode());
        return result;
    }



    public Object getEntitiesPropertiesByName(String name){
        return this.entityProperties.get(name);
    }
    public void setEntitiesProperties(String key, Object value){
        this.entityProperties.put(key, value);
    }
    public Map<String, Object> getentityProperties() {
        return entityProperties;
    }

    public void setentityProperties(Map<String, Object> entityProperties) {
        this.entityProperties = entityProperties;
    }

    public Map<String, Object> getContextProperties() {
        return contextProperties;
    }

    public void setContextProperties(Map<String, Object> contextProperties) {
        this.contextProperties = contextProperties;
    }
}
