package com.j2xq.exception;

/**
 * Generic runtime exception wrapping all the exception thrown from the Java to XQuery code
 * @author soumadri
 *
 */
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
