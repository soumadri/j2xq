package com.j2xq.loader;

import java.io.File;
import java.io.IOException;

import com.j2xq.util.OSDetector;

public class ClassFileLoader {
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
}
