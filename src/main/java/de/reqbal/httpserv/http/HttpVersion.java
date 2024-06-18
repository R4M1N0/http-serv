package de.reqbal.httpserv.http;

import java.util.Map;

public enum HttpVersion {
  ONE_ZERO,
  ONE_ONE,
  TWO_ZERO;

  public static Map<String, HttpVersion> VERSION_IDENTIIFER;
  public static Map<HttpVersion, String> REVERSE_VERSION_IDENTIIFER;
  static {
    VERSION_IDENTIIFER = Map.of(
        "HTTP/1.0", ONE_ZERO,
        "HTTP/1.1", ONE_ONE,
        "HTTP/2.0", TWO_ZERO
    );
    REVERSE_VERSION_IDENTIIFER = Map.of(ONE_ZERO, "HTTP/1.0", ONE_ONE, "HTTP/1.1", TWO_ZERO, "HTTP/2.0");
  }

  public static HttpVersion get(String versionIdentifier) {
    return VERSION_IDENTIIFER.get(versionIdentifier);
  }

  public static String from(HttpVersion version) {
    return REVERSE_VERSION_IDENTIIFER.get(version);
  }

}
