package org.njuse.nekocoin.nekocoin.Utils;

import java.util.Map;

public class StringTools {
    public static void transProperties2Str(String variable, StringBuilder propertiesStr, Map<String, Object> properties) {
        if(properties == null){
            return;
        }
        for(Map.Entry<String, Object> entry: properties.entrySet()){
            propertiesStr.append(variable).append(".").append(entry.getKey()).append("=");
            if(entry.getValue() instanceof Number){
                propertiesStr.append(entry.getValue());
            }
            else{
                propertiesStr.append("\"").append(entry.getValue()).append("\"");
            }
            propertiesStr.append(",");
        }
    }
    public static String deleteEndingComma(StringBuilder propertiesStr){
        final char comma = ',';
        if(propertiesStr.charAt(propertiesStr.length() - 1) == comma){
            propertiesStr.deleteCharAt(propertiesStr.length() - 1);
        }
        return propertiesStr.toString();
    }
}
