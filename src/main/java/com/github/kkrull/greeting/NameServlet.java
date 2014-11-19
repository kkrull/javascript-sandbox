package com.github.kkrull.greeting;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "NameServlet", urlPatterns = "/person/name")
public class NameServlet extends javax.servlet.http.HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    MimeType type = null;
    try {
      type = new MimeType("application/json");
    } catch(MimeTypeParseException e) {
      e.printStackTrace();
    }
    response.setContentType(type.toString());
    response.getWriter().println("{\"firstName\": \"George\"}");
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }
}