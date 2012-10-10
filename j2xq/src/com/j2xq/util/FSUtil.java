package com.j2xq.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;

public class FSUtil {
	static final Logger logger = Logger.getLogger(FSUtil.class);
	
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
	
	public static String convertPackageToPath(String dir, String pkg){	
		
		String path = dir + OSDetector.getPathSeperator();					
		String dirPath = pkg.substring(0, pkg.lastIndexOf('.'));
		
		dirPath = dirPath.replaceAll("\\.", OSDetector.getPathSeperatorEscaped());
		
		//logger.info("Package dir: " + dirPath);
		
		path += dirPath;
		
		File fDir = new File(path);			
		if(!fDir.exists()){			
			fDir.mkdirs();
			//logger.info("Directory created: " + path);
		}
		
		return path + OSDetector.getPathSeperatorEscaped() + pkg.substring(pkg.lastIndexOf('.')+1, pkg.length());		
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
