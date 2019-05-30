package com.zjweu.Movie.util;

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

public class MD5Util {
  public static String encode(String str) throws Exception{
  	MessageDigest  md5=MessageDigest.getInstance("MD5");
  	byte[] dest=md5.digest(str.getBytes());
  	BASE64Encoder base64=new BASE64Encoder();
  	return base64.encode(dest);
  }
  public static void main(String[] args) {
		try {
			System.out.println(encode("1234"));
			//System.out.println(encode("123456789"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
