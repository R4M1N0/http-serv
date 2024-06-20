package de.reqbal.service;

import java.util.ArrayList;
import java.util.List;

public class HelloService {

  private final List<String> messages;

  public HelloService() {
    this.messages = new ArrayList<>();
  }

  public List<String> getMessages() {
    return this.messages;
  }

  public void addMessage(String message) {
    this.messages.add(message);
  }
}
