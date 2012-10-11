package com.j2xq.exception;

public class NotImplementedException extends Exception {
	/**
	 * Exception for the features which are not implemented
	 */
	private static final long serialVersionUID = -2144802883662228003L;
	String feature="";
	public NotImplementedException(String feature){
		this.feature = feature;
	}
	
	public String toString(){
		return feature + " is not implemented yet";
	}
}
