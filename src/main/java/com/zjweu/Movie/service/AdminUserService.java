package com.zjweu.Movie.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjweu.Movie.common.CtlStatus;
import com.zjweu.Movie.common.DateCommon;
import com.zjweu.Movie.common.Page;
import com.zjweu.Movie.constants.AdminUserConstants;
import com.zjweu.Movie.entity.User;
import com.zjweu.Movie.util.MD5Util;

@Service
public class AdminUserService {

	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;

	/**
	 * Description:统计所有用户数
	 *
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月28日 上午11:29:52
	 * @version 1.0
	 */
	public int countUser() throws Exception {
		return baseService.count("User", null, null);
	}

	/**
	 * Description:列表显示所有用户
	 *
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月28日 上午11:35:59
	 * @version 1.0
	 */
	public List<User> listAllUser() {
		return baseService.find("from User", null);
	}

	/**
	 * Description:获得最近7天的的新增用户统计信息
	 *
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月28日 上午11:34:39
	 * @version 1.0
	 */
	public int countNewUser() throws Exception {
		Date beginDate = DateCommon.getDayServenBefore();
		Date endDate = DateCommon.getTime();
		List<Object> params = new ArrayList<>();
		params.add(beginDate);
		params.add(endDate);
		return baseService.count("User", "registerTime>=?1 and registerTime<=?2", params);
	}

	/**
	 * Description:显示近7天新增用户
	 *
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月28日 下午2:19:09
	 * @version 1.0
	 */
	public List<User> listNewUser() throws Exception {
		Date beginDate = DateCommon.getDayServenBefore();
		Date endDate = DateCommon.getTime();
		List<Object> params = new ArrayList<>();
		params.add(beginDate);
		params.add(endDate);
		return baseService.find("from User where registerTime>=?1 and registerTime<=?2", params);
	}

	/**
	 * Description:根据用户名查找，用于登录校验
	 *
	 * @param username
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月30日 上午11:11:35
	 * @version 1.0
	 */
	public User findByUsername(String username) throws Exception {
		return baseService.findObject("AdminUser", "username=?1", username);
	}

	/**
	 * Description:批量操作禁言或允许发言
	 *
	 * @param ids
	 * @param operation
	 * @author cui.jianbin
	 * @date 2019年4月30日 上午11:24:51
	 * @version 1.0
	 */
	public void ListWordAllowOrNot(String ids, int operation) {
		String[] idss = ids.split(",");
		for (String id : idss) {
			int userId = Integer.valueOf(id);
			wordAllowOrNot(userId, operation);
		}
	}

	/**
	 * Description:对单个用户进行禁言操作，0是允许发言，1是禁言
	 *
	 * @param userId
	 * @param operation
	 * @author cui.jianbin
	 * @date 2019年4月30日 上午11:20:38
	 * @version 1.0
	 */
	public void wordAllowOrNot(int userId, int operation) {
		User user = userService.findById(userId);
		if (operation == AdminUserConstants.WORD_ALLOW) {
			user.setStatus(AdminUserConstants.WORD_ALLOW);
		} else if (operation == AdminUserConstants.WORD_NOT_ALLOW) {
			user.setStatus(AdminUserConstants.WORD_NOT_ALLOW);
		}
		userService.saveOrUpdate(user);
	}

	/**
	 * Description:登录
	 *
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月8日 下午4:58:40
	 * @version 1.0
	 */
	public Object login(String username, String password) throws Exception {
		String realPassword = MD5Util.encode(password);
		User user = findByUsername(username);
		if (realPassword.equals(user.getPassword())) {
			return CtlStatus.success("登录成功");
		} else {
			return CtlStatus.failed("用户名或密码错误");
		}
	}

	/**
	 * Description:根据关键字模糊查询所有用户
	 *
	 * @param searchWord
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月28日 上午11:28:22
	 * @version 1.0
	 */
	public List<User> listByConditions(String searchWord) {
		String hql = "from User where (username like ?1 or mobile = ?2)";
		List<Object> params = new ArrayList<>();
		params.add("%" + searchWord + "%");
		params.add(searchWord);
		return baseService.find(hql, params);
	}

	/**
	 * Description:根据关键字模糊查询最近7天注册用户中符合条件的
	 *
	 * @param searchWord
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月30日 下午5:59:10
	 * @version 1.0
	 * @throws Exception 
	 */
	public List<User> listByConditionsInNew(String searchWord) throws Exception {
		String hql = "from User where (username like ?1 or mobile = ?2) and registerTime>=?3 and registerTime<=?4";
		List<Object> params = new ArrayList<>();
		params.add("%" + searchWord + "%");
		params.add(searchWord);
		Date beginDate = DateCommon.getDayServenBefore();
		Date endDate = DateCommon.getTime();
		params.add(beginDate);
		params.add(endDate);
		return baseService.find(hql, params);
	}

}
