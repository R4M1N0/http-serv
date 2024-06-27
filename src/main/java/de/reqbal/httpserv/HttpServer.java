package de.reqbal.httpserv;

import de.reqbal.httpserv.conn.ConnectionCommunicator;
import de.reqbal.httpserv.conn.ConnectionHandler;
import de.reqbal.httpserv.http.HttpConnectionHandler;
import de.reqbal.httpserv.http.HttpStaticResourceLoader;
import de.reqbal.httpserv.http.request.HttpRequestParseGateway;
import de.reqbal.httpserv.route.Route;
import de.reqbal.httpserv.route.RouteContainer;
import java.io.IOException;
import java.util.List;

public class HttpServer {

  private final ConnectionHandler connectionHandler;

  private HttpServer(int port, String baseScanPackage) {

    boolean autodiscoverRoutes = false;
    if (null != baseScanPackage && !baseScanPackage.isBlank()) {
      autodiscoverRoutes = true;
    }
    var routeContainer = new RouteContainer(autodiscoverRoutes, baseScanPackage);
    var httpStaticResourceLoader =
        new HttpStaticResourceLoader("/home/ramine/code/personal/http-serv/http-serv/src/main/resources/www/");
    var httpConnectionHandler = new HttpConnectionHandler(httpParseGateway, routeContainer, httpStaticResourceLoader);
    var connectionCommunicator = new ConnectionCommunicator(httpConnectionHandler);
    this.connectionHandler = new ConnectionHandler(port, connectionCommunicator);
  }

  public static HttpServerBuilder builder() {
    return new HttpServerBuilder();
  }

  public static class HttpServerBuilder {

    private int port;
    private String baseScanPackage;

    private HttpServerBuilder() {}

    public HttpServer build() {
      return new HttpServer(port, baseScanPackage);
    }

    public HttpServerBuilder withPort(int port) {
      this.port = port;
      return this;
    }

    public HttpServerBuilder discoverRoutes(String baseScanPackage) {
      this.baseScanPackage = baseScanPackage;
      return this;
    }

  }

  public void run() {
    try {
      this.connectionHandler.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
