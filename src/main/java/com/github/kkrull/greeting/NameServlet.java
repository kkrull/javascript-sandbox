package com.github.kkrull.greeting;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "NameServlet", urlPatterns = "/person/name")
public class NameServlet extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    MimeType type = null;
//    try {
//      type = new MimeType("application/json");
//    } catch(MimeTypeParseException e) {
//      e.printStackTrace();
//    }
//    response.setContentType(type.toString());
//    response.setStatus(HttpServletResponse.SC_OK);
//    response.getWriter().println("{\"firstName\": \"Bob\"}");
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    throw new UnsupportedOperationException();
  }
}