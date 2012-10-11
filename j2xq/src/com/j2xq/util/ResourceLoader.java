package com.j2xq.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.j2xq.main.Main;

/**
 * Resource loader
 * @author soumadri
 *
 */
public class ResourceLoader {
	static final String newLine = System.getProperty("line.separator");
	
	/**
	 * Returns the content of the resource in the bundle
	 * @param bundle The fully qualified name of the resource
	 * @return Content of the specified resource
	 * @throws IOException
	 */
	public static String readAsText(String bundle) throws IOException{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream(bundle)));
		String content = "" , tmpcontent = "";
		
		while ((tmpcontent = bufferedReader.readLine()) != null) //loop through each line
        {              
			content += tmpcontent + newLine; 
        }
		
		return content;
	}
}
