package com.j2xq.exception;

public class GenericException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private Exception e;
	public GenericException(Exception e){
		this.e = e;
	}
	
	public String toString(){
		return "J2XQ: Exception thrown from dependencies: " + e.getMessage();
	}
}
