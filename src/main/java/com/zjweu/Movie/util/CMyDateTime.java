package com.zjweu.Movie.util;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @version 1.0
 */
/**
 * @Description: TODO(描述类的作用)<BR>
 * @version 1.0
 */
public class CMyDateTime implements Cloneable, Serializable {
	/**
	 * @Fields serialVersionUID : TODO <BR>
	 */
	private static final long serialVersionUID = -3303084442514086076L;

	private java.util.Date m_dtDate = null;

	private SimpleDateFormat m_dtFormater = null;
	public static final int FORMAT_DEFAULT = 0;
	public static final int FORMAT_LONG = 1;
	public static final int FORMAT_SHORT = 2;
	public static final String DEF_DATE_FORMAT_PRG = "yyyy-MM-dd";
	public static final String DEF_TIME_FORMAT_PRG = "HH:mm:ss";
	public static final String DEF_DATETIME_FORMAT_PRG = "yyyy-MM-dd HH:mm:ss";
	public static final String DEF_DATETIME_FORMAT_DB = "YYYY-MM-DD HH24:MI:SS";
	public static final int YEAR = 1;
	public static final int MONTH = 2;
	public static final int DAY = 3;
	public static final int HOUR = 4;
	public static final int MINUTE = 5;
	public static final int SECOND = 6;
	public static final int QUATER = 11;
	public static final int WEEK = 12;
	public static final int DAY_OF_MONTH = 13;
	public static final int WEEK_OF_MONTH = 14;
	public static final int DAY_OF_YEAR = 15;
	public static final int WEEK_OF_YEAR = 16;
	public static final long ADAY_MILLIS = 86400000L;
	public static final String[] MONTHS = { "January", "February", " March", " April", "May", "June", "July", "August",
			"September", "October", "November", "December" };

	public static final String[] WEEKS = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday" };

	public CMyDateTime() {
	}

	/**
	 * @Description: TODO(描述构造函数的作用)<BR>
	 * @param paramLong
	 */
	public CMyDateTime(long paramLong) {
		this.m_dtDate = new java.util.Date(paramLong);
	}

	/**
	 * @Description: TODO(描述方法的作用) <BR>
	 * @return
	 * @version 1.0
	 */
	public static CMyDateTime now() {
		CMyDateTime localCMyDateTime = new CMyDateTime();
		localCMyDateTime.setDateTimeWithCurrentTime();
		return localCMyDateTime;
	}

	public boolean isNull() {
		return this.m_dtDate == null;
	}

	public long getTimeInMillis() {
		return this.m_dtDate == null ? 0L : this.m_dtDate.getTime();
	}

	public static int getTimeZoneRawOffset() {
		TimeZone localTimeZone = TimeZone.getDefault();
		int i = localTimeZone.getRawOffset();
		return i;
	}

	/**
	 * @Description: 时间比较 <BR>
	 * @author liujian
	 * @date 2014-3-8 下午04:46:29
	 * @param paramDate
	 *            java.util.Date类型的日期参数
	 * @return 返回两者之间的时间差（毫秒数）
	 * @version 1.0
	 */
	public long compareTo(java.util.Date paramDate) {
		long l1 = this.m_dtDate == null ? 0L : this.m_dtDate.getTime();
		long l2 = paramDate == null ? 0L : paramDate.getTime();
		return l1 - l2;
	}

	/**
	 * @Description: 时间比较 <BR>
	 * @return 返回两者之间的时间差（毫秒数）
	 * @version 1.0
	 */
	public long compareTo(CMyDateTime paramCMyDateTime) {
		return compareTo(paramCMyDateTime.getDateTime());
	}

	/**
	 * @Description: 取与指定时间之间的（年/月/日/小时/分/秒/季度/周）差 <BR>
	 * @param paramInt
	 *            定义返回值的类型（CMyDateTime.YEAR等）
	 * @param paramDate
	 *            指定的时间对象：CMyDateTime
	 * @return
	 * @throws Exception
	 * @version 1.0
	 */
	public long dateDiff(int paramInt, CMyDateTime paramCMyDateTime) throws Exception {
		if (paramCMyDateTime == null) {
			throw new Exception("无效的日期时间对象参数(CMyDateTime.dateDiff(CMyDateTime))");
		}

		return dateDiff(paramInt, paramCMyDateTime.getDateTime());
	}

