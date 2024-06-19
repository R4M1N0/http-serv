package de.reqbal.httpserv.http;

import de.reqbal.httpserv.http.resource.HttpBodySerializer;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public class HttpResponseSerializer {

  public static final String SINGLE_SPACE = " ";
  public static final String NEW_LINE = "\n";

  private final HttpBodySerializer httpBodySerializer;

  public HttpResponseSerializer() {
    this.httpBodySerializer = new HttpBodySerializer();
  }

  public String serialize(HttpResponse httpResponse) {
    String response = "";
    response += HttpVersion.from(httpResponse.version()) + SINGLE_SPACE + httpResponse.code().getCode() + SINGLE_SPACE + httpResponse.code().getIdentifier();
    response += NEW_LINE;
    response = appendHeaders(httpResponse, response);
    response += NEW_LINE;
    response += NEW_LINE;
    response = appendBody(httpResponse, response);
    return response;
  }

  private String appendBody(HttpResponse httpResponse, String response) {
    Object body = httpResponse.body();
    if (null != body) {
      response += new String(httpBodySerializer.serialize(body), StandardCharsets.UTF_8);
      response += NEW_LINE;
    }
    return response;
  }

  private static String appendHeaders(HttpResponse httpResponse, String response) {
    response += "Date:" + SINGLE_SPACE + DateTimeFormatter.RFC_1123_DATE_TIME.format(httpResponse.date());
    response += NEW_LINE;
    response += "Server:" + SINGLE_SPACE + httpResponse.server();
    response += NEW_LINE;
    response += "Content-Type:" + SINGLE_SPACE + "text/html";
    return response;
  }
}
