package de.reqbal.httpserv.http.resource;

import java.nio.charset.StandardCharsets;

public class HttpBodySerializer {

  public byte[] serialize(Object body) {

    if (body instanceof String) {
      return string((String) body);
    }

    if (body instanceof byte[]) {
      return (byte[]) body;
    }

    return null;
  }

  private static byte[] string(String body) {
    return body.getBytes(StandardCharsets.UTF_8);
  }
}
