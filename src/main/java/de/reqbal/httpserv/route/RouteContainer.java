package de.reqbal.httpserv.route;

import de.reqbal.httpserv.context.annotation.Inject;
import de.reqbal.httpserv.context.annotation.Qualifier;
import de.reqbal.httpserv.context.annotation.WebInfrastructure;
import de.reqbal.httpserv.http.model.HttpMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebInfrastructure
public class RouteContainer {

  private final List<Route> routes;
  private final RouteDiscoveryProcessor routeDiscoveryProcessor;

  @Inject
  public RouteContainer(@Qualifier(name = "autodiscoverRoutes") boolean autodiscoverRoutes,
                        RouteDiscoveryProcessor routeDiscoveryProcessor) {
    this.routeDiscoveryProcessor = routeDiscoveryProcessor;
    this.routes = new ArrayList<>();
    if (autodiscoverRoutes) {
      this.routes.addAll(getDiscoveredRoutes());
    }
  }

  private List<Route> getDiscoveredRoutes() {
    return routeDiscoveryProcessor.getControllerRoutes();
  }

  public RouteContainer withRoute(Route route) {
    this.routes.add(route);
    return this;
  }

  public RouteContainer withRoutes(List<Route> route) {
    if (route != null) {
      this.routes.addAll(route);
    }
    return this;
  }

  public Optional<Route> getRoute(HttpMethod method, String uri) {
    return routes.stream().filter(r -> r.method() == method && r.path().equals(uri)).findFirst();
  }
}
