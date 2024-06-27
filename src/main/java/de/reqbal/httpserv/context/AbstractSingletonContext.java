package de.reqbal.httpserv.context;

import de.reqbal.httpserv.context.annotation.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractSingletonContext {

  private Map<Class<?>, Object> singletons;

  public AbstractSingletonContext() {
    this.singletons = new HashMap<>();
  }

  public <T> T createGetInstance(Class<T> clazz)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

    //Check Cache
    var instance = (T) singletons.get(clazz);
    if (null != instance) {
      return instance;
    }

    return createNewInstance(clazz);
  }

  private <T> T createNewInstance(Class<T> clazz)
      throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    T instance;
    Constructor<T> fallBack;
    try {
      fallBack = clazz.getDeclaredConstructor();
    } catch (Exception ex) {
      fallBack = null;
    }


    Constructor<T> constructor =
        Arrays.stream(clazz.getConstructors())
            .filter(Objects::nonNull)
            .map(ctor -> (Constructor<T>) ctor)
            .filter(ctor -> ctor.getAnnotation(Inject.class) != null).findFirst()
            .orElse(fallBack);

    if (constructor == null) {
      throw new RuntimeException("No suitable constructor found for "+ clazz);
    }

    List<Object> args = new ArrayList<>();
    for (Parameter parameter : constructor.getParameters()) {
      var child = createGetInstance(parameter.getType());
      args.add(child);
    }

    instance = constructor.newInstance(args.toArray());
    singletons.put(clazz, instance);
    return instance;
  }
}
