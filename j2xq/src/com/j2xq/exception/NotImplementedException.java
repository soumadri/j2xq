package com.j2xq.exception;

public class NotImplementedException extends Exception {
	/**
	 * 
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
