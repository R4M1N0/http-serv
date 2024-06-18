package de.reqbal.httpserv.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

public class HttpConnectionHandler {

  public static final String FAKE_BODY = """
      <html>
      Welcome to the <img src="/logo.gif"> example.re homepage!
      </html>
      """;

  private final HttpRequestParseGateway httpRequestParseGateway;
  private final HttpResponseSerializer httpResponseSerializer;

  public HttpConnectionHandler(HttpRequestParseGateway httpRequestParseGateway) {
    this.httpRequestParseGateway = httpRequestParseGateway;
    this.httpResponseSerializer = new HttpResponseSerializer();
  }

  public void serve(BufferedReader in, PrintWriter out) throws IOException, InterruptedException {

    //Parse
    var httpRequest = httpRequestParseGateway.parse(in);

    System.out.println(httpRequest);

    //Match Method & Route

    //Fetch static content / computed content from route

    Thread.sleep(1000);

    //Build Response
    var response = new HttpResponse(HttpVersion.ONE_ZERO, HttpCode.OK, Instant.now(), "server", FAKE_BODY);
    var protocolResponse = httpResponseSerializer.serialize(response);
    out.write(protocolResponse);
    out.flush();
  }
}
