package com.j2xq.exception;

public class TypeNotSupportedException extends Exception {
	
	/**
	 * Exception for types which are not supported by this tool
	 */
	private static final long serialVersionUID = 5802998963503860665L;
	String type="";
	public TypeNotSupportedException(String type){
		this.type = type;
	}
	
	public String toString(){
		return type + " type not supported";
	}
}
