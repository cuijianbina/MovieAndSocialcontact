package com.zjweu.Movie.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zjweu.Movie.common.CtlStatus;
import com.zjweu.Movie.entity.User;
import com.zjweu.Movie.util.MD5Util;

@Service
public class UserService {

	@Autowired
	private BaseService baseService;

	/**
	 * Description:保存或更新用户信息
	 *
	 * @param user
	 * @author cui.jianbin
	 * @date 2019年4月8日 下午5:12:10
	 * @version 1.0
	 */
	public void saveOrUpdate(User user) {
		if(user.getId()==null){
			baseService.save(user);
		}else{
			baseService.update(user);
		}
	}
	
	/**
	 * Description:判断用户是否被禁言
	 *
	 * @param userId
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月3日  下午4:24:35
	 * @version 1.0
	 */
	public boolean getWordAllow(int userId){
		User user = findById(userId);
		boolean flag = true;
		int status = user.getStatus();
		if(status==1){
			flag = false;
		}
		return flag;
	}

	/**
	 * Description:删除该用户
	 *
	 * @param user
	 * @author cui.jianbin
	 * @date 2019年4月8日 下午5:12:40
	 * @version 1.0
	 */
	public void del(User user) {
		baseService.delete(user);
	}
	
	public void delList(String ids){
		String[]  idss = ids.split(",");
		for(String id:idss){
			del(findById(Integer.valueOf(id)));
		}
	}

	/**
	 * Description:根据用户名或手机号查询
	 *
	 * @param usernameOrMobile
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月7日  上午10:54:55
	 * @version 1.0
	 */
	public User findByUsernameOrMobile(String usernameOrMobile) {
		List<Object> params = new ArrayList<>();
		String sql = "from User where username=?1 or mobile=?2";
		params.add(usernameOrMobile);
		params.add(usernameOrMobile);
		List<User> list = baseService.find(sql, params);
		return list.get(0);
	}

	/**
	 * Description:根据token查找用户,唯一
	 *
	 * @param token
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月7日  上午10:56:43
	 * @version 1.0
	 */
	public User findByToken(String token){
		List<Object> params = new ArrayList<>();
		String sql = "from User where token=?1";
		params.add(token);
		List<User> list = baseService.find(sql, params);
		if(list.size()==0||list==null){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	/**
	 * Description:根据id查找用户
	 *
	 * @param id
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月2日 下午5:29:39
	 * @version 1.0
	 */
	//@Cacheable(value="users")
	public User findById(int id) {
		return baseService.findById(User.class, id);
	}

	/**
	 * Description:根据用户名查询
	 *
	 * @param username
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月8日 下午4:49:45
	 * @version 1.0
	 */
	
	public User findByUsername(String username) throws Exception {
		return baseService.findObject("User", "username=?1", username);
	}

	/**
	 * Description:根据手机号查询
	 *
	 * @param mobile
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月8日 下午4:51:06
	 * @version 1.0
	 */
	public User findByMobile(String mobile) throws Exception {
		return baseService.findObject("User", "mobile=?1", mobile);
	}
	
	public User findByIdNumber(String idNumber) throws Exception{
		return baseService.findObject("User", "idNumber=?1", idNumber);
	}

	/**
	 * Description:查询是否存在该用户名
	 *
	 * @param username
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月2日 下午5:44:50
	 * @version 1.0
	 */
	public boolean existUsername(String username) throws Exception {
		return baseService.existData(User.class.getName(), "username=?1", username);
	}

	/**
	 * Description:判断该手机号是否存在
	 *
	 * @param mobile
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月8日 下午4:44:09
	 * @version 1.0
	 */
	public boolean existMobile(String mobile) throws Exception {
		return baseService.existData(User.class.getName(), " mobile=?1", mobile);
	}

	/**
	 * Description:用户登录
	 *
	 * @param usernameOrMobile
	 * @param password
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月8日 下午4:58:40
	 * @version 1.0
	 */
	public Object login(String usernameOrMobile, String password) throws Exception {
		String usernameRegex = "^[a-zA-Z]\\d{9}$";// 用户名必要以字母开头，后面接着9位数字
		String mobileRegex = "^[1][3578]\\d{9}$";// 手机号码必须以1开头，第二位是3578中的任意一位数字，总长度为11
		User user = null;
		String realPassword = MD5Util.encode(password);
		if (usernameOrMobile.matches(usernameRegex)) {
			user = findByUsername(usernameOrMobile);
			if (user == null) {
				return CtlStatus.failed("该账号还未注册，请先注册！！");
			} else {
				if (realPassword.equals(user.getPassword())) {
					return CtlStatus.success("登录成功");
				} else {
					return CtlStatus.failed("用户名或密码错误");
				}
			}
		} else if (usernameOrMobile.matches(mobileRegex)) {
			user = findByMobile(usernameOrMobile);
			if (user == null) {
				return CtlStatus.failed("该手机号还未注册，请先注册！");
			} else {
				if (realPassword.equals(user.getPassword())) {
					return CtlStatus.success("登录成功");
				} else {
					return CtlStatus.failed("手机号或密码输入错误");
				}
			}
		} else {
			return CtlStatus.failed("你输入的用户名格式不正确，请重新输入！");
		}
	}
}
