package org.njuse.nekocoin.nekocoin.Utils;

public enum GraphProperties {
    CREATETIME("createTime");
    private String typeName;
    GraphProperties(String typeName){
        this.typeName = typeName;
    }
    public static boolean is(String str){

        for(GraphProperties graphProperties : GraphProperties.values()){
            if(graphProperties.getTypeName().equals(str)){
                return true;
            }
        }
        return false;
    }

    public String getTypeName() {
        return typeName;
    }
}
