package com.github.kkrull.greeting;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "NameServlet", urlPatterns = "/people/*")
public final class PersonServlet extends HttpServlet {
  private final PersonGateway gateway;

  PersonServlet(PersonGateway gateway) {
    this.gateway = gateway;
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
    PersonRequest request = parseRequest(req);
    if(request == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    if("name".equals(request.requested)) {
      String firstName = gateway.firstName(request.id);
      if(firstName == null) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
      }

      response.setContentType(jsonContentType().toString());
      String serialized = String.format("{\"firstName\": \"%s\"}", firstName);
      response.getWriter().println(serialized);
      return;
    }

    Person person = gateway.get(request.id);
    response.setContentType(jsonContentType().toString());
    String serialized = String.format("{\"id\": %d, \"firstName\": \"%s\"}", person.id, person.firstName);
    response.getWriter().println(serialized);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    throw new UnsupportedOperationException();
  }

  private static PersonRequest parseRequest(HttpServletRequest httpRequest) {
    Pattern parameterPattern = Pattern.compile("^/(\\d+)(/(name)?)?$");
    Matcher matcher = parameterPattern.matcher(httpRequest.getPathInfo());
    System.out.printf("pathInfo=%s\n", httpRequest.getPathInfo());
    if(!matcher.matches()) {
      return null;
    }

    String id = matcher.group(1);
    String requested = matcher.group(3);
    System.out.printf("- id=%s, requested=%s\n", id, requested);
    return new PersonRequest(id == null ? null : Long.parseLong(id), requested);
  }

  private static class PersonRequest {
    public final Long id;
    public final String requested;

    public PersonRequest(Long id, String requested) {
      this.id = id;
      this.requested = requested;
    }
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