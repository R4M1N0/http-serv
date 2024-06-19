package de.reqbal.httpserv;

import de.reqbal.httpserv.http.HttpMethod;
import de.reqbal.httpserv.route.Route;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        var serv = new HttpServer(8080, List.of(new Route(HttpMethod.GET, "/x.json", req -> "Hello World")));
        serv.run();
    }
}
