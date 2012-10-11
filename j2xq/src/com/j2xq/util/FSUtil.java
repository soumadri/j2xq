package com.j2xq.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;

/**
 * Filesystem utility methods
 * @author soumadri
 *
 */
public class FSUtil {
	static final Logger logger = Logger.getLogger(FSUtil.class);
	
	/**
	 * Writes content to the specified file
	 * @param fPath	The path to the file
	 * @param content The content of the file
	 * @throws IOException
	 */
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
	
	/**
	 * Convert a package structure to a filesystem directory structure
	 * @param dir The base directory
	 * @param pkg The fully qualified class name
	 * @return Returns the absolute path to the .java file where the generated code will be written to
	 */
	public static String convertPackageToPath(String dir, String pkg){	
		
		String path = dir + OSDetector.getPathSeperator();					
		String dirPath = pkg.substring(0, pkg.lastIndexOf('.'));
		
		dirPath = dirPath.replaceAll("\\.", OSDetector.getPathSeperatorEscaped());		
		
		path += dirPath;
		
		File fDir = new File(path);			
		if(!fDir.exists()){			
			fDir.mkdirs();
		}
		
		return path + OSDetector.getPathSeperatorEscaped() + pkg.substring(pkg.lastIndexOf('.')+1, pkg.length());		
	}
	
	/**
	 * Cleans up the output directory
	 */
	public static void cleanupOpDir(){
		String opDir = System.getProperty("user.dir")+OSDetector.getPathSeperator()+"output";
		File f = new File(opDir);
		delete(f);
	}
	
	/**
	 * Delete file/files recursively
	 * @param f The file or directory to be deleted
	 */
	private static void delete(File f){
		if(f.isDirectory()){
			for (File c : f.listFiles())
			      delete(c);
		}else{
			f.delete();
		}
	}
	
	public static void createDirectories(String dir){
		File fDir = new File(dir);			
		if(!fDir.exists())
			fDir.mkdirs();
	}
	
	public static String getXQueryOutputDir(){
		return System.getProperty("user.dir")+OSDetector.getPathSeperator()+"output"+OSDetector.getPathSeperator()+"xq";
	}
	
	public static String getJavaOutputDir(){		
		return System.getProperty("user.dir")+OSDetector.getPathSeperator()+"output"+OSDetector.getPathSeperator()+"j";
	}
}
