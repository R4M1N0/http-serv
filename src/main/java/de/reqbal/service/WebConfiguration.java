package de.reqbal.service;

import de.reqbal.httpserv.context.annotation.Configuration;
import de.reqbal.httpserv.context.annotation.WebInfrastructure;

@Configuration
public class WebConfiguration {

  @WebInfrastructure
  public int port() {
    return 8080;
  }

  @WebInfrastructure
  public boolean autodiscoverRoutes() {
    return true;
  }

  @WebInfrastructure
  public String baseScanPackage() {
    return "de.reqbal.service";
  }

  @WebInfrastructure
  public String basePath() {
    return "/home/ramine/code/personal/http-serv/http-serv/src/main/resources/www/";
  }


}
