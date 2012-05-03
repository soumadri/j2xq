package com.j2xq.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FSUtil {
	public static void writeToFile(String fPath, String content) throws IOException{
		File file = new File(fPath);
		Writer output = null;
		if(!file.exists()){			
			file.createNewFile();			
			output = new BufferedWriter(new FileWriter(file));			
		} else {			
			output = new BufferedWriter(new FileWriter(file,true));			
		}
		
		output.append(content);
		output.close();
	}
	
	public static void cleanupOpDir(){
		String opDir = System.getProperty("user.dir")+OSDetector.getPathSeperator()+"output";
		File f = new File(opDir);
		delete(f);
	}
	
	private static void delete(File f){
		if(f.isDirectory()){
			for (File c : f.listFiles())
			      delete(c);
		}else{
			f.delete();
		}
	}
}
