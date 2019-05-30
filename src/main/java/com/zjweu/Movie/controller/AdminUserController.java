package com.zjweu.Movie.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zjweu.Movie.common.CtlStatus;
import com.zjweu.Movie.entity.User;
import com.zjweu.Movie.service.AdminUserService;
import com.zjweu.Movie.service.UserService;
import com.zjweu.Movie.util.CMyString;

@CrossOrigin
@RequestMapping("/adminUser")
@RestController
public class AdminUserController {

	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	private UserService userService;

	@RequestMapping("/delete")
	public Object delete(@RequestParam(value = "userId", required = true) Integer userId) throws Exception {
		userService.del(userService.findById(userId));
		return CtlStatus.success();
	}

	/**
	 * Description:批量删除
	 *
	 * @param request
	 * @param ids
	 * @param operation
	 * @return
	 * @author cui.jianbin
	 * @date 2019年5月8日 下午2:59:15
	 * @version 1.0
	 */
	@RequestMapping("/deleteList")
	public Object deleteList(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids) {
		if (ids.length() == 0) {
			CtlStatus.failed("你还未勾选用户，请选择!");
		} else {
			userService.delList(ids);
		}
		return CtlStatus.success("操作成功");
	}

	/**
	 * Description:统计总人数
	 *
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月28日 下午3:48:05
	 * @version 1.0
	 */
	@RequestMapping("/countUser")
	public Object countUser() throws Exception {
		int count = adminUserService.countUser();
		return CtlStatus.success(count);
	}

	/**
	 * Description:显示所有用户
	 *
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月28日 下午3:51:04
	 * @version 1.0
	 */
	@RequestMapping("/listAllUser")
	public Object listAllUser() {
		List<User> ulist = adminUserService.listAllUser();
		return CtlStatus.success(ulist);
	}

	/**
	 * Description:7天内新增用户人数
	 *
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月28日 下午3:49:29
	 * @version 1.0
	 */
	@RequestMapping("/countNewUser")
	public Object countNewUser() throws Exception {
		int count = adminUserService.countNewUser();
		return CtlStatus.success(count);
	}

	/**
	 * Description:新增用户列表
	 *
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月28日 下午3:52:37
	 * @version 1.0
	 */
	@RequestMapping("/listNewUser")
	public Object listNewUser() throws Exception {
		List<User> ulist = adminUserService.listNewUser();
		return CtlStatus.success(ulist);
	}

	/**
	 * Description:根据用户名模糊查询或手机号精确查询
	 *
	 * @param searchWord
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月30日 下午4:53:37
	 * @version 1.0
	 */
	@RequestMapping("/listByConditions")
	public Object listByConditions(@RequestParam(value = "searchWord", required = true) String searchWord,
			@RequestParam(value = "new", required = false) Integer u) throws Exception {
		List<User> ulist = null;
		if (CMyString.isEmpty(searchWord)) {
			if (u != null) {
				ulist = adminUserService.listNewUser();
			} else {
				ulist = adminUserService.listAllUser();
			}
		}
		ulist = adminUserService.listByConditions(searchWord);
		if (u != null) {
			ulist = adminUserService.listByConditionsInNew(searchWord);
		}
		return CtlStatus.success(ulist);
	}

	/**
	 * Description:禁言或者允许发言用户
	 *
	 * @param request
	 * @param ids
	 * @param operation(0是允许发言，1是禁言)
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月30日 上午11:34:04
	 * @version 1.0
	 */
	@RequestMapping("/wordAllowOrNot")
	public Object wordAllowOrNot(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids,
			@RequestParam(value = "operation", required = true) Integer operation) {
		if (ids.length() == 0) {
			CtlStatus.failed("你还未勾选用户，请选择!");
		} else if (ids.length() == 1) {
			adminUserService.wordAllowOrNot(Integer.valueOf(ids), operation);
		} else {
			adminUserService.ListWordAllowOrNot(ids, operation);
		}
		return CtlStatus.success("操作成功");
	}

	/**
	 * Description:登录
	 *
	 * @param request
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月25日 上午11:12:52
	 * @version 1.0
	 */
	@RequestMapping("/login")
	public Object login(HttpServletRequest request, @RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password) throws Exception {
		return adminUserService.login(username, password);
	}

}
