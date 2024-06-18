package de.reqbal.httpserv;

import de.reqbal.httpserv.conn.ConnectionCommunicator;
import de.reqbal.httpserv.conn.ConnectionHandler;
import de.reqbal.httpserv.http.HttpConnectionHandler;
import de.reqbal.httpserv.http.HttpRequestParseGateway;
import java.io.IOException;

public class HttpServer {

  private final ConnectionHandler connectionHandler;

  public HttpServer(int port) {
    var httpParseGateway = new HttpRequestParseGateway();
    var httpConnectionHandler = new HttpConnectionHandler(httpParseGateway);
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
