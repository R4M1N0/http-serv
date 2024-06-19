package de.reqbal.httpserv;

import de.reqbal.httpserv.conn.ConnectionCommunicator;
import de.reqbal.httpserv.conn.ConnectionHandler;
import de.reqbal.httpserv.http.HttpConnectionHandler;
import de.reqbal.httpserv.http.HttpRequestParseGateway;
import de.reqbal.httpserv.http.HttpStaticResourceLoader;
import de.reqbal.httpserv.route.Route;
import de.reqbal.httpserv.route.RouteContainer;
import java.io.IOException;
import java.util.List;

public class HttpServer {

  private final ConnectionHandler connectionHandler;

  public HttpServer(int port, List<Route> routes) {
    var httpParseGateway = new HttpRequestParseGateway();
    var routeContainer = new RouteContainer().withRoutes(routes);
    var httpStaticResourceLoader =
        new HttpStaticResourceLoader("/home/ramine/code/personal/http-serv/http-serv/src/main/resources/www/");
    var httpConnectionHandler = new HttpConnectionHandler(httpParseGateway, routeContainer, httpStaticResourceLoader);
    var connectionCommunicator = new ConnectionCommunicator(httpConnectionHandler);
    this.connectionHandler = new ConnectionHandler(port, connectionCommunicator, 100);
  }

  public void run() {
    try {
      this.connectionHandler.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
