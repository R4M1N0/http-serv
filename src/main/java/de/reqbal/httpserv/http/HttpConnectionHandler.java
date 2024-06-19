package de.reqbal.httpserv.http;

import de.reqbal.httpserv.http.exception.HttpRequestInputException;
import de.reqbal.httpserv.route.RouteContainer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.OffsetDateTime;
import java.util.Optional;

public class HttpConnectionHandler {
  public static final String SERVER = "server";
  private final HttpRequestParseGateway httpRequestParseGateway;
  private final HttpResponseSerializer httpResponseSerializer;
  private final RouteContainer routeContainer;
  private final HttpStaticResourceLoader httpStaticResourceLoader;

  public HttpConnectionHandler(HttpRequestParseGateway httpRequestParseGateway, RouteContainer routeContainer,
                               HttpStaticResourceLoader httpStaticResourceLoader) {
    this.httpRequestParseGateway = httpRequestParseGateway;
    this.routeContainer = routeContainer;
    this.httpStaticResourceLoader = httpStaticResourceLoader;
    this.httpResponseSerializer = new HttpResponseSerializer();
  }

  public void serve(BufferedReader in, PrintWriter out) throws IOException, InterruptedException {
    //Parse
    HttpRequest httpRequest = Optional.ofNullable(getHttpRequest(in)).orElseThrow(HttpRequestInputException::new);

    HttpResponse response;
    try {
      Object result = getResult(httpRequest);
      response = new HttpResponse(HttpVersion.ONE_ZERO, HttpCode.OK, OffsetDateTime.now(), SERVER, result);
    } catch (IOException ex) {
      response = new HttpResponse(HttpVersion.ONE_ZERO, HttpCode.NOT_FOUND, OffsetDateTime.now(), SERVER, null);
    } catch (Exception ex) {
      response =
          new HttpResponse(HttpVersion.ONE_ZERO, HttpCode.INTERNAL_SERVER_ERROR, OffsetDateTime.now(), SERVER, null);
    }

    //Build Response
    var protocolResponse = httpResponseSerializer.serialize(response);
    out.write(protocolResponse);
    out.flush();
  }

  private Object getResult(HttpRequest httpRequest) throws IOException {
    var matchedRoute = routeContainer.getRoute(httpRequest.method(), httpRequest.uri());
    Object result;
    if (matchedRoute.isEmpty()) {
      result = httpStaticResourceLoader.load(httpRequest.uri());
    } else {
      var route = matchedRoute.get();
      result = route.invocation().apply(httpRequest);
    }
    return result;
  }

  private HttpRequest getHttpRequest(BufferedReader in) {
    HttpRequest httpRequest = null;
    try {
      httpRequest = httpRequestParseGateway.parse(in);
    } catch (Exception ex) {
      // do nothjng
    }
    return httpRequest;
  }
}
