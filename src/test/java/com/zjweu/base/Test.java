package com.zjweu.base;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import com.zjweu.Movie.common.CtlStatus;
import com.zjweu.Movie.util.MD5Util;

import net.sf.json.JSONObject;

public class Test {
	public static void main(String[] args) throws Exception {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(new Date());
//		System.out.println(calendar.getTime()+"before");
//		calendar.add(Calendar.DAY_OF_MONTH, -7);
//		System.out.println(calendar.getTime()+"end");
//		try{
//			File file = new File("D:/Documents/HBuilderProject/毕设前端/video/11.mp4");
//			System.out.println(file.exists());
//			file.delete();
//		}catch(Exception e){
//			System.out.println("异常了");
//		}
		System.out.println(new Date().getTime());
		System.out.println(Calendar.getInstance().getTimeInMillis());
	}
}
