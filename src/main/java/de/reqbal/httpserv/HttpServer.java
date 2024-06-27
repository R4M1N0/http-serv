package de.reqbal.httpserv;

import de.reqbal.httpserv.conn.ConnectionHandler;
import de.reqbal.httpserv.context.annotation.Inject;
import de.reqbal.httpserv.context.annotation.WebInfrastructure;
import java.io.IOException;

@WebInfrastructure
public class HttpServer {

  private final ConnectionHandler connectionHandler;

  @Inject
  public HttpServer(ConnectionHandler connectionHandler) {
    this.connectionHandler = connectionHandler;
  }

  public static HttpServerBuilder builder() {
    return new HttpServerBuilder();
  }

  public static class HttpServerBuilder {

    private int port;
    private String baseScanPackage;

    private HttpServerBuilder() {}

    /*public HttpServer build() {
      return new HttpServer(port, baseScanPackage);
    }*/

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
