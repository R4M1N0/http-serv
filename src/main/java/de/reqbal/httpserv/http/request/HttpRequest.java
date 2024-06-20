package de.reqbal.httpserv.http.request;

import de.reqbal.httpserv.http.model.HttpHeader;
import de.reqbal.httpserv.http.model.HttpMethod;
import java.util.List;

public record HttpRequest(HttpMethod method, String uri, List<HttpHeader> header, String body) {
}
