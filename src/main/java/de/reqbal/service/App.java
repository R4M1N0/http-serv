package de.reqbal.service;

import de.reqbal.httpserv.HttpServer;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main(String[] args ) {

        var serv = HttpServer.builder()
            .withPort(8080)
            .discoverRoutes("de.reqbal.service")
            .build();
        serv.run();
    }
}
