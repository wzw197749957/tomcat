package com.wzw.tomcat;

import com.wzw.tomcat.container.Context;
import com.wzw.tomcat.container.Host;
import com.wzw.tomcat.util.ProtocolHttpUtil;
import com.wzw.tomcat.util.StaticResourceUtil;

import java.io.*;

public class Response {
    private Host host;
    private Context context;
    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void write(String content) throws IOException {
        this.outputStream.write(content.getBytes());
    }

    public void outputHtml(String path) throws IOException {
        String absolutePath = StaticResourceUtil.getAbsolutePath(path);

        File file = new File(absolutePath);
        if (file.exists() && file.isFile()) {
            InputStream fis = new FileInputStream(file);
            StaticResourceUtil.outputStaticResource(fis, outputStream);
        } else {
            outputStream.write(ProtocolHttpUtil.http404header().getBytes());
        }

    }

    public void outputHtml1(String path) throws IOException {
        String absolutePath = host.getAppBase() + "/" + path;
        File file = new File(absolutePath);
        if (file.exists() && file.isFile()) {
            InputStream fis = new FileInputStream(file);
            StaticResourceUtil.outputStaticResource(fis, outputStream);
        } else {
            outputStream.write(ProtocolHttpUtil.http404header().getBytes());
        }
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

}
