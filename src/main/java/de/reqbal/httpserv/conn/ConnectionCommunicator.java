package de.reqbal.httpserv.conn;

import de.reqbal.httpserv.context.annotation.Inject;
import de.reqbal.httpserv.context.annotation.WebInfrastructure;
import de.reqbal.httpserv.http.HttpConnectionHandler;
import de.reqbal.httpserv.http.exception.HttpTimeoutException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@WebInfrastructure
public class ConnectionCommunicator {

  private final HttpConnectionHandler httpConnectionHandler;

  @Inject
  public ConnectionCommunicator(HttpConnectionHandler httpConnectionHandler) {
    this.httpConnectionHandler = httpConnectionHandler;
  }

  public void serve(Socket socket) {
    try {
      PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      try {
        httpConnectionHandler.serve(in, out);
      } catch (HttpTimeoutException ex) {
        System.err.println(ex.getMessage());
      } finally {
        out.close();
        in.close();
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
