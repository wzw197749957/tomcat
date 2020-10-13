package com.wzw.tomcat.util;

/**
 * HTTP协议工具类
 */
public class ProtocolHttpUtil {

    /**
     * 写出状态码200的响应头
     * @param contentLen 内容长度
     * @return
     */
    public static String http200header(long contentLen){
        return "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html;charset=utf-8\n"+
                "Content-Length: "+contentLen + "\n"+
                "\r\n";
    }

    public static String http404header(){
        String page404 = "<h1>404,Page Not Found.</h1>";
        return "HTTP/1.1 404 NOT Found\n" +
                "Content-Type: text/html;charset=utf-8\n"+
                "Content-Length: " + page404.getBytes().length+"\n"+
                "\r\n"+
                page404;
    }

}
