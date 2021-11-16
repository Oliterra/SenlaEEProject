package edu.senla.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Connection;

@Retention(RetentionPolicy.RUNTIME)
public @interface Transaction { }
