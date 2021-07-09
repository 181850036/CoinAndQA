package org.njuse.nekocoin.nekocoin.entity;



import java.util.HashMap;
import java.util.Map;
import static org.njuse.nekocoin.nekocoin.Utils.StringTools.deleteEndingComma;
import static org.njuse.nekocoin.nekocoin.Utils.StringTools.transProperties2Str;

public class Category extends Node{

    private Map<String, Object> categoryProperties;


    public Category(){
//        this.setLabel("category");
        this.setName("未命名");
    }
    public Category(Map<String, Object> properties){
        this.categoryProperties = new HashMap<>();
        for(Map.Entry<String, Object> entry: properties.entrySet()){
            if("name".equals(entry.getKey())){
                super.setName(entry.getValue().toString());
            }
            else if("graphId".equals(entry.getKey())){
                super.setGraphId(entry.getValue().toString());
            }
            else{
                this.categoryProperties.put(entry.getKey(), entry.getValue());
                }
            }
        }

    public Category(String name, String graphId){
        this.categoryProperties = new HashMap<>();
//        this.setLabel("category");
        this.setName(name);
        this.setGraphId(graphId);
    }
    @Override
    public String getPropertiesAsString(String variable) {
        StringBuilder myProperties = new StringBuilder();
        Map<String, Object> viewProperties = new HashMap<>();
        viewProperties.put("name", this.getName());
        viewProperties.put("graphId", this.getGraphId());
        transProperties2Str(variable,myProperties, viewProperties);
        transProperties2Str(variable,myProperties, categoryProperties);
        return deleteEndingComma(myProperties);
    }


    public Map<String, Object> getCategoryProperties() {
        return categoryProperties;
    }

    public void setCategoryProperties(Map<String, Object> categoryProperties) {
        this.categoryProperties = categoryProperties;
    }


}
