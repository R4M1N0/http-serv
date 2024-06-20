package de.reqbal.httpserv.http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.Buffer;
import org.junit.jupiter.api.Test;

class Http10RequestParserTest {

  private final Http10RequestParser cut;

  Http10RequestParserTest() {
    this.cut = new Http10RequestParser();
  }

  @Test
  void parse() throws IOException {
    var stringReader = new StringReader("Content-Length: 1\n\n" + "aaaaa");
    BufferedReader bufReader = new BufferedReader(stringReader);
    var result = cut.parse("GET /test HTTP/1.0", bufReader);
  }
}