	/**
	 * @Description: 取与指定时间之间的（年/月/日/小时/分/秒/季度/周）差 <BR>
	 * @param paramInt
	 *            定义返回值的类型（CMyDateTime.YEAR等）
	 * @param paramDate
	 *            指定的时间对象：java.util.Date
	 * @return
	 * @throws Exception
	 * @version 1.0
	 */
	public long dateDiff(int paramInt, java.util.Date paramDate) throws Exception {
		if (paramDate == null) {
			throw new Exception("无效的日期时间参数（CMyDateTime.dateDiff(int,java.util.Date)）");
		}

		if (isNull()) {
			throw new Exception("日期时间为空（CMyDateTime.dateDiff(int,java.util.Date)）");
		}

		if (paramInt == 1)
			return dateDiff_year(paramDate);
		if (paramInt == 2) {
			return dateDiff_month(paramDate);
		}

		long l1 = this.m_dtDate == null ? 0L : this.m_dtDate.getTime();
		long l2 = paramDate == null ? 0L : paramDate.getTime();
		long l3 = (l1 - l2) / 1000L;

		switch (paramInt) {
		case 3:
			return l3 / 86400L;
		case 4:
			return l3 / 3600L;
		case 5:
			return l3 / 60L;
		case 6:
			return l3;
		case 11:
			return l3 / 86400L / 91L;
		case 12:
			return l3 / 86400L / 7L;
		case 7:
		case 8:
		case 9:
		case 10:
		}
		throw new Exception("参数无效(CMyDateTime.dateDiff(int,java.util.Date))");
	}

	/**
	 * @Description: 计算当前时间对象与指定时间的【年】差 <BR>
	 * @param paramDate
	 *            指定的时间对象
	 * @return 返回两者之间的差（年数）
	 * @version 1.0
	 */
	private long dateDiff_year(java.util.Date paramDate) {
		GregorianCalendar localGregorianCalendar = new GregorianCalendar();
		localGregorianCalendar.setTimeZone(TimeZone.getDefault());

		localGregorianCalendar.setTime(this.m_dtDate);
		int i = localGregorianCalendar.get(1);
		int k = localGregorianCalendar.get(2);

		localGregorianCalendar.setTime(paramDate);
		int j = localGregorianCalendar.get(1);
		int m = localGregorianCalendar.get(2);

		if (i == j)
			return 0L;
		if (i > j) {
			return i - j + (k >= m ? 0 : -1);
		}
		return i - j + (k > m ? 1 : 0);
	}

	/**
	 * @Description: 计算当前时间对象与指定时间的【月】差 <BR>
	 * @param paramDate
	 *            指定的时间对象
	 * @return 返回两者之间的差（月数）
	 * @version 1.0
	 */
	public long dateDiff_month(java.util.Date paramDate) {
		GregorianCalendar localGregorianCalendar = new GregorianCalendar();
		localGregorianCalendar.setTimeZone(TimeZone.getDefault());

		localGregorianCalendar.setTime(this.m_dtDate);
		int i = localGregorianCalendar.get(1) * 12 + localGregorianCalendar.get(2);
		int k = localGregorianCalendar.get(5);

		localGregorianCalendar.setTime(paramDate);
		int j = localGregorianCalendar.get(1) * 12 + localGregorianCalendar.get(2);
		int m = localGregorianCalendar.get(5);

		if (i == j)
			return 0L;
		if (i > j) {
			return i - j + (k < m ? -1 : 0);
		}
		return i - j + (k > m ? 1 : 0);
	}

