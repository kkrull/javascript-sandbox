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
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.servlet.Servlet;

import static org.hamcrest.CoreMatchers.equalTo;

@SuppressWarnings("UnusedDeclaration")
@RunWith(JavaSpecRunner.class)
public class PersonServletTest {
  private Server server = new Server(8080);
  private Response response;
  private final PersonGateway gateway = Mockito.mock(PersonGateway.class);

  Establish server_started = () -> runServlet("/", "/person/*", new PersonServlet(gateway));
  Cleanup stop_server = () -> server.stop();

  class GET {
    class given_an_invalid_request {
      Establish that = () -> personIs(new Person(42, "Jarvis"));
      Because of = () -> doGet("/person/");
      It responds_400_bad_request = () -> response.then().statusCode(400);
    }

    class given_the_id_for_an_unknown_person {
      Establish that = () -> Mockito.stub(gateway.firstName(Matchers.anyLong())).toReturn(null);
      Because of = () -> doGet("/person/0");
      It responds_404_not_found = () -> response.then().statusCode(404);
    }

    class given_a_valid_person_request {
      Establish that = () -> personIs(new Person(42, "Jarvis"));
      Because of = () -> doGet("/person/42");
      It has_status_200_ok = () -> response.then().statusCode(200);
      It content_is_json = () -> response.then().contentType("application/json");
      It contains_the_specified_persons_attributes = () -> response.then()
        .body("id", equalTo(42))
        .body("firstName", equalTo("Jarvis"));
    }
  }

  class given_any_other_request_method {
    Because of = () -> doPost("/person/1");
    It has_stat_405_method_not_allowed = () -> response.then().statusCode(405);
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

  private void personIs(Person person) {
    Mockito.stub(gateway.firstName(person.id)).toReturn(person.firstName);
    Mockito.stub((gateway.get(person.id))).toReturn(person);
  }

  private void doGet(String url) {
    response = RestAssured.when().get(url);
  }
  private void doPost(String url) { response = RestAssured.when().post(url); }
}