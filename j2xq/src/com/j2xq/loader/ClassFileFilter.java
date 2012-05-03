package com.j2xq.loader;

import java.io.File;
import java.io.FilenameFilter;

public class ClassFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		if (name.endsWith(".class") || name.endsWith(".CLASS"))
        {
             return true;
        }
        return false;
	}

}
