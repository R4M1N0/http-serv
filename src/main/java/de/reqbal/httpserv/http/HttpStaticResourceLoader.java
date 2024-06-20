package de.reqbal.httpserv.http;

import de.reqbal.httpserv.http.resource.HttpResource;
import de.reqbal.httpserv.http.resource.HttpResourceImpl;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;

public class HttpStaticResourceLoader {

  private final String basePath;

  public HttpStaticResourceLoader(String basePath) {
    this.basePath = basePath;
  }

  public HttpResource load(String uri) throws IOException {
    var fullUri = basePath + uri;
    var file = new File(fullUri);
    FileInputStream in = new FileInputStream(file);
    var buffered = new BufferedInputStream(in);
    try(DataInputStream reader = new DataInputStream(buffered)) {
      var contentType = URLConnection.guessContentTypeFromStream(buffered);
      int nBytesToRead = reader.available();
      if (nBytesToRead > 0) {
        byte[] bytes = new byte[nBytesToRead];
        reader.read(bytes);
        return new HttpResourceImpl(contentType, bytes);
      }
    }
    return null;
  }
}
