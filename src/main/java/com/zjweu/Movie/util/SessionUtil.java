package com.zjweu.Movie.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.zjweu.Movie.entity.User;

public class SessionUtil {

	/**
	 * Description:获取httpsession
	 *
	 * @param request
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月19日 下午2:38:34
	 * @version 1.0
	 */
	public static HttpSession getSession(HttpServletRequest request) {
		return request.getSession();
	}

	/**
	 * Description:设置session
	 *
	 * @param request
	 * @param key
	 * @author cui.jianbin
	 * @date 2019年4月19日 下午2:46:14
	 * @version 1.0
	 */
	public static void setSession(HttpServletRequest request, User user) {
		HttpSession session = getSession(request);
		session.setAttribute("user", user);
	}

	/**
	 * Description:获取session中对象,key为user获取sessin中的用户
	 *
	 * @param request
	 * @param key
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月19日 下午2:39:08
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getObjFromSession(HttpServletRequest request, String key) {
		HttpSession session = getSession(request);
		if (session == null)
			return null;
		return (T) session.getAttribute(key);
	}

	/**
	 * Description:清除session
	 *
	 * @param request
	 * @author cui.jianbin
	 * @date 2019年4月19日 下午3:30:25
	 * @version 1.0
	 */
	public static void invalidate(HttpServletRequest request) {
		HttpSession session = getSession(request);
		session.invalidate();
	}
}
