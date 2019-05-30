package com.zjweu.Movie.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zjweu.Movie.common.CtlStatus;
import com.zjweu.Movie.common.DateCommon;
import com.zjweu.Movie.constants.UserConstants;
import com.zjweu.Movie.entity.User;
import com.zjweu.Movie.service.RedisService;
import com.zjweu.Movie.service.UserService;
import com.zjweu.Movie.util.CMyString;
import com.zjweu.Movie.util.MD5Util;
import com.zjweu.Movie.util.SendPhoneVerifyCode;
import com.zjweu.Movie.util.SessionUtil;
import com.zjweu.Movie.util.VerifyUtil;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisService redisService;

	private FileUploadController fileUploadControler = new FileUploadController();

	/**
	 * Description:用户登录
	 *
	 * @param request
	 * @param usernameOrMobile
	 *            用户名或手机号
	 * @param password
	 *            密码
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月19日 下午2:52:40
	 * @version 1.0
	 */
	@RequestMapping("/login")
	public Object login(HttpServletRequest request,
			@RequestParam(value = "usernameOrMobile", required = true) String usernameOrMobile,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "code", required = true) String code) throws Exception {
		String imgCode = redisService.get("imgCode");
		if (!code.equalsIgnoreCase(imgCode)) {
			return CtlStatus.failed("验证码输入错误");
		}
		Object object = userService.login(usernameOrMobile, password);
		// JSONObject jsonObject = JSONObject.fromObject(object);
		CtlStatus ctlStatus = (CtlStatus) object;
		if (!ctlStatus.isSuccess()) {
			return ctlStatus;
		}
		String token = UUID.randomUUID().toString().substring(0, 16);
		User user = userService.findByUsernameOrMobile(usernameOrMobile);
		user.setLastLoginTime(DateCommon.getTime());
		user.setToken(token);
		userService.saveOrUpdate(user);
		return CtlStatus.success().put("token", token);
	}

	/**
	 * Description:退出
	 *
	 * @param request
	 * @author cui.jianbin
	 * @date 2019年4月19日 下午3:33:09
	 * @version 1.0
	 */
	@RequestMapping("/quit")
	public void quit(HttpServletRequest request) {
		String token = request.getHeader("token");
		User user = userService.findByToken(token);
		user.setToken("");
		userService.saveOrUpdate(user);
	}

	@RequestMapping("/findByToken")
	public Object findByToken(HttpServletRequest request,
			@RequestParam(value = "token", required = false) String token) {
		if (CMyString.isEmpty(token)) {
			return CtlStatus.failed();
		}
		User user = userService.findByToken(token);
		if (user == null) {
			return CtlStatus.failed();
		}
		return CtlStatus.success(user.getUsername() + "," + user.getLogo());
	}

	/**
	 * Description:用户注册
	 *
	 * @param request
	 * @param username
	 * @param password
	 * @param mobile
	 * @param code
	 * @param phoneVerifyCode
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月25日 上午9:48:15
	 * @version 1.0
	 * @throws Exception
	 */
	@RequestMapping("/register")
	public Object register(HttpServletRequest request,
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "mobile", required = true) String mobile,
			@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "phoneVerifyCode", required = true) String phoneVerifyCode,
			@RequestParam(value = "idNumber", required = true) String idNumber,
			@RequestParam(value = "gender", required = false, defaultValue = "0") Integer gender,
			@RequestParam(value = "file") MultipartFile filename) throws Exception {
		String coverImg =null;
		
		String imgCode = redisService.get("imgCode");
		if (!code.equalsIgnoreCase(imgCode)) {
			return CtlStatus.failed("验证码输入错误");
		}
		Map<String, Integer> codeMap = SendPhoneVerifyCode.getMap();
		if (userService.findByMobile(mobile) != null) {
			return CtlStatus.failed("手机号已存在");
		}
		if (userService.findByUsername(username) != null) {
			return CtlStatus.failed("用户名已存在");
		}
		if (userService.findByIdNumber(idNumber) != null) {
			return CtlStatus.failed("身份证已存在");
		}
		int relCode = codeMap.get(mobile);
		if (Integer.valueOf(phoneVerifyCode) != relCode) {
			return CtlStatus.failed("手机验证码输入错误");
		}
		CtlStatus ct = (CtlStatus) fileUploadControler.fileUpload(filename);
		if(!ct.isSuccess()){
			return ct;
		}else{
			coverImg =(String) ct.get("coverImg");
		}
		User user = new User();
		
		user.setLogo(coverImg);
		user.setUsername(username);
		user.setIdNumber(idNumber);
		user.setMobile(mobile);
		user.setPassword(MD5Util.encode(password));
		user.setGender(gender);
		user.setStatus(UserConstants.USER_STATUS_ALLOW);
		Date registerTime = DateCommon.getTime();
		user.setRegisterTime(registerTime);
		userService.saveOrUpdate(user);
		return CtlStatus.success("注册成功");
	}

	/**
	 * Description:发送手机验证码
	 *
	 * @param request
	 * @param mobile
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月25日 上午10:19:47
	 * @version 1.0
	 */
	@RequestMapping("/sendPhoneVerifyCode")
	public Object sendPhoneVerifyCode(HttpServletRequest request,
			@RequestParam(value = "mobile", required = true) String mobile) {
		SendPhoneVerifyCode.testSend(mobile);
		//SendPhoneVerifyCode.send(mobile);
		return CtlStatus.success();
	}

	/**
	 * Description:图片验证码
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 * @author cui.jianbin
	 * @date 2019年4月27日 下午5:52:48
	 * @version 1.0
	 */
	@RequestMapping("image")
	public void image(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		try {
			Object[] objs = VerifyUtil.createImage();
			BufferedImage image = (BufferedImage) objs[1];
			// 生产验证码字符串并保存到session中
			String createText = (String) objs[0];
			// HttpSession session = SessionUtil.getSession(request);
			// session.setAttribute(CodeConstants.SESSION_CODE_IMAGE,
			// createText);
			redisService.set("imgCode", createText);
			// System.out.println( "验证码=" + createText);
			ImageIO.write(image, "png", jpegOutputStream);
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		// 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(jpegOutputStream.toByteArray());
		responseOutputStream.flush();
		responseOutputStream.close();
	}
}
