package org.njuse.nekocoin.nekocoin.Utils;

public class Response {

    /**
     * 调用是否成功
     */
    private Boolean success;

    /**
     * 返回的提示信息
     */
    private String message;

    /**
     * 内容
     */
    private Object content;

    public static Response buildSuccess(){
        Response response=new Response();
        response.setSuccess(true);
        return response;
    }

    public static Response buildSuccess(Object content){
        Response response=new Response();
        response.setContent(content);
        response.setSuccess(true);
        return response;
    }

    public static Response buildFailure(String message){
        Response response=new Response();
        response.setSuccess(false);
        response.setMessage(message);
        System.out.println(message);
        return response;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
