package de.reqbal.httpserv.http;

import java.time.OffsetDateTime;

public record HttpResponse(HttpVersion version, HttpCode code, OffsetDateTime date, String server, Object body) {
}
