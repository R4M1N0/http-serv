package de.reqbal.httpserv.http;

public enum HttpCode {
  OK(200, "OK"),
  NOT_FOUND(404, "Not Found"),
  INTERNAL_SERVER_ERROR(500, "Internal Server Error");

  private final int code;
  private final String identifier;

  HttpCode(int code, String identifier) {
    this.code = code;
    this.identifier = identifier;
  }

  public int getCode() {
    return code;
  }

  public String getIdentifier() {
    return identifier;
  }
}
