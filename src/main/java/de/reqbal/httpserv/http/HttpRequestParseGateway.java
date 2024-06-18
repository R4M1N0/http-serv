package de.reqbal.httpserv.http;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequestParseGateway {

  private final Http10RequestParser http10RequestParser;

  public HttpRequestParseGateway() {
    this.http10RequestParser = new Http10RequestParser();
  }

  public HttpRequest parse(BufferedReader in) throws IOException {
    String requestLine = in.readLine();
    var version = HttpVersionResolver.resolve(requestLine);

    switch (version) {
      case ONE_ZERO -> {
        return http10RequestParser.parse(requestLine, in);
      }
      case ONE_ONE -> {
        //TODO: implement
        return null;
      }
      case TWO_ZERO -> {
        //TODO: implement
        return null;
      }
    }
    return null;
  }
}
