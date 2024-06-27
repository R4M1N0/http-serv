package de.reqbal.httpserv.http;

import de.reqbal.httpserv.context.annotation.WebInfrastructure;
import de.reqbal.httpserv.http.model.HttpVersion;
import java.util.Arrays;

@WebInfrastructure
public class HttpVersionResolver {

  public static HttpVersion resolve(String requestLine) {
    String[] split = requestLine.split(HttpParsingConstants.SINGLE_SPACE);
    var list = Arrays.stream(split).toList();
    try {
      var version = list.get(2);
      return HttpVersion.get(version);
    } catch (IndexOutOfBoundsException ex) {
      return HttpVersion.ONE_ZERO;
    }
  }
}
