package com.j2xq.generator;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.j2xq.annotation.J2XQ;
import com.j2xq.exception.TypeNotSupportedException;
import com.j2xq.type.TypeConverter;
import com.j2xq.type.TypeUtils;
import com.j2xq.util.FSUtil;
import com.j2xq.util.OSDetector;

/**
 * Responsible to generate the XQuery stubs
 * @author soumadri
 *
 */
public class XQueryStubGenerator {
	static final String newLine = System.getProperty("line.separator");
	
	/**
	 * Generates the prolog for the XQuery bridge (main module) module 
	 * @param class1 The class object (interface) for which the import statement has to be generated
	 * @param ns Optional namespace the module belongs to. Empty string will result in 'local' namespace
	 * @return The import module statement
	 */
	public static String generateProlog(Class<?> class1,String ns){
		String tmpName = MethodConverter.convertName(class1.getSimpleName());
		String nsPrefix = getNamespacePrefix(class1).equals("")?(tmpName.startsWith("-")?tmpName.substring(1) : tmpName):getNamespacePrefix(class1);
		String fname = tmpName.startsWith("-")?tmpName.substring(1) : tmpName;
		nsPrefix = nsPrefix.equals("")?"local":nsPrefix;		
		
		String op = "import module namespace " + nsPrefix + " = \"" + ns + "\" at \"" + fname + ".xqy\";" + newLine;
		
		return op;
	}
	
	/**
	 * Generates empty function definition
	 * @param method The Method object representing the method declared in the interface
	 * @param nsPrefix Optional namespace prefix the function belongs to. Empty string specifies 'local' namespace prefix
	 * @return The empty function definition
	 * @throws TypeNotSupportedException
	 */
	private static String generateFunction(Method method,String nsPrefix) throws TypeNotSupportedException{
		String funcPre = nsPrefix.equals("")?"declare function local:":"declare function "+nsPrefix+":";
		String signature = "";
			
		if(getMethodName(method).equals(""))
			signature += funcPre + MethodConverter.convertName(method.getName()) + "(";
		else
			signature += funcPre + getMethodName(method) + "(";			 
		
		signature += MethodConverter.convertParameter(method);		
		
		signature += ")";
		
		if(method.getReturnType().getName() != "void"){
			Class<?> class1 = method.getReturnType();			
			signature += " as " + TypeConverter.convertJavaTypeToXQueryType(TypeUtils.resolveType(class1));
		}
		
		signature += "\n{\n\"\"\n};\n";
		
		return signature;
	}
	
	/**
	 * Returns the XQuery equivalent function name
	 * @param method
	 * @return
	 */
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
	
	/**
	 * Generates the XQuery stub library modules
	 * @param dClass The Class object representing the input interface
	 * @param dir The directory to write the files to
	 * @return The namespace of the module so that it can be included in the main module's prolog
	 * @throws IOException
	 * @throws TypeNotSupportedException
	 */
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
			contentToWrite += XQueryStubGenerator.generateFunction(method,nsPrefix)+"\n";
		}
		
		FSUtil.writeToFile(path,contentToWrite);
		
		return ns;
	}
	
	/**
	 * Gets the namespace from the annotation in the input interface (if provided)
	 * @param aClass Class object for input interface
	 * @return The namespace specified in the annotation (if provided, "" otherwise)
	 */
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
	
	/**
	 * Gets the namespace prefix from the annotation in the input interface (if provided)
	 * @param aClass Class object for input interface
	 * @return The namespace prefix specified in the annotation (if provided, "" otherwise)
	 */
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
	
	/**
	 * Gets the name of the XQuery function to be generated which would be called from the generated Java code
	 * @param method Method object representing the Java method declared in the input interface
	 * @return The name of the function specified in the annotation
	 */
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
