package com.j2xq.main;

import org.apache.log4j.Logger;

import com.j2xq.generator.CodeGenerator;

public class Main {
	static final Logger logger = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		try {
			if(args.length < 1){
				CodeGenerator.generateCode(args[0]);
			} else{
				System.out.println("Argument expected. Please provide the path to the interface.");
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
