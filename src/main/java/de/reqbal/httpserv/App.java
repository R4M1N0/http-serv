package de.reqbal.httpserv;

import de.reqbal.httpserv.http.model.HttpMethod;
import de.reqbal.httpserv.http.request.HttpRequest;
import de.reqbal.httpserv.route.Route;
import de.reqbal.service.HelloService;
import java.util.List;
import java.util.function.Function;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main(String[] args ) {
        var service = new HelloService();

        List<Route> ROUTES = List.of(
            new Route(HttpMethod.GET, "/sample", req -> "Hello World", "*", "text/html"),
            new Route(HttpMethod.GET, "/hello", getHello(service), "*", "text/html"),
            new Route(HttpMethod.POST, "/hello", addHello(service), "text", "text/html")
        );

        var serv = new HttpServer(8080, ROUTES);
        serv.run();
    }

    private static Function<HttpRequest, Object> getHello(HelloService helloService) {
        return req -> {
            throw new RuntimeException("ex");
        };
    }

    private static Function<HttpRequest, Object> addHello(HelloService helloService) {
        return req -> {
            helloService.addMessage(req.body());
            return null;
        };
    }
}
