package de.reqbal.httpserv.http;

import java.util.Arrays;

public class HttpVersionResolver {

  public static final String SINGLE_SPACE = " ";

  public static HttpVersion resolve(String requestLine) {
    String[] split = requestLine.split(SINGLE_SPACE);
    var list = Arrays.stream(split).toList();
    try {
      var version = list.get(2);
      return HttpVersion.get(version);
    } catch (IndexOutOfBoundsException ex) {
      return HttpVersion.ONE_ZERO;
    }
  }
}