	public int get(int paramInt) throws Exception {
		if (this.m_dtDate == null) {
			throw new Exception("日期时间为空（CMyDateTime.get）");
		}

		GregorianCalendar localGregorianCalendar = new GregorianCalendar();
		localGregorianCalendar.setTimeZone(TimeZone.getDefault());
		localGregorianCalendar.setTime(this.m_dtDate);
		switch (paramInt) {
		case 1:
			return localGregorianCalendar.get(1);
		case 2:
			return localGregorianCalendar.get(2) + 1;
		case 3:
			return localGregorianCalendar.get(5);
		case 4:
			return localGregorianCalendar.get(11);
		case 5:
			return localGregorianCalendar.get(12);
		case 6:
			return localGregorianCalendar.get(13);
		case 12:
			return localGregorianCalendar.get(7);
		case 13:
			return ((GregorianCalendar) localGregorianCalendar).getActualMaximum(5);
		case 14:
			return getWeekCountsOfMonth(true);
		case 15:
			return ((GregorianCalendar) localGregorianCalendar).getActualMaximum(6);
		case 16:
			return ((GregorianCalendar) localGregorianCalendar).getActualMaximum(3);
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		}
		throw new Exception("无效的日期时间域参数（CMyDateTime.get）");
	}

	/**
	 * @Description: 获取当前对象中的【年】的部分
	 * @return
	 * @throws Exception
	 * @version 1.0
	 */
	public int getYear() throws Exception {
		return get(1);
	}

	/**
	 * @Description: 获取当前对象中的【月】的部分
	 * @return
	 * @throws Exception
	 * @version 1.0
	 */
	public int getMonth() throws Exception {
		return get(2);
	}

	/**
	 * @Description: 获取当前对象中的【天】的部分
	 * @return
	 * @throws Exception
	 * @version 1.0
	 */
	public int getDay() throws Exception {
		return get(3);
	}

	/**
	 * @Description: 获取当前对象中的【时】的部分
	 * @return
	 * @throws Exception
	 * @version 1.0
	 */
	public int getHour() throws Exception {
		return get(4);
	}

	/**
	 * @Description: 获取当前对象中的【分】的部分
	 * @return
	 * @throws Exception
	 * @version 1.0
	 */
	public int getMinute() throws Exception {
		return get(5);
	}

	/**
	 * @Description: 获取当前对象中的【秒】的部分
	 * @return
	 * @throws Exception
	 * @version 1.0
	 */
	public int getSecond() throws Exception {
		return get(6);
	}

	/**
	 * @Description: 取当前日期是所在week的第几天 <BR>
	 * @return 当前日期是所在week的第几天(int)
	 * @throws Exception
	 * @version 1.0
	 */
	public int getDayOfWeek() throws Exception {
		return get(12);
	}

