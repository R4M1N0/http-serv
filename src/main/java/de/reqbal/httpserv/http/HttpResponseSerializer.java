package de.reqbal.httpserv.http;

public class HttpResponseSerializer {

  public static final String SINGLE_SPACE = " ";
  public static final String NEW_LINE = "\n";

  public String serialize(HttpResponse httpResponse) {
    String response = "";
    response += HttpVersion.from(httpResponse.version()) + SINGLE_SPACE + httpResponse.code().getCode() + SINGLE_SPACE + httpResponse.code().getIdentifier();
    response += NEW_LINE;
    response += "Date:" + SINGLE_SPACE + httpResponse.date().toString();
    response += NEW_LINE;
    response += "Server:" + SINGLE_SPACE + httpResponse.server();
    response += NEW_LINE;
    response += "Content-Type:" + SINGLE_SPACE + "text/html";
    response += NEW_LINE;
    response += NEW_LINE;
    response += httpResponse.body();
    return response;
  }
}
