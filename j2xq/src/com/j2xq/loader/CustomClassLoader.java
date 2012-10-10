package com.j2xq.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class CustomClassLoader extends ClassLoader {
	
	static final Logger logger = Logger.getLogger(CustomClassLoader.class);
	
    public Class<?> loadClass(String name, String path) throws ClassNotFoundException, IOException {                
            String url = path;
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();

            while(data != -1){
                buffer.write(data);
                data = input.read();
            }

            input.close();

            byte[] classData = buffer.toByteArray();           
            return defineClass(name,
                    classData, 0, classData.length);            
    }
}
