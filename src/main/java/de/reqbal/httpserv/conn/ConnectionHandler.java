package de.reqbal.httpserv.conn;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionHandler {

  private final Integer port;
  private final ConnectionCommunicator connectionCommunicator;
  private final Integer maxConnection;

  private List<Socket> connections;
  private boolean running;

  public ConnectionHandler(Integer port, ConnectionCommunicator connectionCommunicator, Integer maxConnection) {
    this.connectionCommunicator = connectionCommunicator;
    this.port = port;
    this.maxConnection = maxConnection;
    this.connections = Collections.synchronizedList(new ArrayList<>());
    this.running = true;
  }

  public void start() throws IOException {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
        executorService.execute(() -> {
          acceptConnections(serverSocket);
        });
      }
    }
  }

  private void acceptConnections(ServerSocket serverSocket) {
    try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
      while (running) {
        try {
          Socket clientSocket = serverSocket.accept();
          executorService.execute(() -> handleClientSocket(clientSocket));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
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
