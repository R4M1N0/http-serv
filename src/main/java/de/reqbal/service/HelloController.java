package de.reqbal.service;

import de.reqbal.httpserv.context.annotation.Inject;
import de.reqbal.httpserv.http.request.HttpRequest;
import de.reqbal.httpserv.route.annotation.Controller;
import de.reqbal.httpserv.route.annotation.GET;
import de.reqbal.httpserv.route.annotation.POST;
import java.util.List;

@Controller(path = "/hello")
@SuppressWarnings("unused")
public class HelloController {

  private final HelloService helloService;

  @Inject
  public HelloController(HelloService helloService) {
    this.helloService = helloService;
  }


  @GET(accepts = "*", produces = "*")
  public String hello(HttpRequest request) {
    return "Hello World";
  }

  @GET(path = "/messages",accepts = "*", produces = "*")
  public List<String> getMessages(HttpRequest request) {
    return helloService.getMessages();
  }

  @POST(path = "/messages",accepts = "*", produces = "*")
  public String saveMessages(HttpRequest request) {
     helloService.addMessage(request.body());
     return null;
  }
}
