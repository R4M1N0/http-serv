package de.reqbal.httpserv.http;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class HttpStaticResourceLoader {

  private final String basePath;
  private final ConcurrentHashMap<String, byte[]> cache;
  private final ConcurrentHashMap<String, OffsetDateTime> cacheTime;

  public HttpStaticResourceLoader(String basePath) {
    this.basePath = basePath;
    this.cache = new ConcurrentHashMap<>();
    this.cacheTime = new ConcurrentHashMap<>();
  }

  public Object load(String uri) throws IOException {
    var fullUri = basePath + uri;

    var cacheHit = cache.get(fullUri);
    var cTime = cacheTime.get(fullUri);
    if (cacheHit != null && cTime != null) {
      var now = OffsetDateTime.now();
      Duration elapsed = Duration.between(cTime, now);
      if (elapsed.toMinutes() <= 30) {
        return cacheHit;
      } else {
        cache.remove(fullUri);
        cacheTime.remove(fullUri);
      }
    }

    DataInputStream reader = new DataInputStream(new FileInputStream(fullUri));
    int nBytesToRead = reader.available();
    if(nBytesToRead > 0) {
      byte[] bytes = new byte[nBytesToRead];
      reader.read(bytes);
      cache.putIfAbsent(fullUri, bytes);
      cacheTime.putIfAbsent(fullUri, OffsetDateTime.now());
      return bytes;
    }
    return null;
  }
}
