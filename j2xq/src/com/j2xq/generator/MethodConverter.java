package com.j2xq.generator;

import java.lang.reflect.Method;

import com.j2xq.exception.TypeNotSupportedException;

public class MethodConverter {
	public static String convertName(String methodName){
		return methodName.replaceAll("([A-Z])+", "-$1").toLowerCase();
	}
	
	public static String convertParameter(Method method) throws TypeNotSupportedException{
		
		Class<?>[] c = method.getParameterTypes();
		
		String op = "";
		int i = 1;
		for (Class<?> class1 : c) {
			op += "$param" + (i++) + " as " + convertType(class1.getName());
			
			if (i <= c.length)
				op += ", ";
		}
		
		return op;
	}
	
	public static String convertType(String type) throws TypeNotSupportedException{
		if (type == "boolean") {
			return "xs:boolean";
		} else if(type == "byte") {
			return "xs:byte";
		} else if(type == "double") {
			return "xs:double";
		} else if(type == "float") {
			return "xs:float";
		} else if(type == "int") {
			return "xs:int";
		} else if(type == "long") {
			return "xs:long";
		} else if(type == "short") {
			return "xs:short";
		} else if(type == "java.lang.Boolean") {
			return "xs:boolean";
		} else if(type == "java.lang.Byte") {
			return "xs:byte";
		} else if(type == "java.lang.Float") {
			return "xs:float";
		} else if(type == "java.lang.Double") {
			return "xs:double";
		} else if(type == "java.lang.Integer") {
			return "xs:int";
		} else if(type == "java.long.Long") {
			return "xs:long";
		} else if(type == "java.lang.Short") {
			return "xs:short";
		} else if(type == "java.lang.String") {
			return "xs:string";
		} else if(type == "java.math.BigDecimal") {
			return "xs:decimal";
		} else if(type == "java.math.BigInteger") {
			return "xs:integer";
		} else {
			throw new TypeNotSupportedException(type);
		}
	}
}
