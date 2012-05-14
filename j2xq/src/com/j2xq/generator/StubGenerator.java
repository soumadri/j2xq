package com.j2xq.generator;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.j2xq.annotation.J2XQ;
import com.j2xq.exception.TypeNotSupportedException;
import com.j2xq.util.FSUtil;
import com.j2xq.util.OSDetector;

public class StubGenerator {
	public static String generateProlog(Class<?> class1,String ns){
		String tmpName = MethodConverter.convertName(class1.getSimpleName());
		String nsPrefix = getNamespacePrefix(class1).equals("")?(tmpName.startsWith("-")?tmpName.substring(1) : tmpName):getNamespacePrefix(class1);
		nsPrefix = nsPrefix.equals("")?"local":nsPrefix;		
		
		String op = "import module namespace " + nsPrefix + " = \"" + ns + "\" at \"" + nsPrefix + ".xqy\";\n";
		
		return op;
	}
	
	public static String generateMethod(Method method,String nsPrefix) throws TypeNotSupportedException{
		String funcPre = nsPrefix.equals("")?"declare function local:":"declare function "+nsPrefix+":";
		String signature = "";
			
		if(getMethodName(method).equals(""))
			signature += funcPre + MethodConverter.convertName(method.getName()) + "(";
		else
			signature += funcPre + getMethodName(method) + "(";			 
		
		signature += MethodConverter.convertParameter(method);		
		
		signature += ")";
		
		if(method.getReturnType().getName() != "void")
			signature += " as " + MethodConverter.convertType(method.getReturnType().getName());
		
		signature += "\n{\n\"\"\n};\n";
		
		return signature;
	}
	
	public static String getFunctionName(Method method){
		Class<?> class1 = method.getDeclaringClass();
		String tmpName = MethodConverter.convertName(class1.getSimpleName());
		String nsPrefix = getNamespacePrefix(class1).equals("")?(tmpName.startsWith("-")?tmpName.substring(1) : tmpName):getNamespacePrefix(class1);
		nsPrefix = nsPrefix.equals("")?"local:":nsPrefix+":";
		
		if(getMethodName(method).equals(""))
			return nsPrefix+MethodConverter.convertName(method.getName());
		else
			return nsPrefix+getMethodName(method);
	}
	
	public static String generateStub(Class<?> dClass, String dir) throws IOException, TypeNotSupportedException{
		String fname = MethodConverter.convertName(dClass.getName());
		fname = fname.startsWith("-")?fname.substring(1) + ".xqy" :fname + ".xqy";
			
		String path = dir + OSDetector.getPathSeperator() + fname;
		
		Method methods[] = dClass.getMethods();
				
		String contentToWrite = "";
		String nsPrefix = "";
		String ns = "";
		if(!getNamespace(dClass).equals("")){
			String tmpName = MethodConverter.convertName(dClass.getName());
			nsPrefix = getNamespacePrefix(dClass).equals("")?(tmpName.startsWith("-")?tmpName.substring(1) : tmpName):getNamespacePrefix(dClass);
			ns = getNamespace(dClass);
			contentToWrite += "module namespace "+nsPrefix+" = \""+ns+"\";\n\n";
		}else{
			String tmpName = MethodConverter.convertName(dClass.getName());
			nsPrefix = getNamespacePrefix(dClass).equals("")?(tmpName.startsWith("-")?tmpName.substring(1) : tmpName):getNamespacePrefix(dClass);
			ns = "http://www.j2xq.com/xq-stub/" + nsPrefix;
			contentToWrite += "module namespace "+nsPrefix+" = \""+ns+"\";\n\n";
		}
		
		for (Method method : methods) {			
			contentToWrite += StubGenerator.generateMethod(method,nsPrefix)+"\n";
		}
		
		FSUtil.writeToFile(path,contentToWrite);
		
		return ns;
	}
	
	private static String getNamespace(Class<?> aClass){
		Annotation[] annotations = aClass.getAnnotations();
		
		for(Annotation annotation : annotations){
		    if(annotation instanceof J2XQ){
		        J2XQ annot = (J2XQ) annotation;		        
		        if(annot.name().equals("namespace"))
		        	return annot.value();		        
		    }
		}
		
		return "";
	}
	
	private static String getNamespacePrefix(Class<?> aClass){
		Annotation[] annotations = aClass.getAnnotations();
		
		for(Annotation annotation : annotations){
		    if(annotation instanceof J2XQ){
		        J2XQ annot = (J2XQ) annotation;		        
		        return annot.nsprefix();		        
		    }
		}
		
		return "";
	}
	
	private static String getMethodName(Method method){
		Annotation[] annotations = method.getAnnotations();
		
		for(Annotation annotation : annotations){
		    if(annotation instanceof J2XQ){
		        J2XQ annot = (J2XQ) annotation;		        
		        if(annot.name().equals("name"))
		        	return annot.value();		        
		    }
		}
		
		return "";
	}
}
