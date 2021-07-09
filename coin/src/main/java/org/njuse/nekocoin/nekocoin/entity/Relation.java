package org.njuse.nekocoin.nekocoin.entity;


import java.util.Map;

public class Relation {


    private String graphId;

    private String source;

    private String target;

    private String value;

    private Map<String, Object> styles;


    public Relation(){}
    public Relation(String source, String target, String value){
        this.source = source;
        this.target = target;
        this.value = value;
    }
    public Relation(String source, String target, String value, String graphId){
        this.graphId = graphId;
        this.source = source;
        this.target = target;
        this.value = value;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getValue() {
        if(this.value == null) {
            return "null";
        }
        else {
            return this.value;
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }


    public Map<String, Object> getStyles() {
        return styles;
    }

    public void setStyles(Map<String, Object> styles) {
        this.styles = styles;
    }
}
