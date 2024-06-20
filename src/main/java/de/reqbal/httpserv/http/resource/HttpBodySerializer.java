package de.reqbal.httpserv.http.resource;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HttpBodySerializer {

  public byte[] serialize(Object body) {

    if (body instanceof String) {
      return string((String) body);
    }

    if (body instanceof byte[]) {
      return (byte[]) body;
    }

    //Unbox list
    if (body instanceof List<?>) {
      return serializeList((List<?>) body);
    }

    return null;
  }

  private byte[] serializeList(List<?> body) {
    try (var is = new SequenceInputStream(
        Collections.enumeration(
            body.stream().map(this::serialize).map(ByteArrayInputStream::new).collect(Collectors.toList())
        )
    )) {
      return is.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static byte[] string(String body) {
    return body.getBytes(StandardCharsets.UTF_8);
  }
}
