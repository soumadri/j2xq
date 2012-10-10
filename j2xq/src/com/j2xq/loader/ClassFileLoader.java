package com.j2xq.loader;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.j2xq.util.OSDetector;

public class ClassFileLoader {
	static final Logger logger = Logger.getLogger(ClassFileLoader.class);
	
	public static Class<?>[] loadClasses(String baseDir) throws ClassNotFoundException, IOException{				
		File folder = new File(baseDir);
		File[] files = folder.listFiles(new ClassFileFilter());
		
		Class<?>[] classes = new Class[files.length];
		
		int i = 0;
		for (File file : files) {
			String path = file.getPath();
			String name = file.getName().substring(0, file.getName().indexOf('.'));
			
			//Replace \ with /, if OS is Windows
			if(OSDetector.isWindows()){
				path = "file:"+path.replace("\\", "/");
			}
			
			classes[i++] = new CustomClassLoader().loadClass(name, path);
			
			//Delete the class file
			file.delete();
		}
		
		return classes;
	}
	
	public static Class<?>[] loadClasses(String baseDir, String relFilePath) throws ClassNotFoundException, IOException{
		String relDir = "";
		
		if(OSDetector.isWindows()){
			relDir = relFilePath.substring(0, relFilePath.lastIndexOf('\\'));
		}else{
			relDir = relFilePath.substring(0, relFilePath.lastIndexOf('/'));
		}
		
		File folder = new File(baseDir + OSDetector.getPathSeperator() + relDir);
		
		File[] files = folder.listFiles(new ClassFileFilter());
		
		Class<?>[] classes = new Class[files.length];
		
		int i = 0;
		for (File file : files) {
			String path = file.getPath();
			String name = relFilePath.substring(0, relFilePath.indexOf('.')).replaceAll(OSDetector.getPathSeperatorEscaped(), ".");

			//Replace \ with /, if OS is Windows
			if(OSDetector.isWindows()){
				path = "file:"+path.replace("\\", "/");
			}
			
			classes[i++] = new CustomClassLoader().loadClass(name, path);
			
			//Delete the class file
			file.delete();
		}
		
		return classes;
	}
}
