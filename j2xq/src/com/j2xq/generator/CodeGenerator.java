package com.j2xq.generator;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.j2xq.exception.NotImplementedException;
import com.j2xq.exception.TypeNotSupportedException;
import com.j2xq.util.FSUtil;
import com.j2xq.util.OSDetector;
import com.j2xq.util.ResourceLoader;
import com.j2xq.util.XMLUtils;

public class CodeGenerator {
	public static String generateParamValueList(Method method) throws TypeNotSupportedException{
		Class<?>[] classes = method.getParameterTypes();
		ArrayList<Param> arr = new ArrayList<Param>();
		int i=1;
		for (Class<?> class1 : classes) {
			Param p = new Param();
			p.setType(MethodConverter.convertType(class1.getName()));
			p.setValue(generateStringTypecastedVariable(class1.getName(), "param"+(i++)));
			arr.add(p);
		}
		
		return toXML(StubGenerator.getFunctionName(method),arr);
	}
	
	public static String toXML(String funcName, ArrayList<Param> params){
		String op = "\t\tString params = \"<method name=\\"+"\""+funcName+"\\"+"\"><params>";
		
		for (Param param : params) {
			op += "<param type=\\"+"\""+param.getType()+"\\"+"\">\"+"+param.getValue()+"+\"</param>";
		}
		
		op += "</params></method>\";";
		
		return op;
	}
	
	public static void generateCode(Class<?> dClass, String dir) throws IOException, TypeNotSupportedException, NotImplementedException{						
		String fname = dClass.getName()+"Impl";			
			
		String path = dir + OSDetector.getPathSeperator() + fname + ".java";
		
		Method methods[] = dClass.getDeclaredMethods();
		
		String contentToWrite = ResourceLoader.readAsText("classImports.template");
				
		/*if(!dClass.getPackage().getName().equals(""))			
			contentToWrite = "import " + dClass.getPackage().getName() + ";\n\npublic class " + dClass.getName()+"Impl implements "+dClass.getName() + " {\n";
		else*/
			contentToWrite += "public class " + dClass.getName()+"Impl implements "+dClass.getName() + " {\n";
		
		contentToWrite += ResourceLoader.readAsText("classBody.template").replace("{{%1}}", fname);
		
		for (Method method : methods) {
			contentToWrite += generateMethod(method);			
		}		
		contentToWrite += "\n}";
		
		FSUtil.writeToFile(path, contentToWrite);
	}
	
	public static String generateMethod(Method method) throws TypeNotSupportedException, IOException{
		String op = "\t" + getModifier(method.getModifiers()) + " " + method.getReturnType().getSimpleName() + " " + method.getName() + generateArgumentList(method); 
		
		//Check for 'throws' declarations
		if(method.getExceptionTypes().length > 0){
			op += " throws ";
			Class<?>[] classes = method.getExceptionTypes();
			
			int i=0;
			for (Class<?> class1 : classes) {
				op += class1.getSimpleName();
				if(i < classes.length-1) {
					op += ", ";
					i++;
				}
			}
		}
		
		op += " {\n";
		/*int i=1;
		Class<?>[] types = method.getParameterTypes();
		for (Class<?> type : types) {
			op += generateTypecastedVariable(type.getName(), "param"+(i++));
		}*/
		
		op += generateParamValueList(method);
		op += ResourceLoader.readAsText("methodBody.template");
		if(!method.getReturnType().getName().equals("void")){
			op += "\t\tXdmItem valueFromServer = resultSequence.itemAt(0);\n";
			op += "\t\treturn " + generateReturnTypecastedVariable(method.getReturnType().getName()) + ";";
		}
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
	
	public static String generateStringTypecastedVariable(String type, String var) throws TypeNotSupportedException{
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
		} else if(type == "org.w3c.dom.Document") {
			return "XMLUtils.toString("+var+")";
		} else if(type == "org.w3c.dom.DocumentFragment") {
			return "XMLUtils.toString("+var+")";
		} else {
			throw new TypeNotSupportedException(type);
		}
	}

	public static String generateReturnTypecastedVariable(String type) throws TypeNotSupportedException{
		if (type == "boolean") {
			return "Boolean.valueOf(valueFromServer.asString())";
		} else if(type == "byte") {
			return "";	//NEED TO CONVERT
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
			return "XMLUtils.fromString(valueFromServer.asInputStream())";
		} else if(type == "org.w3c.dom.DocumentFragment") {
			return "XMLUtils.fromString(valueFromServer.asInputStream())";
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
