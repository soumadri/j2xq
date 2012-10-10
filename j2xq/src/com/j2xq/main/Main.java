package com.j2xq.main;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.j2xq.exception.NotImplementedException;
import com.j2xq.exception.TypeNotSupportedException;
import com.j2xq.generator.CodeGenerator;
import com.j2xq.generator.StubGenerator;
import com.j2xq.loader.ClassFileLoader;
import com.j2xq.util.FSUtil;
import com.j2xq.util.OSDetector;
import com.j2xq.util.ResourceLoader;

public class Main {
	static final Logger logger = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		try {
			//Cleanup the output dir
			logger.debug("Cleaning output directories");
			FSUtil.cleanupOpDir();
			
			//Load the classes from disk
			logger.debug("Loading classes from filesystem");
			Class<?>[] classes = ClassFileLoader.loadClasses(System.getProperty("user.dir"),args[0]);
			
			//Code output dir
			logger.debug("Generating XQuery stubs");
			String dir = System.getProperty("user.dir")+OSDetector.getPathSeperator()+"output"+OSDetector.getPathSeperator()+"xq";
			
			//Create the directories if it doesn't exist
			logger.debug("Creating directories for XQuery stubs");
			File fDir = new File(dir);			
			if(!fDir.exists())
				fDir.mkdirs();
			
			String prolog = "";
						
			//Create stubs for each interface
			logger.debug("Generating XQuery prologs");
			for (Class<?> dClass : classes) {
				prolog += StubGenerator.generateProlog(dClass, StubGenerator.generateStub(dClass, dir));				
			}						
			
			//Create main module for XQY
			logger.debug("Generating XQuery main module");
			String mainXQFile = dir + OSDetector.getPathSeperator() + "xq2j-main.xqy";
			
			//Write the rolog to the main xqy
			logger.debug("Writing XQuery main module to filesystem");
			FSUtil.writeToFile(mainXQFile, prolog);
						
			FSUtil.writeToFile(mainXQFile, ResourceLoader.readAsText("xqmain.template"));
			
			
			//Code output dir
			logger.debug("Creating directories for Java classes");
			String dir1 = System.getProperty("user.dir")+OSDetector.getPathSeperator()+"output"+OSDetector.getPathSeperator()+"j";
			
			//Create the directories if it doesn't exist
			File fDir1 = new File(dir1);			
			if(!fDir1.exists())
				fDir1.mkdirs();
			
			//Create concrete class for each interface
			logger.debug("Creating Java code");
			for (Class<?> dClass : classes) {
				CodeGenerator.generateCode(dClass, dir1);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TypeNotSupportedException e) {
			e.printStackTrace();
		} catch (NotImplementedException e) {
			e.printStackTrace();
		}
	}

}
