package com.j2xq.generator;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.j2xq.exception.NotImplementedException;
import com.j2xq.exception.TypeNotSupportedException;
import com.j2xq.type.TypeConverter;
import com.j2xq.type.TypeUtils;
import com.j2xq.util.FSUtil;
import com.j2xq.util.ResourceLoader;

/**
 * Generates Java concrete classes implementing the input interface
 * @author soumadri
 *
 */
public class JavaCodeGenerator {
	static final Logger logger = Logger.getLogger(JavaCodeGenerator.class);	
	static final String newLine = System.getProperty("line.separator");
	
	/**
	 * Generates the serialized XML for a function call
	 * @param method Method object representing the method declared in the input interface
	 * @return Serialized XML for the function call
	 * @throws TypeNotSupportedException
	 */
	public static String generateParamValueList(Method method) throws TypeNotSupportedException{
		Class<?>[] classes = method.getParameterTypes();
		ArrayList<MethodParameter> arr = new ArrayList<MethodParameter>();
		int i=1;
		for (Class<?> class1 : classes) {
			MethodParameter p = new MethodParameter();
			String typeName = class1.getName();
			
			//Check whether its an array type. E.g. byte[]
			typeName = (typeName.startsWith("["))?class1.getComponentType().getName():class1.getName();
			p.setType(TypeConverter.convertJavaTypeToXQueryType(typeName));			
			p.setValue(TypeConverter.generateTypecastedVariable(typeName, "param"+(i++)));
			arr.add(p);
		}
		
		return toXML(XQueryStubGenerator.getFunctionName(method),arr);
	}
	
	/**
	 * Serializes an XQuery function call details into an XML based format
	 * @param funcName Name of the XQuery function to be called
	 * @param params List function parameter of type MethodParameter
	 * @return Serialized XML representing the function call
	 */
	public static String toXML(String funcName, ArrayList<MethodParameter> params){
		String op = "\t\t\tString params = \"<method name=\\"+"\""+funcName+"\\"+"\"><params>";
		
		for (MethodParameter param : params) {
			op += "<param type=\\"+"\""+param.getType()+"\\"+"\">\"+"+param.getValue()+"+\"</param>";
		}
		
		op += "</params></method>\";";
		
		return op;
	}
	
	/**
	 * Returns the simple class name from the fully qualified class name
	 * @param absClassName The fully qualified class name
	 * @return The simple class name
	 */
	public static String getClassName(String absClassName){
		if(absClassName.indexOf('.') > 0){			
			return absClassName.substring(absClassName.lastIndexOf('.')+1, absClassName.length());
		}else{
			return absClassName;
		}
	}
	
	/**
	 * Returns the package from the fully qualified class name
	 * @param absClassName The fully qualified class name
	 * @return The package for the specified class. Returns "", if the class doesn't belong to any package
	 */
	public static String getPackage(String absClassName){
		if(absClassName.indexOf('.') > 0){			
			return absClassName.substring(0, absClassName.lastIndexOf('.'));
		}else{
			return "";
		}
	}
	
	/**
	 * Generate the code for the concrete class
	 * @param dClass
	 * @param dir
	 * @throws IOException
	 * @throws TypeNotSupportedException
	 * @throws NotImplementedException
	 */
	public static void generateCode(Class<?> dClass, String dir) throws IOException, TypeNotSupportedException, NotImplementedException{						
		String fname = dClass.getName()+"Impl";			
			
		String path = FSUtil.convertPackageToPath(dir, fname) + ".java";
		
		Method methods[] = dClass.getDeclaredMethods();
		
		String contentToWrite = ResourceLoader.readAsText("licenseJava.template");
		
		if(fname.indexOf('.') > 0){
			contentToWrite += "package " + getPackage(fname) + ";" + newLine;
		}
		contentToWrite += ResourceLoader.readAsText("classImports.template") + addTypeImports(methods);//
				
		contentToWrite += "public class " + getClassName(dClass.getName()) + "Impl implements " + getClassName(dClass.getName()) + " {" + newLine;
		
		contentToWrite += ResourceLoader.readAsText("classBody.template").replace("{{%1}}", getClassName(fname));
		
		for (Method method : methods) {
			contentToWrite += generateMethod(method);			
		}		
		contentToWrite += newLine + "}";
		
		FSUtil.writeToFile(path, contentToWrite);
	}
	
