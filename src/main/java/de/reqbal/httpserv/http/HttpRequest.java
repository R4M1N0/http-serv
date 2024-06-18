package de.reqbal.httpserv.http;

public record HttpRequest(HttpMethod method, String uri) {
}
