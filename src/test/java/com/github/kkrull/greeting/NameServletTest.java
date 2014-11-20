package com.github.kkrull.greeting;

import info.javaspec.dsl.It;
import info.javaspec.runner.JavaSpecRunner;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JavaSpecRunner.class)
public class NameServletTest {
  It works = () -> assertEquals(42, 42);
}