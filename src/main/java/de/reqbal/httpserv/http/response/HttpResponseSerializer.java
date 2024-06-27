package de.reqbal.httpserv.http.response;

import de.reqbal.httpserv.context.annotation.Inject;
import de.reqbal.httpserv.context.annotation.WebInfrastructure;
import de.reqbal.httpserv.http.model.HttpVersion;
import de.reqbal.httpserv.http.resource.HttpBodySerializer;
import de.reqbal.httpserv.http.resource.HttpResource;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@WebInfrastructure
public class HttpResponseSerializer {

  public static final String SINGLE_SPACE = " ";
  public static final String NEW_LINE = "\n";

  private final HttpBodySerializer httpBodySerializer;

  @Inject
  public HttpResponseSerializer(HttpBodySerializer httpBodySerializer) {
    this.httpBodySerializer = httpBodySerializer;
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
    HttpResource resource = httpResponse.body();
    if (resource == null) {
      return response;
    }

    Object body = resource.value();
    byte[] bodyBytes = httpBodySerializer.serialize(body);
    if (null != bodyBytes) {
      response += new String(bodyBytes, StandardCharsets.UTF_8);
      response += NEW_LINE;
    }
    return response;
  }

  private static String appendHeaders(HttpResponse httpResponse, String response) {
    var headers = httpResponse.header();
    var resDiff = headers.stream().map(header -> header.key() + ":" + SINGLE_SPACE + header.value())
        .collect(Collectors.joining(NEW_LINE));
    response += resDiff;
    return response;
  }
}
