package de.reqbal.httpserv.http.resource;

public record HttpResourceImpl(String mimeType, Object value) implements HttpResource {
}
