package com.github.kkrull.greeting;

import info.javaspec.dsl.It;
import info.javaspec.runner.JavaSpecRunner;
import org.apache.catalina.Context;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(JavaSpecRunner.class)
public class NameServletTest {
  class GET {
    class given_a_blank_request {
      It works = () -> {
        File webappDir = new File("src/main/webapp/");
        System.out.println("configuring app with basedir: " + webappDir.getAbsolutePath());

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tomcat");
        tomcat.setPort(8080);

        Context context = tomcat.addWebapp("/javascript-sandbox", webappDir.getAbsolutePath());
        tomcat.addServlet(context.getPath(), "NameServlet", new NameServlet());
        context.addServletMapping("/*", "NameServlet");
        tomcat.start();
        tomcat.getServer().await();
//        tomcat.stop();
      };
    }
  }
}