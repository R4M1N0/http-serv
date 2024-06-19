package de.reqbal.httpserv.http;

import java.util.List;

public record HttpRequest(HttpMethod method, String uri, List<HttpHeader> header, String body) {
}
