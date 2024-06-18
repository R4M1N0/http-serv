package de.reqbal.httpserv;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        var serv = new HttpServer(8080);
        serv.run();
    }
}
