package com.github.kkrull.greeting;

import com.jayway.restassured.RestAssured;
import info.javaspec.dsl.It;
import info.javaspec.runner.JavaSpecRunner;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(JavaSpecRunner.class)
public class NameServletTest {
  class GET {
    class given_a_blank_request {
      It works = () -> {
        File webappDir = new File("src/main/webapp/");
        System.out.println("configuring app with basedir: " + webappDir.getAbsolutePath());

        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/javascript-sandbox");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new NameServlet()), "/person/name");

        server.start();

        RestAssured.when().get("/javascript-sandbox/person/name").then().statusCode(200);

//        server.join();
      };
    }
  }
}