	/**
	 * Generates type imports in the concrete classes
	 * @param methods
	 * @return Java import statements for the referenced types from the code
	 */
	public static String addTypeImports(Method methods[]){
		String imports = "";
		for (Method method : methods) {
			Class<?>[] classes = method.getExceptionTypes();
			
			for (Class<?> class1 : classes) {
				if(imports.indexOf(class1.getName()) == -1 && class1.getName().indexOf('.') > 0 && !class1.getName().equals("com.marklogic.xcc.exceptions.RequestException")){
					imports += "import " + class1.getName() + ";" + newLine;					
				}
			}
			
			classes = method.getParameterTypes();
			for (Class<?> class1 : classes) {
				String type = class1.getName();
				
				if(type.equals("[B")){ //Byte array					
					if(imports.indexOf("import org.apache.commons.codec.binary.Hex;") == -1)
						imports += "import org.apache.commons.codec.binary.Hex;" + newLine;
				} else if(type.startsWith("org.w3c.dom.")){
					if(imports.indexOf(type) == -1 && type.indexOf('.') > 0)
						imports += "import " + type + ";" + newLine + "import com.j2xq.util.XMLUtils;" + newLine;
				} else if(!type.startsWith("java.lang.")){					
					if(imports.indexOf(type) == -1 && type.indexOf('.') > 0)
						imports += "import " + type + ";" + newLine;
				}  				
			}
			
			Class<?> class1 = method.getReturnType();
			String type = class1.getName();
			
			if(type.equals("[B")){ //Byte array
				if(imports.indexOf("import org.apache.commons.codec.binary.Hex;") == -1)
					imports += "import org.apache.commons.codec.binary.Hex;" + newLine;					
			} else if(type.startsWith("org.w3c.dom.")){
				if(imports.indexOf(type) == -1)
					imports += "import " + type + ";" + newLine + "import com.j2xq.util.XMLUtils;" + newLine;
			} else if(!type.startsWith("java.lang.")){
				if(imports.indexOf(type) == -1 && class1.getName().indexOf('.') > 0)
					imports += "import " + type + ";" + newLine;
			}			
		}
		
		return imports + newLine + newLine;
	}
	
	/**
	 * Generates method definition in the concrete class
	 * @param method The method object representing the method in the input interface
	 * @return Java method definitions
	 * @throws TypeNotSupportedException
	 * @throws IOException
	 */
	public static String generateMethod(Method method) throws TypeNotSupportedException, IOException{
		Class<?> class1 = method.getReturnType();		
		
		String op = "\t" + getModifier(method.getModifiers()) + " " + method.getReturnType().getSimpleName() + " " + method.getName() + generateArgumentList(method); 
				
		op += " {"+ newLine +"\t\ttry{" + newLine;		
		op += generateParamValueList(method);
		op += ResourceLoader.readAsText("methodBody.template");
		if(!method.getReturnType().getName().equals("void")){
			op += "\t\t\tXdmItem valueFromServer = resultSequence.itemAt(0);" + newLine;
			op += "\t\t\treturn " + TypeConverter.generateReturnTypecastedVariable(TypeUtils.resolveType(class1)) + ";";
		}
		op += newLine + "\t\t}" + newLine + "\t\tcatch(Exception e)" + newLine + "\t\t{" + newLine + "\t\t\tthrow new GenericException(e);" + newLine + "\t\t}";
		op += newLine +  "\t}" + newLine + newLine;
		
		return op;
	}
	
	/**
	 * Generates comma separated method parameter list
	 * @param method The method object representing the method in the input interface
	 * @return Comma separated method parameter list 
	 */
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

	/**
	 * Returns java modifier keywords according to the modifier found via reflection
	 * @param modifier The modifier found via reflection
	 * @return The java keyword for that modifier
	 */
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
