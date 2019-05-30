package com.zjweu.Movie.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
	private static boolean processFLV(String inputFile, String outputFile) { 
//	    if (!checkfile(inputFile)) { 
//	      System.out.println(inputFile + " is not file"); 
//	      return false; 
//	    } 
	    List<String> commend = new ArrayList<String>(); 
	      
	    commend.add("F:\\ffmpeg-20190507-e25bddf-win64-static\\bin"); 
	    commend.add("-i"); 
	    commend.add(inputFile); 
	    commend.add("-ab"); 
	    commend.add("128"); 
	    commend.add("-acodec"); 
	    commend.add("libmp3lame"); 
	    commend.add("-ac"); 
	    commend.add("1"); 
	    commend.add("-ar"); 
	    commend.add("22050"); 
	    commend.add("-r"); 
	    commend.add("29.97"); 
	    //高品质  
	    commend.add("-qscale"); 
	    commend.add("6"); 
	    //低品质 
	//   commend.add("-b"); 
	//   commend.add("512"); 
	    commend.add("-y"); 
	      
	    commend.add(outputFile); 
	    StringBuffer test = new StringBuffer(); 
	    for (int i = 0; i < commend.size(); i++) { 
	      test.append(commend.get(i) + " "); 
	    } 
	  
	    System.out.println(test); 
	  
	    try { 
	      ProcessBuilder builder = new ProcessBuilder(); 
	      builder.command(commend); 
	      builder.start(); 
	      return true; 
	    } catch (Exception e) { 
	      e.printStackTrace(); 
	      return false; 
	    } 
	  } 
	
	public static void main(String[] args) {
        String rootPath="E:\\video\\24.02_多线程(死锁问题概述和使用).avi";
        String file = "E:\\video\\155531738655790.01796004902738.mp4";
        processFLV(rootPath,file);
//      convertToFlv(file);
        System.out.println(rootPath);
    }
}
