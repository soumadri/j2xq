package com.j2xq.util;

public class OSDetector {
	
	/**
	 * Returns the filesystem path separator
	 * @return The filesystem path separator
	 */
	public static String getPathSeperator(){
		return OSDetector.isWindows()?"\\":"/";
	}
	
	/**
	 * Returns and regular expression escaped filesystem path separator
	 * @return The regular expression escaped filesystem path separator
	 */
	public static String getPathSeperatorEscaped(){
		return OSDetector.isWindows()?"\\\\":"/";
	}
	
	/**
	 * Checks the OS is Windows
	 * @return true if Windows, false otherwise
	 */
	public static boolean isWindows() {
		 
		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);
 
	}
	
	/**
	 * Checks the OS is Mac
	 * @return true if Mac, false otherwise
	 */
	public static boolean isMac() {
 
		String os = System.getProperty("os.name").toLowerCase();
		// Mac
		return (os.indexOf("mac") >= 0);
 
	}
	
	/**
	 * Checks the OS is UNIX
	 * @return true if UNIX, false otherwise
	 */
	public static boolean isUnix() {
 
		String os = System.getProperty("os.name").toLowerCase();
		// linux or unix
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
 
	}
	
	/**
	 * Checks the OS is Solaris
	 * @return true if Solaris, false otherwise
	 */
	public static boolean isSolaris() {
 
		String os = System.getProperty("os.name").toLowerCase();
		// Solaris
		return (os.indexOf("sunos") >= 0);
 
	}
}
