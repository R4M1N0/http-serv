package de.reqbal.httpserv.route;

import de.reqbal.httpserv.http.HttpMethod;

public record Route(HttpMethod method, String path) {
}
