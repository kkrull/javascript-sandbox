package com.github.kkrull.greeting;

import info.javaspec.dsl.Because;
import info.javaspec.dsl.It;
import info.javaspec.runner.JavaSpecRunner;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(JavaSpecRunner.class)
public class NameServletTest {
  private final NameServlet subject = new NameServlet();
  private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
  private final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

  class doGet {
    class given_a_valid_request {
      Because of = () -> subject.doGet(request, response);
      It sets_status_200 = () -> verify(response).setStatus(200);
      It sets_a_json_content_type = () -> verify(response).setContentType("application/json");
      It responds_with_the_persons_name;
    }
  }
}