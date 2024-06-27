package de.reqbal.httpserv.context;

import de.reqbal.httpserv.context.annotation.Inject;
import de.reqbal.httpserv.context.annotation.Qualifier;
import de.reqbal.httpserv.context.annotation.WebInfrastructure;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractSingletonContext {

  private final Map<String, Object> singletons;

  private static final List<Class<?>> PRIMITIVES = List.of(
      boolean.class,
      int.class,
      long.class,
      double.class,
      String.class
  );

  public AbstractSingletonContext() {
    this.singletons = new HashMap<>();
  }

  public <T> T createGetInstance(Class<T> clazz)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

    var name = clazz.getName();
    return createGetInstance(clazz, name);
  }

  public <T> T createGetInstance(Class<T> clazz, String name)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

    //Check Cache
    var instance = (T) singletons.get(name);
    if (null != instance) {
      return instance;
    }

    if (PRIMITIVES.contains(clazz)) {

    }

    return createClassInstance(clazz, name);
  }

  private <T> T createClassInstance(Class<T> clazz, String name)
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
      throw new RuntimeException("No suitable constructor found for " + clazz + " in constructor " + constructor);
    }

    List<Object> args = getMethodArgs(constructor);
    instance = constructor.newInstance(args.toArray());
    singletons.put(name, instance);

    //Eval methods
    scanMethodMembers(clazz, constructor, instance);


    return instance;
  }

  private <T> void scanMethodMembers(Class<T> clazz, Constructor<T> constructor, T instance)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    var methods = Arrays.stream(clazz.getMethods())
        .filter(Objects::nonNull)
        .filter(m -> m.getAnnotation(WebInfrastructure.class) != null)
        .collect(Collectors.toList());

    for (var method : methods) {
      var methodAnnotation = method.getAnnotation(WebInfrastructure.class);
      var methodName = methodAnnotation.name();
      if (methodName.isBlank()) {
        methodName = method.getName();
      }

      List<Object> mArgs = getMethodArgs(constructor);
      Object methodResult = method.invoke(instance, mArgs.toArray());
      singletons.put(methodName, methodResult);
    }
  }

  private <T> List<Object> getMethodArgs(Constructor<T> constructor)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    return getMethodArgs(constructor.getParameters());
  }

  private <T> List<Object> getMethodArgs(Parameter[] parameters)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    List<Object> args = new ArrayList<>();
    for (Parameter parameter : parameters) {
      Class<?> type = parameter.getType();
      var origninalName = type.getName();
      try {
      var paramName = type.getName();
      var qualifier = parameter.getAnnotation(Qualifier.class);
      if (null != qualifier) {
        paramName = qualifier.name();
      }

      var child = createGetInstance(type, paramName);
      args.add(child);
      } catch (RuntimeException ex) {
        System.err.println(ex);
        throw new RuntimeException("Could not resolve parameter " + origninalName);
      }
    }
    return args;
  }
}
