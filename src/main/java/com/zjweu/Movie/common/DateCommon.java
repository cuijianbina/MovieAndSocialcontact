package com.zjweu.Movie.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 获取时间
 * 
 * @author Administrator
 *
 */
public class DateCommon {

	/**
	 * Description:获得当前系统时间
	 *
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月28日 上午11:32:30
	 * @version 1.0
	 */
	public static Date getTime() throws Exception {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(sdf.format(date));
	}

	/**
	 * Description:获得当前时间的7天前
	 *
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月28日 上午11:39:16
	 * @version 1.0
	 */
	public static Date getDayServenBefore() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		return calendar.getTime();
	}

}
