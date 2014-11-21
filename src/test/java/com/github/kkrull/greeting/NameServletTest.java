package com.github.kkrull.greeting;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import info.javaspec.dsl.Because;
import info.javaspec.dsl.Cleanup;
import info.javaspec.dsl.Establish;
import info.javaspec.dsl.It;
import info.javaspec.runner.JavaSpecRunner;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.runner.RunWith;

import javax.servlet.Servlet;

import static com.jayway.restassured.RestAssured.when;

@RunWith(JavaSpecRunner.class)
public class NameServletTest {
  private final Server server = new Server(8080);
  private Response response;

  Establish server_started = () -> runServlet("/javascript-sandbox", "/person/name", new NameServlet());
  Cleanup stop_server = () -> server.stop();

  class GET {
    class given_a_blank_request {
      Because of = () -> response = when().get("/javascript-sandbox/person/name");
      It has_status_200 = () -> response.then().statusCode(200);
    }
  }

  private void runServlet(String contextPath, String urlPattern, Servlet onlyServlet) throws Exception {
    ServletContextHandler onlyContext = makeContext(contextPath);
    server.setHandler(onlyContext);
    onlyContext.addServlet(new ServletHolder(onlyServlet), urlPattern);
    server.start();
  }

  private static ServletContextHandler makeContext(String contextPath) {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath(contextPath);
    return context;
  }
}