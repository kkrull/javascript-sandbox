package com.github.kkrull.greeting;

public interface PersonGateway {
  String firstName(long id);
  Person get(long id);
}