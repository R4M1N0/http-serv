package de.reqbal.httpserv.route;

import de.reqbal.httpserv.http.HttpMethod;
import de.reqbal.httpserv.http.HttpRequest;
import java.util.function.Function;

public record Route(HttpMethod method, String path, Function<HttpRequest, Object> invocation) {
}
