package de.reqbal.httpserv.http.response;

import de.reqbal.httpserv.http.model.HttpHeader;
import de.reqbal.httpserv.http.resource.HttpResource;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HttpResponseHeaderProvider {

  private final String serverName;

  public HttpResponseHeaderProvider() {
    this.serverName = "http-serv";
  }


  public List<HttpHeader> getDefaultHeaders() {
    return new ArrayList<>(List.of(
        new HttpHeader("Server", "http-serv"),
        new HttpHeader("Date", DateTimeFormatter.RFC_1123_DATE_TIME.format(OffsetDateTime.now()))
    ));
  }

  public Optional<HttpHeader> getMimeHeader(HttpResource result) {
    String value = result.mimeType();
    if (value == null) {
      return Optional.empty();
    }
    return Optional.of(new HttpHeader("Content-Type", value));
  }
}
