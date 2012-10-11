package com.j2xq.generator;

import java.lang.reflect.Method;

import com.j2xq.exception.TypeNotSupportedException;
import com.j2xq.type.TypeConverter;
/**
 * Converts Java method signatures to XQuery function signature
 * @author soumadri
 *
 */
public class MethodConverter {
	
	/**
	 * Converts camel case functions to XQuery style function names. E.g. getValue() to get-value() 
	 * @param methodName Name of the method
	 * @return XQuery style function name for the specified method name
	 */
	public static String convertName(String methodName){
		return methodName.replaceAll("([A-Z])+", "-$1").toLowerCase();
	}
	
	/**
	 * Converts java method parameters to its XQuery equivalent. E.g. String val to $val as xs:string
	 * @param method The reference to the java method
	 * @return Comma separated XQuery function parameter list 
	 * @throws TypeNotSupportedException
	 */
	public static String convertParameter(Method method) throws TypeNotSupportedException{
		
		Class<?>[] c = method.getParameterTypes();
		
		String op = "";
		int i = 1;
		for (Class<?> class1 : c) {
			op += "$param" + (i++) + " as " + TypeConverter.convertJavaTypeToXQueryType(class1.getName());
			
			if (i <= c.length)
				op += ", ";
		}
		
		return op;
	}
		
}
