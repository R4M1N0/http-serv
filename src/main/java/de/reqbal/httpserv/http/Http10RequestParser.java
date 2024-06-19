package de.reqbal.httpserv.http;

import static de.reqbal.httpserv.http.HttpResponseSerializer.SINGLE_SPACE;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Http10RequestParser {

  public HttpRequest parse(String requestLine, BufferedReader remaining) throws IOException {
    MethodPath result = getMethodPath(requestLine);
    List<HttpHeader> headers = getHttpHeaders(remaining);
    String body = getBody(remaining);
    return new HttpRequest(result.method(), result.uri(), headers, body);
  }

  private static String getBody(BufferedReader remaining) throws IOException {
    StringBuilder body = new StringBuilder();
    while (remaining.ready()) {
      var nextLine = remaining.readLine();
      body.append(nextLine);
    }
    return body.toString();
  }

  private static List<HttpHeader> getHttpHeaders(BufferedReader remaining) throws IOException {
    List<HttpHeader> headers = new ArrayList<>();
    while (remaining.ready()) {
      var nextLine = remaining.readLine();
      if (nextLine.equals(HttpParsingConstants.CRLF)) {
        break;
      }
      var headerParts = nextLine.split(":");
      if (headerParts.length == 2) {
        var header = new HttpHeader(headerParts[0].trim(), headerParts[1].trim());
        headers.add(header);
      }
    }
    return headers;
  }

  private static MethodPath getMethodPath(String requestLine) {
    String[] split = requestLine.split(SINGLE_SPACE);
    HttpMethod method = null;
    String uri = null;
    var list = Arrays.stream(split).toList();
    try {
      method = HttpMethod.valueOf(list.get(0));
      uri = list.get(1);
    } catch (IndexOutOfBoundsException ex) {
      //do nothjng
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException("METHOD not found");
  }

    if (method == null) {
      throw new RuntimeException("METHOD not found");
    }

    if (uri == null) {
      throw new RuntimeException("URI not found");
    }
    MethodPath result = new MethodPath(method, uri);
    return result;
  }

  private record MethodPath(HttpMethod method, String uri) {
  }
}
