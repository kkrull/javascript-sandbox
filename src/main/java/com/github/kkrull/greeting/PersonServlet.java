package com.github.kkrull.greeting;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "NameServlet", urlPatterns = "/person/*")
public final class PersonServlet extends HttpServlet {
  private final PersonGateway gateway;

  public PersonServlet() {
    this.gateway = new PersonGateway() {
      @Override
      public String firstName(long id) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Person get(long id) {
        throw new UnsupportedOperationException();
      }
    };
  }

  PersonServlet(PersonGateway gateway) {
    this.gateway = gateway;
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
    PersonRequest request = parseRequest(req);
    request.respond(response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.printf("doPost:\n");
//    response.setStatus(HttpServletResponse.SC_CREATED);

    BufferedReader reader = request.getReader();
    String serialized = reader.readLine();
    if(reader.readLine() != null) {
      throw new UnsupportedOperationException("Multi-line JSON request");
    }

    Pattern parameterPattern = Pattern.compile(".*\"firstName\":\"(.*)\".*");
    Matcher matcher = parameterPattern.matcher(serialized);
    if(!matcher.matches()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    String firstName = matcher.group(1);
    Person person = new Person(1, firstName);

    response.setContentType(jsonContentType().toString());
    String updated = String.format("{\"id\": %d, \"firstName\": \"%s\"}", person.id, person.firstName);
    System.out.printf("- response: %s\n", updated);
    response.getWriter().println(updated);
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doPost(req, resp);
  }

  private PersonRequest parseRequest(HttpServletRequest httpRequest) {
    Pattern parameterPattern = Pattern.compile("^/(\\d+)(/(name)?)?$");
    Matcher matcher = parameterPattern.matcher(httpRequest.getPathInfo());
    if(!matcher.matches()) {
      return response -> response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    PersonRequest unknownPersonRequest = response -> response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    Long id = Long.parseLong(matcher.group(1));
    String requested = matcher.group(3);
    if("name".equals(requested)) {
      String firstName = gateway.firstName(id);
      return firstName == null ? unknownPersonRequest : response -> {
        response.setContentType(jsonContentType().toString());
        String serialized = String.format("{\"firstName\": \"%s\"}", firstName);
        response.getWriter().println(serialized);
      };
    } else {
      Person person = gateway.get(id);
      return person == null ? unknownPersonRequest : response -> {
        response.setContentType(jsonContentType().toString());
        String serialized = String.format("{\"id\": %d, \"firstName\": \"%s\"}", person.id, person.firstName);
        response.getWriter().println(serialized);
      };
    }
  }

  private interface PersonRequest {
    public void respond(HttpServletResponse response) throws IOException;
  }

  private MimeType jsonContentType() {
    try {
      return new MimeType("application/json");
    } catch(MimeTypeParseException e) {
      throw new MimeTypeException(e);
    }
  }

  static class MimeTypeException extends RuntimeException {
    MimeTypeException(Exception cause) { super(cause); }
  }
}