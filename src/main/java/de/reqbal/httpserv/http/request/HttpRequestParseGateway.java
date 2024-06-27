package de.reqbal.httpserv.http.request;

import de.reqbal.httpserv.context.annotation.Inject;
import de.reqbal.httpserv.context.annotation.WebInfrastructure;
import de.reqbal.httpserv.http.HttpVersionResolver;
import java.io.BufferedReader;
import java.io.IOException;

@WebInfrastructure
public class HttpRequestParseGateway {

  private final Http10RequestParser http10RequestParser;

  @Inject
  public HttpRequestParseGateway(Http10RequestParser http10RequestParser) {
    this.http10RequestParser = http10RequestParser;
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
        return http10RequestParser.parse(requestLine, in);
      }
      case TWO_ZERO -> {
        //TODO: implement
        return null;
      }
    }
    return null;
  }
}
