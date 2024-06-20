package de.reqbal.httpserv.http.request;

import static de.reqbal.httpserv.http.response.HttpResponseSerializer.SINGLE_SPACE;

import de.reqbal.httpserv.http.model.HttpHeader;
import de.reqbal.httpserv.http.model.HttpMethod;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Http10RequestParser {

  public HttpRequest parse(String requestLine, BufferedReader remaining) throws IOException {
    MethodPath result = getMethodPath(requestLine);
    List<HttpHeader> headers = getHttpHeaders(remaining);
    var contentLength = Optional.ofNullable(HttpHeader.getContentLength(headers)).map(Integer::parseInt).orElse(null);
    String body = getBody(remaining, contentLength);
    return new HttpRequest(result.method(), result.uri(), headers, body);
  }

  private static String getBody(BufferedReader remaining, Integer contentLength) throws IOException {

    if (contentLength == null) {
      return null;
    }

    StringBuilder body = new StringBuilder();
    while (remaining.ready()) {
      char[] cBody = new char[contentLength];
      //will only read until cBody is full
      remaining.read(cBody);
      //TODO: How to handle timeout
      body.append(cBody);
    }
    return body.toString();
  }

  private static List<HttpHeader> getHttpHeaders(BufferedReader remaining) throws IOException {
    List<HttpHeader> headers = new ArrayList<>();
    while (remaining.ready()) {
      var nextLine = remaining.readLine();
      if (nextLine.isBlank()) {
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
