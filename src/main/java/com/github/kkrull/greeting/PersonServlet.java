package com.github.kkrull.greeting;

import sun.misc.Regexp;

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

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Pattern requestPattern = Pattern.compile("/(\\d+)/name");
    Matcher matcher = requestPattern.matcher(request.getPathInfo());
    if(!matcher.matches()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    String id = matcher.group(1);
    String firstName = gateway.firstName(Long.parseLong(id));
    if(firstName == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    response.setContentType(jsonContentType().toString());
    String serialized = String.format("{\"firstName\": \"%s\"}", firstName);
    response.getWriter().println(serialized);
  }

  private MimeType jsonContentType() {
    try {
      return new MimeType("application/json");
    } catch(MimeTypeParseException e) {
      throw new MimeTypeException(e);
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  }

  static class MimeTypeException extends RuntimeException {
    MimeTypeException(Exception cause) { super(cause); }
  }
}