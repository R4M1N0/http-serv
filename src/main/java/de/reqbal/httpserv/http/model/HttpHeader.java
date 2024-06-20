package de.reqbal.httpserv.http.model;

import java.util.List;

public record HttpHeader(String key, String value) {

  public static final String CONTENT_LENGTH = "Content-Length";

  public static String getContentLength(List<HttpHeader> headers) {
    return headers.stream().filter(header -> header.key.equals(CONTENT_LENGTH)).map(HttpHeader::value).findFirst()
        .orElse(null);
  }
}
