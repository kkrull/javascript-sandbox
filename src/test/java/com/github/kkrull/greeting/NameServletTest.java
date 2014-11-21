package com.github.kkrull.greeting;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import info.javaspec.dsl.Because;
import info.javaspec.dsl.Cleanup;
import info.javaspec.dsl.Establish;
import info.javaspec.dsl.It;
import info.javaspec.runner.JavaSpecRunner;
import org.junit.Assert;
import org.junit.runner.RunWith;

@RunWith(JavaSpecRunner.class)
public class NameServletTest {
  private final ServletRunner runner = new ServletRunner();
  private ServletUnitClient client;
  private WebRequest request;
  private WebResponse response;

  Establish start_servlet = () -> {
    runner.registerServlet("person/name", NameServlet.class.getName());
    client = runner.newClient();
  };
  Cleanup kill_servlet = () -> runner.shutDown();

  class GET {
    class given_a_blank_request {
      Establish that = () -> request = new GetMethodWebRequest("http://localhost/person/name");
      Because of = () -> response = client.getResponse(request);
      It response_has_status_200 = () -> Assert.assertEquals(200, response.getResponseCode());
    }
  }
}