	/**
	 * @Description: 日期时间增/减函数 <BR>
	 * @param paramInt1
	 *            指定域（例如CMyDateTime.YEAR等）
	 * @param paramInt2
	 *            增加数目（负值为减）
	 * @return 返回当前对象本身
	 * @throws Exception
	 * @version 1.0
	 */
	public CMyDateTime dateAdd(int paramInt1, int paramInt2) throws Exception {
		if (this.m_dtDate == null) {
			throw new Exception("日期时间为空（CMyDateTime.dateAdd）");
		}

		int i = 0;
		switch (paramInt1) {
		case 1:
			i = 1;
			break;
		case 2:
			i = 2;
			break;
		case 12:
			i = 5;
			paramInt2 *= 7;
			break;
		case 3:
			i = 5;
			break;
		case 4:
			i = 10;
			break;
		case 5:
			i = 12;
			break;
		case 6:
			i = 13;
			break;
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		default:
			throw new Exception("无效的日期时间域参数（CMyDateTime.dateAdd）");
		}

		GregorianCalendar localGregorianCalendar = new GregorianCalendar();
		localGregorianCalendar.setTimeZone(TimeZone.getDefault());
		localGregorianCalendar.setTime(this.m_dtDate);
		localGregorianCalendar.set(i, localGregorianCalendar.get(i) + paramInt2);
		this.m_dtDate = localGregorianCalendar.getTime();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public synchronized Object clone() {
		CMyDateTime localCMyDateTime = new CMyDateTime();
		localCMyDateTime.m_dtDate = (this.m_dtDate == null ? null : (java.util.Date) this.m_dtDate.clone());

		localCMyDateTime.m_dtFormater = (this.m_dtFormater == null ? null
				: (SimpleDateFormat) this.m_dtFormater.clone());

		return localCMyDateTime;
	}

	public java.util.Date getDateTime() {
		return this.m_dtDate;
	}

	public String toString() {
		return toString("yyyy-MM-dd HH:mm:ss");
	}

	public String toString(String paramString) {
		if (this.m_dtDate == null)
			return null;
		try {
			return getDateTimeAsString(paramString);
		} catch (Exception localException) {
		}
		return null;
	}

	public String toString(String paramString1, String paramString2, String paramString3) {
		if (this.m_dtDate == null) {
			return null;
		}
		int i = !CMyString.isEmpty(paramString2) ? 1 : 0;
		int j = !CMyString.isEmpty(paramString3) ? 1 : 0;
		if ((i == 0) && (j == 0))
			return toString(paramString1);
		try {
			Locale localLocale = i != 0 ? new Locale(paramString2) : Locale.getDefault();

			TimeZone localTimeZone = j != 0 ? TimeZone.getTimeZone(paramString3) : TimeZone.getDefault();

			SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(paramString1, localLocale);
			localSimpleDateFormat.setTimeZone(localTimeZone);
			return localSimpleDateFormat.format(this.m_dtDate);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	public java.sql.Date toDate() {
		if (this.m_dtDate == null)
			return null;
		return new java.sql.Date(this.m_dtDate.getTime());
	}

	public Time toTime() {
		if (this.m_dtDate == null)
			return null;
		return new Time(this.m_dtDate.getTime());
	}

	public Timestamp toTimestamp() {
		if (this.m_dtDate == null)
			return null;
		return new Timestamp(this.m_dtDate.getTime());
	}

	public void setDateTime(java.util.Date paramDate) {
		this.m_dtDate = paramDate;
	}

	public boolean setDateTimeWithString(String paramString1, String paramString2) throws Exception {
		try {
			SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(paramString2);
			this.m_dtDate = localSimpleDateFormat.parse(paramString1);
			return true;
		} catch (Exception localException) {
		}
		throw new Exception("日期时间字符串值和格式无效（CMyDateTime.setDateTimeWithString）");
	}

	public void setDateTimeWithCurrentTime() {
		if (this.m_dtDate == null)
			this.m_dtDate = new java.util.Date(System.currentTimeMillis());
		else
			this.m_dtDate.setTime(System.currentTimeMillis());
	}

	public void setDateTimeWithTimestamp(Timestamp paramTimestamp) throws Exception {
		try {
			if (paramTimestamp == null) {
				this.m_dtDate = null;
			} else {
				if (this.m_dtDate == null) {
					this.m_dtDate = new java.util.Date();
				}
				this.m_dtDate.setTime(paramTimestamp.getTime());
			}
		} catch (Exception localException) {
			throw new Exception("使用Timestamp对象设置日期和时间出错：CMyDateTime.setDateTimeWithTimestamp()");
		}
	}

	public void setDateTimeWithRs(ResultSet paramResultSet, int paramInt) throws Exception {
		try {
			Timestamp localTimestamp = paramResultSet.getTimestamp(paramInt);

			setDateTimeWithTimestamp(localTimestamp);
		} catch (SQLException localSQLException) {
			throw new Exception("从记录集中读取时间字段时出错：CMyDateTime.setDateTimeWithRs()", localSQLException);
		}
	}

	public void setDateTimeWithRs(ResultSet paramResultSet, String paramString) throws Exception {
		try {
			Timestamp localTimestamp = paramResultSet.getTimestamp(paramString);
			setDateTimeWithTimestamp(localTimestamp);
		} catch (SQLException localSQLException) {
			throw new Exception("从记录集中读取时间字段时出错：CMyDateTime.setDateTimeWithRs()", localSQLException);
		}
	}

	public boolean setDate(java.sql.Date paramDate) throws Exception {
		if (paramDate == null)
			return false;
		return setDateWithString(paramDate.toString(), 0);
	}

	public boolean setTime(Time paramTime) throws Exception {
		if (paramTime == null)
			return false;
		return setTimeWithString(paramTime.toString(), 0);
	}

	public boolean setDateWithString(String paramString, int paramInt) throws Exception {
		int i = 0;
		int j = paramString.length();

		if (j < 6)
			throw new Exception("日期字符串无效（CMyDateTime.setDateWithString）");
		try {
			String str1;
			switch (paramInt) {
			case 1:
				i = j >= 10 ? 1 : 0;
				str1 = paramString.substring(0, 4) + "-" + paramString.substring(i != 0 ? 5 : 4, i != 0 ? 7 : 6) + "-"
						+ paramString.substring(i != 0 ? 8 : 6, i != 0 ? 10 : 8);

				break;
			case 2:
				str1 = paramString.charAt(0) < '5' ? "20" : "19";
				i = j >= 8 ? 1 : 0;
				str1 = str1 + paramString.substring(0, 2) + "-" + paramString.substring(i != 0 ? 3 : 2, i != 0 ? 5 : 4)
						+ "-" + paramString.substring(i != 0 ? 6 : 4, i != 0 ? 8 : 6);

				break;
			default:
				str1 = paramString;
			}

			if (this.m_dtDate == null) {
				return setDateTimeWithString(str1, "yyyy-MM-dd");
			}

			String str2 = getDateTimeAsString("HH:mm:ss");
			return setDateTimeWithString(str1 + " " + str2, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception localException) {
		}
		throw new Exception("无效的日期字符串（Exception.setDateWithString）");
	}

	public boolean setTimeWithString(String paramString, int paramInt) throws Exception {
		int i = 0;
		int j = paramString.length();

		if (j < 4)
			throw new Exception("时间字符串格式无效（）");
		try {
			String str2;
			switch (paramInt) {
			case 1:
				i = j >= 8 ? 1 : 0;
				str2 = paramString.substring(0, 2) + ":" + paramString.substring(i != 0 ? 3 : 2, i != 0 ? 5 : 4) + ":"
						+ paramString.substring(i != 0 ? 6 : 4, i != 0 ? 8 : 6);

				break;
			case 2:
				i = j >= 5 ? 1 : 0;
				str2 = paramString.substring(0, 2) + ":" + paramString.substring(i != 0 ? 3 : 2, i != 0 ? 5 : 4)
						+ ":00";

				break;
			default:
				str2 = paramString;
			}

			if (this.m_dtDate == null) {
				return setDateTimeWithString(str2, "HH:mm:ss");
			}

			String str1 = getDateTimeAsString("yyyy-MM-dd");
			return setDateTimeWithString(str1 + " " + str2, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception localException) {
		}
		throw new Exception("无效的时间字符串（Exception.setTimeWithString）");
	}

	public void setDateTimeFormat(String paramString) {
		if (this.m_dtFormater == null)
			this.m_dtFormater = new SimpleDateFormat(paramString);
		else
			this.m_dtFormater.applyPattern(paramString);
	}

	public String getDateTimeAsString(String paramString) throws Exception {
		if (this.m_dtDate == null)
			return null;
		try {
			setDateTimeFormat(paramString);
			return this.m_dtFormater.format(this.m_dtDate);
		} catch (Exception localException) {
		}
		throw new Exception("指定的日期时间格式有错（CMyDateTime.getDateTimeAsString）");
	}

	public String getDateTimeAsString() throws Exception {
		if ((this.m_dtDate == null) || (this.m_dtFormater == null))
			return null;
		try {
			return this.m_dtFormater.format(this.m_dtDate);
		} catch (Exception localException) {
		}
		throw new Exception("格式化日期时间字符串出错（CMyDateTime.getDateTimeAsString()）");
	}

	public static String extractDateTimeFormat(String paramString) {
		char[] arrayOfChar = { 'y', 'M', 'd', 'H', 'm', 's' };

		return extractFormat(paramString, arrayOfChar);
	}

	public static String extractDateFormat(String paramString) {
		char[] arrayOfChar = { 'y', 'M', 'd' };

		return extractFormat(paramString, arrayOfChar);
	}

	public static String extractTimeFormat(String paramString) {
		char[] arrayOfChar = { 'H', 'm', 's' };

		return extractFormat(paramString, arrayOfChar);
	}

	private static String extractFormat(String paramString, char[] paramArrayOfChar) {
		if (paramString == null) {
			return null;
		}
		char[] arrayOfChar = paramString.trim().toCharArray();
		if (arrayOfChar.length == 0) {
			return null;
		}

		StringBuffer localStringBuffer = new StringBuffer(19);

		int i = 0;
		int j = 0;

		while (i < arrayOfChar.length) {
			char c = arrayOfChar[(i++)];
			if (Character.isDigit(c)) {
				localStringBuffer.append(paramArrayOfChar[j]);
				continue;
			}
			localStringBuffer.append(c);
			j++;
			if (j >= paramArrayOfChar.length) {
				break;
			}
		}

		return localStringBuffer.toString();
	}

	public boolean setDateTimeWithString(String paramString) throws Exception {
		String str = extractDateTimeFormat(paramString);
		if (paramString == null) {
			return false;
		}

		return setDateTimeWithString(paramString, str);
	}

	public static final String formatTimeUsed(long paramLong) {
		if (paramLong <= 0L) {
			return "";
		}

		int i = 0;
		int j = 0;
		StringBuffer localStringBuffer = new StringBuffer(16);
		i = (int) (paramLong / 1000L);
		paramLong %= 1000L;
		if (i > 0) {
			j = i / 60;
			i %= 60;
		}
		if (j > 0) {
			if (j > 1) {
				localStringBuffer.append(j).append("分");
			} else {
				localStringBuffer.append(j).append("分");
			}

			if (i < 10) {
				localStringBuffer.append('0');
			}
			localStringBuffer.append(i);
		} else {
			localStringBuffer.append(i).append('.');
			if (paramLong < 10L)
				localStringBuffer.append('0').append('0');
			else if (paramLong < 100L) {
				localStringBuffer.append('0');
			}
			localStringBuffer.append(paramLong);
		}
		if (paramLong > 1L) {
			localStringBuffer.append("秒");
		} else {
			localStringBuffer.append("秒");
		}

		return localStringBuffer.toString();
	}

	public static String getStr(Object paramObject, String paramString) {
		if ((paramObject instanceof CMyDateTime)) {
			return ((CMyDateTime) paramObject).toString(paramString);
		}

		return CMyString.showObjNull(paramObject);
	}

	public boolean isLeapYear() throws Exception {
		GregorianCalendar localGregorianCalendar = new GregorianCalendar();
		localGregorianCalendar.setTime(this.m_dtDate);
		return localGregorianCalendar.isLeapYear(getYear());
	}

	public boolean isToday() {
		CMyDateTime localCMyDateTime = now();
		return toString("yyyy-MM-dd").equals(localCMyDateTime.toString("yyyy-MM-dd"));
	}

	public int getWeekCountsOfMonth(boolean paramBoolean) throws Exception {
		GregorianCalendar localGregorianCalendar = new GregorianCalendar();
		localGregorianCalendar.setTime(this.m_dtDate);
		int i = localGregorianCalendar.getActualMaximum(4);

		if (paramBoolean) {
			return i;
		}

		CMyDateTime localCMyDateTime = new CMyDateTime();
		localCMyDateTime.setDateTime(this.m_dtDate);
		localCMyDateTime.setDateTimeWithString(localCMyDateTime.getYear() + "-" + localCMyDateTime.getMonth() + "-1");

		if (localCMyDateTime.getDayOfWeek() == 6) {
			i++;
		}

		return i;
	}

	public boolean equals(Object paramObject) {
		return (paramObject != null) && ((paramObject instanceof CMyDateTime))
				&& (((CMyDateTime) paramObject).getTimeInMillis() == getTimeInMillis());
	}

	public boolean testDateFormat(String paramString) {
		try {
			boolean bool = setDateTimeWithString(paramString);
			return bool;
		} catch (Exception localException) {
		}
		return false;
	}
}