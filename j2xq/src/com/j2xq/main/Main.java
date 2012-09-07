package com.j2xq.main;

import java.io.File;
import java.io.IOException;

import com.j2xq.exception.NotImplementedException;
import com.j2xq.exception.TypeNotSupportedException;
import com.j2xq.generator.CodeGenerator;
import com.j2xq.generator.StubGenerator;
import com.j2xq.loader.ClassFileLoader;
import com.j2xq.util.FSUtil;
import com.j2xq.util.OSDetector;
import com.j2xq.util.ResourceLoader;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//Cleanup the output dir
			FSUtil.cleanupOpDir();
			
			//Load the classes from disk
			Class<?>[] classes = ClassFileLoader.loadClasses(System.getProperty("user.dir"));
			
			//Code output dir
			String dir = System.getProperty("user.dir")+OSDetector.getPathSeperator()+"output"+OSDetector.getPathSeperator()+"xq";
			
			//Create the directories if it doesn't exist
			File fDir = new File(dir);			
			if(!fDir.exists())
				fDir.mkdirs();
			
			String prolog = "";
			//Create stubs for each interface
			for (Class<?> dClass : classes) {
				prolog += StubGenerator.generateProlog(dClass, StubGenerator.generateStub(dClass, dir));				
			}						
			
			//Create main module for XQY
			String mainXQFile = dir + OSDetector.getPathSeperator() + "xq2j-main.xqy";
			
			//Write the rolog to the main xqy
			FSUtil.writeToFile(mainXQFile, prolog);
			
			/*BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream()));
			String content = "" , tmpcontent = "";
			
			while ((tmpcontent = bufferedReader.readLine()) != null) //loop through each line
            {              
				content += tmpcontent + "\n"; 
            }*/
			
			FSUtil.writeToFile(mainXQFile, ResourceLoader.readAsText("xqmain.template"));
			
			
			//Code output dir
			String dir1 = System.getProperty("user.dir")+OSDetector.getPathSeperator()+"output"+OSDetector.getPathSeperator()+"j";
			
			//Create the directories if it doesn't exist
			File fDir1 = new File(dir1);			
			if(!fDir1.exists())
				fDir1.mkdirs();
			
			//Create concrete class for each interface
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
