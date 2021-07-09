package org.njuse.nekocoin.nekocoin.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

/**
 * 节点抽象类，提取了Graph,Category,Entity三种节点类型的共同属性和方法
 * @Author 蒋祚竑
 * @Date 2021/3/30
 */
@JsonDeserialize
public abstract class Node {

//    private String label;
    private String name;
    private String graphId;

//    public String getLabel() {
//        return label;
//    }

//    public void setLabel(String label) {
//        this.label = label;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String  getPropertiesAsString(String variable);


    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }
}
