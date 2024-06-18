package de.reqbal.httpserv.http;

import java.io.BufferedReader;

public class Http10RequestParser {

  public HttpRequest parse(String requestLine, BufferedReader remaining) {
    //TODO: Implement
    return new HttpRequest(HttpMethod.GET, "test");
  }
}
