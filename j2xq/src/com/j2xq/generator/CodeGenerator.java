package com.j2xq.generator;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.j2xq.exception.TypeNotSupportedException;
import com.j2xq.util.FSUtil;
import com.j2xq.util.OSDetector;

public class CodeGenerator {
	public static String generateParamValueList(Method method) throws TypeNotSupportedException{
		Class<?>[] classes = method.getParameterTypes();
		ArrayList<Param> arr = new ArrayList<Param>();
		int i=1;
		for (Class<?> class1 : classes) {
			Param p = new Param();
			p.setType(MethodConverter.convertType(class1.getName()));
			p.setValue(generateTypecastedVariable(class1.getName(), "param"+(i++)));
			arr.add(p);
		}
		
		return toXML(StubGenerator.getFunctionName(method),arr);
	}
	
	public static String toXML(String funcName, ArrayList<Param> params){
		String op = "\t\tString params = \"<method name=\\"+"\""+funcName+"\\"+"\">";
		
		for (Param param : params) {
			op += "<param type=\\"+"\""+param.getType()+"\\"+"\">\"+"+param.getValue()+"+\"</param>";
		}
		
		op += "</method>\";";
		
		return op;
	}
	
	public static void generateCode(Class<?> dClass, String dir) throws IOException, TypeNotSupportedException{
		
		String fname = dClass.getName()+"Impl";		
			
		String path = dir + OSDetector.getPathSeperator() + fname + ".java";
		
		Method methods[] = dClass.getDeclaredMethods();
		
		String contentToWrite = "public class " + dClass.getName()+"Impl implements "+dClass.getName() + " {\n";
		for (Method method : methods) {
			contentToWrite += generateMethod(method);			
		}		
		contentToWrite += "\n}";
		
		FSUtil.writeToFile(path, contentToWrite);
	}
	
	public static String generateMethod(Method method) throws TypeNotSupportedException{
		String op = "\t" + getModifier(method.getModifiers()) + " " + method.getReturnType().getSimpleName() + " " + method.getName() + generateArgumentList(method) + "{\n"; 
		
		/*int i=1;
		Class<?>[] types = method.getParameterTypes();
		for (Class<?> type : types) {
			op += generateTypecastedVariable(type.getName(), "param"+(i++));
		}*/
		
		op += generateParamValueList(method);
		
		op += "\n\t}\n\n";
		
		return op;
	}
	
	public static String generateArgumentList(Method method){
		Class<?>[] classes = method.getParameterTypes();
		
		String op = "(";
		int i=1;
		for (Class<?> class1 : classes) {
			op += class1.getSimpleName() + " param"+(i++);
			
			if(i<=classes.length)
				op += ", ";
		}
		op += ")";
		
		return op;
	}
	
	public static String generateTypecastedVariable(String type, String var) throws TypeNotSupportedException{
		if (type == "boolean") {
			return "Boolean.toString("+var+")";
		} else if(type == "byte") {
			return "";	//NEED TO CONVERT
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
		} else {
			throw new TypeNotSupportedException(type);
		}
	}
	
	public static String getModifier(int modifier){
		switch (modifier) {
		case Modifier.ABSTRACT:
			return "abstract";
		case Modifier.FINAL:
			return "final";
		case Modifier.INTERFACE:
			return "interface";
		case Modifier.NATIVE:
			return "native";
		case Modifier.PRIVATE:
			return "private";
		case Modifier.PROTECTED:
			return "protected";
		case Modifier.PUBLIC:
			return "public";
		case Modifier.STATIC:
			return "static";
		case Modifier.STRICT:
			return "strict";
		case Modifier.SYNCHRONIZED:
			return "synchronized";
		case Modifier.TRANSIENT:
			return "transient";
		case Modifier.VOLATILE:
			return "volatile";

		default:
			return "public";			
		}
	}
}
