package com.wzw.tomcat.listener;


import com.wzw.tomcat.Request;
import com.wzw.tomcat.Response;
import com.wzw.tomcat.connector.Connector;
import com.wzw.tomcat.container.Context;
import com.wzw.tomcat.container.Engine;
import com.wzw.tomcat.container.Host;
import com.wzw.tomcat.container.Wrapper;

import java.io.IOException;

public class CoyotoConnectorListener implements ConnectorListener {
    private Connector connector;

    public CoyotoConnectorListener() {
    }

    @Override
    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    @Override
    public Connector getConnector() {
        return this.connector;
    }

    @Override
    public void service(Request request, Response response) throws Exception {
        String[] split = request.getUrl().split("/");
        //查找Context
        Engine[] engines = this.connector.getService().getEngines();
        boolean isInHost = false;
        for (Engine engine : engines) {
            Host[] hosts = engine.getHosts();
            for (Host host : hosts) {
                if (!host.getName().equalsIgnoreCase(request.getHost())) {
                    continue;
                }
                Context[] contextes = host.getContextes();
                for (Context context : contextes) {
                    String[] split1 = request.getUrl().split("/");
                    if (context.getContextName().equalsIgnoreCase(split1[1])) {
                        //查找上下文
                        request.setContext(context);
                        response.setContext(context);
                        isInHost = true;
                        response.setHost(host);
                        break;
                    }
                }

                if (isInHost) {
                    break;
                }
            }
            if (isInHost) {
                break;
            }
        }

        // 查找servlet并且执行。
        if (request.getContext() != null) {
            String contextName = request.getContext().getContextName();
            String urlPattern = request.getUrl().replaceFirst("/" + contextName, "");
            Wrapper wrapper = request.getContext().getWrapper(urlPattern);
            if (wrapper == null) {
                try {
                    response.outputHtml1(request.getUrl());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                wrapper.getServlet().service(request, response);
            }
        }
    }
}
