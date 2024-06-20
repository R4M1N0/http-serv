package de.reqbal.httpserv.route;

import de.reqbal.httpserv.http.model.HttpMethod;
import de.reqbal.httpserv.http.request.HttpRequest;
import java.util.function.Function;

public record Route(HttpMethod method, String path, Function<HttpRequest, Object> invocation, String mimeAccepts,
                    String mimeProduces) {
}
