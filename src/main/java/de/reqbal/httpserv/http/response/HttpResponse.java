package de.reqbal.httpserv.http.response;

import de.reqbal.httpserv.http.model.HttpCode;
import de.reqbal.httpserv.http.model.HttpHeader;
import de.reqbal.httpserv.http.model.HttpVersion;
import de.reqbal.httpserv.http.resource.HttpResource;
import java.util.List;

public record HttpResponse(HttpVersion version, HttpCode code, List<HttpHeader> header, HttpResource body) {
}
