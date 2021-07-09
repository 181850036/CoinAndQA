package org.njuse.nekocoin.nekocoin.Utils;



public enum EntityProperties {
    SHAPE("symbol"),
    COLOR("color"),
    X("x"),
    Y("y"),
    SYMBOLSIZE("symbolSize"),
    CATEGORY("category"),
    VALUE("value");
    private String typeName;
    EntityProperties(String typeName){
        this.typeName = typeName;
    }
    public static boolean isNodeStyle(String str){
        for(EntityProperties entityViewProperties : EntityProperties.values()){
            if(entityViewProperties.getTypeName().equals(str)){
                return true;
            }
        }
        return false;
    }

    public String getTypeName() {
        return typeName;
    }
}

