package de.reqbal.httpserv.context;

import de.reqbal.httpserv.HttpServer;
import de.reqbal.httpserv.context.annotation.Configuration;
import de.reqbal.httpserv.context.annotation.WebInfrastructure;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import org.reflections.Reflections;

public class WebInfrastructureContext extends AbstractSingletonContext {

  public WebInfrastructureContext(String baseScanPackage) {
    super();
    scan(baseScanPackage);
  }

  public static void run(String baseScanPackage) {
    Instant start = Instant.now();
    var context = new WebInfrastructureContext(baseScanPackage);
    try {
      var server = context.createGetInstance(HttpServer.class);
      Instant end = Instant.now();
      var duration = Duration.between(start, end);
      System.out.println("Duration of Context Initation" + duration);
      server.run();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  private void scan(String baseScanPackage) {
    scanConfiguration(baseScanPackage);
    scanWebInfrastructure();
  }


  private void scanConfiguration(String baseScanPackage) {
    Reflections reflections = new Reflections(baseScanPackage);
    var clazzes = reflections.getTypesAnnotatedWith(Configuration.class);
    for (var clazz : clazzes) {
      try {
        var configuration = clazz.getAnnotation(Configuration.class);
        var name = configuration.name();
        if (name.isBlank()) {
          name = clazz.getName();
        }
        createGetInstance(clazz, name);

      } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void scanWebInfrastructure() {
    Reflections reflections = new Reflections("de.reqbal.httpserv");
    var clazzes = reflections.getTypesAnnotatedWith(WebInfrastructure.class);
    for (var clazz : clazzes) {
      try {
        var controller = clazz.getAnnotation(WebInfrastructure.class);
        var name = controller.name();
        if (name.isBlank()) {
          name = clazz.getName();
        }
        createGetInstance(clazz, name);

      } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
