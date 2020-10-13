package com.wzw.tomcat;

import com.wzw.tomcat.container.Context;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    private String url;  //请求路径
    private String method; //请求方法GET,POST
    private String host; //请求的主机名称
    private String contextName; //请求上下文名称
    private String port;

    private Context context;

    private InputStream inputStream;  // 输入流，其他属性从输入流中解析出来

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    // 构造器，输入流传入
    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        int count = 0;
        while (count == 0) {
            count = this.inputStream.available();
        }

        byte[] bytes = new byte[count];
        inputStream.read(bytes);

        String instr = new String(bytes);
        String[] split = instr.split("\n");
        String firstLine = split[0];
        String[] s = firstLine.split(" ");
        String secLine = split[1];
        String[] s1 = secLine.split(" ");
        this.method = s[0];
        this.url = s[1];
        String[] split1 = s1[1].split(":");
        this.host = split1[0];
    }
}
