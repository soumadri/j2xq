package com.j2xq.type;

public class TypeUtils {
	public static String resolveType(Class<?> class1){
		String typeName = class1.getName();
		
		//Check whether its an array type. E.g. byte[]
		typeName = (typeName.startsWith("["))?class1.getComponentType().getName():class1.getName();		
		return typeName;
	}
}
