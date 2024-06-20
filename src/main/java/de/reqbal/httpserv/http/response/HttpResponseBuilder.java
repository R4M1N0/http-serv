package de.reqbal.httpserv.http.response;

import de.reqbal.httpserv.http.model.HttpCode;
import de.reqbal.httpserv.http.model.HttpHeader;
import de.reqbal.httpserv.http.model.HttpVersion;
import de.reqbal.httpserv.http.resource.HttpResource;
import java.util.ArrayList;
import java.util.List;

public class HttpResponseBuilder {

  private HttpCode status;
  private HttpVersion version;
  private List<HttpHeader> headers;
  private HttpResource body;

  private HttpResponseBuilder() {

  }

  public static HttpResponseBuilder builder() {
    return new HttpResponseBuilder().version(HttpVersion.ONE_ZERO);
  }

  public HttpResponseBuilder status(HttpCode status) {
    this.status = status;
    return this;
  }

  public HttpResponseBuilder headers(List<HttpHeader> headers) {
    this.headers = headers;
    return this;
  }

  public HttpResponseBuilder addHeader(HttpHeader header) {
    if (this.headers == null) {
      this.headers = new ArrayList<>();
    }
    this.headers.add(header);
    return this;
  }

  public HttpResponseBuilder body(HttpResource body) {
    this.body = body;
    return this;
  }

  public HttpResponse build() {
    return new HttpResponse(version, status, headers, body);
  }


  private HttpResponseBuilder version(HttpVersion version) {
    this.version = version;
    return this;
  }

}
