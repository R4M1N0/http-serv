package de.reqbal.httpserv.http;

import java.time.Instant;

public record HttpResponse(HttpVersion version, HttpCode code, Instant date, String server, String body) {
}
