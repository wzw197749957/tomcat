package com.wzw.tomcat;

import com.wzw.tomcat.connector.Connector;
import com.wzw.tomcat.connector.Endpoint;
import com.wzw.tomcat.connector.ProtocolHandler;
import com.wzw.tomcat.connector.impl.BioEndpoint;
import com.wzw.tomcat.connector.impl.Http11ProtocolHandler;
import com.wzw.tomcat.container.*;
import com.wzw.tomcat.container.impl.*;
import com.wzw.tomcat.listener.ConnectorListener;
import com.wzw.tomcat.listener.CoyotoConnectorListener;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Minicat的主类
 */
public class Bootstrap {

    private int port = 8080;

    private Map<String, HttpServlet> servletMap = new HashMap<String, HttpServlet>();

    private Server server;

    public static void main(String[] args) throws IOException {
        new Bootstrap().start();
    }

    /**
     * V4.0
     */
    public void start() {
        initialize();
        startServer();
    }

    private void initialize() {
        //加载配置文件server.xml以及初始化Context容器和Wapper容器
        try {
            load();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        server.init();
    }

    /**
     * V4.0,初始化
     */
    private void startServer() {
        this.server.start();
    }

    private void load() throws FileNotFoundException {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document server = saxReader.read(resourceAsStream);
            Element rootElement = server.getRootElement();
            this.server = createServer();

            //初始化service
            List<Element> servicesList = rootElement.selectNodes("//Service");
            Service[] services = new Service[servicesList.size()];
            for (int i = 0; i < servicesList.size(); i++) {
                services[i] = createService(this.server, servicesList.get(i));
            }
            this.server.setServices(services);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建Service节点
     *
     * @param server
     * @param element
     * @return
     */
    private Service createService(Server server, Element element) throws FileNotFoundException {
        List<Element> listConnector = element.selectNodes("//Connector");
        List<Element> listEngine = element.selectNodes("//Engine");
        Service service = new ServiceImpl();
        for (int i = 0; i < listEngine.size(); i++) {
            Engine engine = createEngine(service, listEngine.get(i));
            service.setEngine(engine);
        }
        service.setServer(server);

        for (int i = 0; i < listConnector.size(); i++) {
            Element elementConnector = listConnector.get(i);
            String port = elementConnector.attributeValue("port");
            ConnectorListener connectorListener = new CoyotoConnectorListener();
            Endpoint endpoint = new BioEndpoint(connectorListener);
            ProtocolHandler protocolHandler = new Http11ProtocolHandler(endpoint);
            Connector connector = new Connector(protocolHandler);
            connectorListener.setConnector(connector);
            protocolHandler.setConnector(connector);
            endpoint.setProtocolHandler(protocolHandler);
            connector.setPort(Integer.valueOf(port));
            connector.setServer(server);
            connector.setService(service);

            server.setConnector(connector);
        }
        return service;
    }

    /**
     * 创建引擎
     *
     * @param element
     * @return
     */
    private Engine createEngine(Service service, Element element) throws FileNotFoundException {
        List<Element> listHost = element.selectNodes("//Host");
        Engine engine = new EngineImpl();
        Host[] hosts = new Host[listHost.size()];
        for (int i = 0; i < listHost.size(); i++) {
            hosts[i] = createHost(engine, listHost.get(i));
        }
        engine.setHosts(hosts);
        return engine;
    }

    /**
     * 创建Host容器
     *
     * @param engine
     * @param element
     * @return
     */
    private Host createHost(Engine engine, Element element) throws FileNotFoundException {
        Host host = new HostImpl();
        String name = element.attributeValue("name");
        String appBase = element.attributeValue("appBase");
        host.setName(name);
        host.setAppBase(appBase);
        host.setEngine(engine);

        File baseFile = new File(host.getAppBase());
        File[] files = baseFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            File web = files[i];
            if (web.isDirectory()) {
                // 初始化Context
                Context context = createContext(web);
                host.setContext(context);
            }
        }
        return host;
    }

    /**
     * 创建上下文容器
     *
     * @param web
     * @return
     */
    private Context createContext(File web) throws FileNotFoundException {
        Context context = new ContextImpl();
        String name = web.getName();
        context.setContextName(name);
        File classPath = new File(web, "/WEB-INF/classes");
        URL[] urls = new URL[1];
        try {
            urls[0] = classPath.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        context.setClassloader(new WebappsClassloader(urls));
        // 加载web.xml,并解析出Wapper
        File webXml = new File(web, "/WEB-INF/web.xml");
        if (webXml.exists()) {
            createWapper(context, webXml);
        }
        return context;
    }

    private void createWapper(Context context, File webXml) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(webXml);
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");

            for (Element element : selectNodes) {
                Element servletNameEle = (Element) element.selectSingleNode("//servlet-name");
                String servletName = servletNameEle.getStringValue();
                Element servletClsEle = (Element) element.selectSingleNode("//servlet-class");
                String servletClass = servletClsEle.getStringValue();

                //根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();

                Wrapper wapper = new WrapperImpl();
                wapper.setServletName(servletName);
                wapper.setClassName(servletClass);
                wapper.setUrlPattern(urlPattern);
                wapper.setContext(context);

                context.setWrapper(wapper);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建server
     *
     * @return
     */
    private Server createServer() {
        return new ServerImpl();
    }

}
