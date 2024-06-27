package de.reqbal.httpserv.route;

import de.reqbal.httpserv.context.WebContext;
import de.reqbal.httpserv.context.annotation.Inject;
import de.reqbal.httpserv.context.annotation.Qualifier;
import de.reqbal.httpserv.context.annotation.WebInfrastructure;
import de.reqbal.httpserv.http.model.HttpMethod;
import de.reqbal.httpserv.http.request.HttpRequest;
import de.reqbal.httpserv.route.annotation.Controller;
import de.reqbal.httpserv.route.annotation.GET;
import de.reqbal.httpserv.route.annotation.POST;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.reflections.Reflections;

@WebInfrastructure
public class RouteDiscoveryProcessor {

  private static List<Class<? extends Annotation>> METHODS = List.of(GET.class, POST.class);

  private final String baseScanPackage;

  private final WebContext webContext;

  @Inject
  public RouteDiscoveryProcessor(@Qualifier(name = "baseScanPackage") String baseScanPackage) {
    this.baseScanPackage = baseScanPackage;
    this.webContext = new WebContext();
  }

  public List<Route> getControllerRoutes() {
    List<Route> routes = new ArrayList<>();

    Reflections reflections = new Reflections(baseScanPackage);
    var clazzes = reflections.getTypesAnnotatedWith(Controller.class);
    for (var clazz : clazzes) {
      try {
        var controller = clazz.getAnnotation(Controller.class);
        var instance = createInstance(clazz, controller);
        var basePath = controller.path();
        var methods = clazz.getMethods();
        for (var method : methods) {
          var methodAnnotations = method.getDeclaredAnnotations();
          for (var annotation : methodAnnotations) {
            if (METHODS.contains(annotation.annotationType())) {
              routes.add(switch (annotation) {
                case GET m -> get(instance, basePath, m, method);
                case POST m -> post(instance, basePath, m, method);
                default -> throw new IllegalStateException("Unexpected value: " + annotation);
              });
            }
          }
        }

      } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    return routes;
  }

  private Object createInstance(Class<?> clazz, Controller controller)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    var name = controller.name();
    if (name.isBlank()) {
      name = clazz.getName();
    }
    return webContext.createGetInstance(clazz, name);
  }

  private Route post(Object instance, String basePath, POST m, Method method) {
    var fullPath = basePath + m.path();
    return createRoute(HttpMethod.POST, instance, method, fullPath, m.accepts(), m.produces());
  }

  private Route get(Object instance, String basePath, GET m, Method method) {
    var fullPath = basePath + m.path();
    return createRoute(HttpMethod.GET, instance, method, fullPath, m.accepts(), m.produces());
  }

  private static Route createRoute(HttpMethod httpMethod, Object instance, Method method, String fullPath,
                                   String accepts, String produces) {
    Arrays.stream(method.getParameters())
        .filter(p -> p.getType() == HttpRequest.class)
        .findFirst()
        .orElseThrow(() -> new RuntimeException(method + "does not have HttpRequest Param"));
    Function<HttpRequest, Object> invocation = request -> {
      try {
        return method.invoke(instance, request);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    };
    return new Route(httpMethod, fullPath, invocation, accepts, produces);
  }



}
