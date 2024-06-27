package de.reqbal.httpserv.context;

import de.reqbal.httpserv.context.annotation.WebInfrastructure;
import java.lang.reflect.InvocationTargetException;
import org.reflections.Reflections;

public class WebInfrastructureContext extends AbstractSingletonContext{

  public WebInfrastructureContext() {
    super();
    scan();
  }

  private void scan() {

    Reflections reflections = new Reflections("de.reqbal.httpserv");
    var clazzes = reflections.getTypesAnnotatedWith(WebInfrastructure.class);
    for (var clazz : clazzes) {
      try {
        createGetInstance(clazz);

      } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

  }

}
