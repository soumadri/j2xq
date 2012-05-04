package com.j2xq.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.j2xq.main.Main;

public class ResourceLoader {
	public static String readAsText(String bundle) throws IOException{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream(bundle)));
		String content = "" , tmpcontent = "";
		
		while ((tmpcontent = bufferedReader.readLine()) != null) //loop through each line
        {              
			content += tmpcontent + "\n"; 
        }
		
		return content;
	}
}
