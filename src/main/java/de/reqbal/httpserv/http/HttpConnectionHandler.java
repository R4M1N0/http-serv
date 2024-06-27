package de.reqbal.httpserv.http;

import de.reqbal.httpserv.context.annotation.Inject;
import de.reqbal.httpserv.context.annotation.WebInfrastructure;
import de.reqbal.httpserv.http.exception.HttpRequestInputException;
import de.reqbal.httpserv.http.model.HttpCode;
import de.reqbal.httpserv.http.request.HttpRequest;
import de.reqbal.httpserv.http.request.HttpRequestParseGateway;
import de.reqbal.httpserv.http.resource.HttpResource;
import de.reqbal.httpserv.http.resource.HttpResourceImpl;
import de.reqbal.httpserv.http.response.HttpResponseBuilder;
import de.reqbal.httpserv.http.response.HttpResponseHeaderProvider;
import de.reqbal.httpserv.http.response.HttpResponseSerializer;
import de.reqbal.httpserv.route.RouteContainer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebInfrastructure
public class HttpConnectionHandler {
  private final HttpRequestParseGateway httpRequestParseGateway;
  private final HttpResponseSerializer httpResponseSerializer;
  private final RouteContainer routeContainer;
  private final HttpStaticResourceLoader httpStaticResourceLoader;
  private final HttpResponseHeaderProvider httpResponseHeaderProvider;

  @Inject
  public HttpConnectionHandler(HttpRequestParseGateway httpRequestParseGateway,
                               HttpResponseSerializer httpResponseSerializer, RouteContainer routeContainer,
                               HttpStaticResourceLoader httpStaticResourceLoader,
                               HttpResponseHeaderProvider httpResponseHeaderProvider) {
    this.httpRequestParseGateway = httpRequestParseGateway;
    this.httpResponseSerializer = httpResponseSerializer;
    this.routeContainer = routeContainer;
    this.httpStaticResourceLoader = httpStaticResourceLoader;
    this.httpResponseHeaderProvider = httpResponseHeaderProvider;
  }

  public void serve(BufferedReader in, PrintWriter out) throws IOException, InterruptedException {
    //Parse
    HttpRequest httpRequest = Optional.ofNullable(getHttpRequest(in)).orElseThrow(HttpRequestInputException::new);


    var responseBuilder = HttpResponseBuilder.builder()
        .headers(httpResponseHeaderProvider.getDefaultHeaders());

    try {
      HttpResource result = getResult(httpRequest);
      responseBuilder = responseBuilder.status(HttpCode.OK).body(result);
      httpResponseHeaderProvider.getMimeHeader(result).ifPresent(responseBuilder::addHeader);
    } catch (IOException ex) {
      responseBuilder = responseBuilder.status(HttpCode.NOT_FOUND);
    } catch (Exception ex) {
      System.err.println(ex);
      responseBuilder = responseBuilder.status(HttpCode.INTERNAL_SERVER_ERROR);
    }

    var response = responseBuilder.build();
    var protocolResponse = httpResponseSerializer.serialize(response);
    out.write(protocolResponse);
    out.flush();
  }

  private HttpResource getResult(HttpRequest httpRequest) throws IOException {
    var matchedRoute = routeContainer.getRoute(httpRequest.method(), httpRequest.uri());
    HttpResource result;
    if (matchedRoute.isEmpty()) {
      result = httpStaticResourceLoader.load(httpRequest.uri());
    } else {
      var route = matchedRoute.get();
      result = new HttpResourceImpl(route.mimeProduces(), route.invocation().apply(httpRequest));
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
