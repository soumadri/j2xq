package com.j2xq.type;

import com.j2xq.exception.TypeNotSupportedException;

public class TypeConverter {
	
	/**
	 * Maps Java types to XQuery types
	 * @param type The java type
	 * @return Respective XQuery type
	 * @throws TypeNotSupportedException
	 */
	public static String convertJavaTypeToXQueryType(String type) throws TypeNotSupportedException{
		if (type == "boolean") {
			return "xs:boolean";
		} else if(type == "byte") {
			return "xs:hexBinary";
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
		} else if(type == "org.w3c.dom.Document") {
			return "document-node(element(*, xs:untyped))";
		} else if(type == "org.w3c.dom.DocumentFragment") {
			return "document-node(element(*, xs:untyped))";
		} else {
			throw new TypeNotSupportedException(type);
		}
	}
	
	/**
	 * Generates code for serialize all Java types to Java String before sending it to MarkLogic in serialized XML form
	 * @param type The XQuery type
	 * @param var Name of the variable to converted
	 * @return Code statement for XQuery to Java type conversion
	 * @throws TypeNotSupportedException
	 */
	public static String generateTypecastedVariable(String type, String var) throws TypeNotSupportedException{
		if (type == "boolean") {
			return "Boolean.toString("+var+")";
		} else if(type == "byte") {					
			return "Hex.encodeHexString("+var+")";
		} else if(type == "double") {
			return "Double.toString("+var+")";
		} else if(type == "float") {
			return "Float.toString("+var+")";
		} else if(type == "int") {
			return "Integer.toString("+var+")";
		} else if(type == "long") {
			return "Long.toString("+var+")";
		} else if(type == "short") {
			return "Short.toString("+var+")";
		} else if(type == "java.lang.Boolean") {
			return var+".toString()";
		} else if(type == "java.lang.Byte") {
			return var+".toString()";
		} else if(type == "java.lang.Float") {
			return var+".toString()";
		} else if(type == "java.lang.Double") {
			return var+".toString()";
		} else if(type == "java.lang.Integer") {
			return var+".toString()";
		} else if(type == "java.long.Long") {
			return var+".toString()";
		} else if(type == "java.lang.Short") {
			return var+".toString()";
		} else if(type == "java.lang.String") {
			return var;
		} else if(type == "java.math.BigDecimal") {
			return var+".toString()";
		} else if(type == "java.math.BigInteger") {
			return var+".toString()";
		} else if(type == "org.w3c.dom.Document") {
			return "XMLUtils.toString("+var+")";
		} else if(type == "org.w3c.dom.DocumentFragment") {
			return "XMLUtils.toString("+var+")";
		} else {
			throw new TypeNotSupportedException(type);
		}
	}
	
	/**
	 * Generates code for typecasting/converting the values retrned from MarkLogic back to Java types
	 * @param type The XQuery type
	 * @return Code to cast/convert the value to Java type
	 * @throws TypeNotSupportedException
	 */
	public static String generateReturnTypecastedVariable(String type) throws TypeNotSupportedException{
		if (type == "boolean") {
			return "Boolean.valueOf(valueFromServer.asString())";
		} else if(type == "byte") {
			return "Hex.decodeHex(valueFromServer.asString().toCharArray())";
		} else if(type == "double") {
			return "Double.valueOf(valueFromServer.asString())";
		} else if(type == "float") {
			return "Float.valueOf(valueFromServer.asString())";
		} else if(type == "int") {
			return "Integer.valueOf(valueFromServer.asString())";
		} else if(type == "long") {
			return "Long.valueOf(valueFromServer.asString())";
		} else if(type == "short") {
			return "Short.valueOf(valueFromServer.asString())";
		} else if(type == "java.lang.Boolean") {
			return "new Boolean(valueFromServer.asString())";
		} else if(type == "java.lang.Byte") {
			return "new Byte(valueFromServer.asString())";
		} else if(type == "java.lang.Float") {
			return "new Float(valueFromServer.asString())";
		} else if(type == "java.lang.Double") {
			return "new Double(valueFromServer.asString())";
		} else if(type == "java.lang.Integer") {
			return "new Integer(valueFromServer.asString())";
		} else if(type == "java.long.Long") {
			return "new Long(valueFromServer.asString())";
		} else if(type == "java.lang.Short") {
			return "new Short(valueFromServer.asString())";
		} else if(type == "java.lang.String") {
			return "valueFromServer.asString()";
		} else if(type == "java.math.BigDecimal") {
			return "new java.math.BigDecimal(valueFromServer.asString())";
		} else if(type == "java.math.BigInteger") {
			return "new java.math.BigInteger(valueFromServer.asString())";
		} else if(type == "org.w3c.dom.Document") {
			return "XMLUtils.fromString(valueFromServer.asString())";
		} else if(type == "org.w3c.dom.DocumentFragment") {
			return "XMLUtils.fromString(valueFromServer.asString())";
		} else {
			throw new TypeNotSupportedException(type);
		}
	}
}
