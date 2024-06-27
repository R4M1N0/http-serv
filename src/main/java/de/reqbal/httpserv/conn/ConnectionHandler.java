package de.reqbal.httpserv.conn;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConnectionHandler {

  private final Integer port;
  private final ConnectionCommunicator connectionCommunicator;

  private List<Socket> connections;
  private final ExecutorService serverTcpExecutor;

  public ConnectionHandler(Integer port, ConnectionCommunicator connectionCommunicator) {
    this.connectionCommunicator = connectionCommunicator;
    this.port = port;
    this.connections = Collections.synchronizedList(new ArrayList<>());
    this.serverTcpExecutor = Executors.newSingleThreadExecutor();
  }

  public void start() throws IOException {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      try (serverTcpExecutor) {
        serverTcpExecutor
            .execute(() -> {
          acceptConnections(serverSocket);
        });
      }
    }
  }

  public void stop() throws InterruptedException {
    serverTcpExecutor.awaitTermination(2, TimeUnit.SECONDS);
  }

  private void acceptConnections(ServerSocket serverSocket) {
    try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
      try {
        while (true) {
          Socket clientSocket = serverSocket.accept();
          executorService.execute(() -> handleClientSocket(clientSocket));
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }

  private void handleClientSocket(Socket clientSocket) {
    connections.add(clientSocket);
    System.out.println("Active socket count: " + connections.size());
    connectionCommunicator.serve(clientSocket);
    connections.remove(clientSocket);
    try {
      clientSocket.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
