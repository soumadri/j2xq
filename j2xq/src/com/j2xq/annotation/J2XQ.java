package com.j2xq.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface J2XQ {
	String name(); 
	String value(); 	
}
