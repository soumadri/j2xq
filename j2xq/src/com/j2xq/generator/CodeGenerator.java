package com.j2xq.generator;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.j2xq.exception.NotImplementedException;
import com.j2xq.exception.TypeNotSupportedException;
import com.j2xq.loader.ClassFileLoader;
import com.j2xq.util.FSUtil;
import com.j2xq.util.OSDetector;
import com.j2xq.util.ResourceLoader;

/**
 * Generates Java and XQuery code
 * @author soumadri
 *
 */
public class CodeGenerator {
	static final Logger logger = Logger.getLogger(CodeGenerator.class);
	
	/**
	 * Generates XQuery stubs and Java code and writes to the specified directory
	 * @param fPath Directory path to write the files to
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws TypeNotSupportedException
	 * @throws NotImplementedException
	 */
	public static void generateCode(String fPath) throws ClassNotFoundException, IOException, TypeNotSupportedException, NotImplementedException{
		//Cleanup the output directory
		logger.debug("Cleaning output directories");
		FSUtil.cleanupOpDir();
		
		//Load the classes from disk
		logger.debug("Loading classes from filesystem");
		Class<?>[] classes = ClassFileLoader.loadClasses(System.getProperty("user.dir"),fPath);
		
		//Code output directory
		logger.debug("Generating XQuery stubs");
		String xqDir = FSUtil.getXQueryOutputDir();
		
		//Create the directories if it doesn't exist
		logger.debug("Creating directories for XQuery stubs");
		FSUtil.createDirectories(xqDir);
		
		//Generate the XQuery modules
		generateXQYModules(classes, xqDir);
		
		//Java code output directory
		logger.debug("Creating directories for Java classes");
		String javaDir = FSUtil.getJavaOutputDir();
		
		//Generate the Java concrete classes
		generateJavaConcreteClasses(classes, javaDir);
		
	}
	
	/**
	 * Generates XQuery stubs for the input Class objects and saves files to disk
	 * @param classes Class objects
	 * @param dir Directory to write the files to
	 * @throws IOException
	 * @throws TypeNotSupportedException
	 */
	private static void generateXQYModules(Class<?>[] classes, String dir) throws IOException, TypeNotSupportedException{
		String prolog = "";
		
		//Create stubs for each interface
		logger.debug("Generating XQuery prologs");
		for (Class<?> dClass : classes) {
			prolog += XQueryStubGenerator.generateProlog(dClass, XQueryStubGenerator.generateStub(dClass, dir));				
		}						
		
		//Create main module for XQY
		logger.debug("Generating XQuery main module");
		String mainXQFile = dir + OSDetector.getPathSeperator() + "xq2j-main.xqy";
		
		//Write the prolog to the main XQY
		logger.debug("Writing XQuery main module to filesystem");
		FSUtil.writeToFile(mainXQFile, prolog);
					
		FSUtil.writeToFile(mainXQFile, ResourceLoader.readAsText("xqmain.template"));
	}
	
	/**
	 * Generates the Java concrete classes
	 * @param classes Class objects
	 * @param javaDir Directory to write the files to
	 * @throws IOException
	 * @throws TypeNotSupportedException
	 * @throws NotImplementedException
	 */
	private static void generateJavaConcreteClasses(Class<?>[] classes, String javaDir) throws IOException, TypeNotSupportedException, NotImplementedException{				
		//Create the directories if it doesn't exist
		File fJavaDir = new File(javaDir);			
		if(!fJavaDir.exists())
			fJavaDir.mkdirs();
		
		//Create concrete class for each interface
		logger.debug("Creating Java code");
		for (Class<?> dClass : classes) {
			JavaCodeGenerator.generateCode(dClass, javaDir);
		}
	}
}
