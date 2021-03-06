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

@WebServlet(name = "NameServlet", urlPatterns = "/person/*")
public final class PersonServlet extends HttpServlet {
  private final PersonGateway gateway;

  public PersonServlet() {
    this.gateway = new OnePersonGateway();
  }

  PersonServlet(PersonGateway gateway) {
    this.gateway = gateway;
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
    PersonRequest request = parseRequest(req);
    request.respond(response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
  }

  private PersonRequest parseRequest(HttpServletRequest httpRequest) {
    Pattern parameterPattern = Pattern.compile("^/(\\d+)/?$");
    Matcher matcher = parameterPattern.matcher(httpRequest.getPathInfo());
    if(!matcher.matches()) {
      return response -> response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    Long id = Long.parseLong(matcher.group(1));
    Person person = gateway.get(id);
    return person == null ?
      response -> response.setStatus(HttpServletResponse.SC_NOT_FOUND) :
      response -> {
        response.setContentType(jsonContentType().toString());
        String serialized = String.format("{\"id\": %d, \"firstName\": \"%s\"}", person.id, person.firstName);
        response.getWriter().println(serialized);
      };
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

  private static class OnePersonGateway implements PersonGateway {
    private static final Person ONLY_PERSON = new Person(42, "Duke");

    @Override
    public String firstName(long id) {
      return id == ONLY_PERSON.id ? ONLY_PERSON.firstName : null;
    }

    @Override
    public Person get(long id) {
      return id == ONLY_PERSON.id ? ONLY_PERSON : null;
    }
  }
}