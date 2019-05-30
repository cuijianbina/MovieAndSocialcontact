package com.zjweu.Movie.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zjweu.Movie.common.CtlStatus;
import com.zjweu.Movie.common.DateCommon;
import com.zjweu.Movie.constants.AdminUserConstants;
import com.zjweu.Movie.entity.Message;
import com.zjweu.Movie.entity.User;
import com.zjweu.Movie.service.MessageService;
import com.zjweu.Movie.service.UserService;
import com.zjweu.Movie.util.CMyString;
import com.zjweu.Movie.util.SessionUtil;

//@CrossOrigin
@RequestMapping("/message")
@RestController
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;

	/**
	 * Description:发送消息
	 *
	 * @param request
	 * @param messages
	 * @param aimId
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月29日 下午3:38:01
	 * @version 1.0
	 */
	@RequestMapping("/sendMessage")
	public Object sendMessage(HttpServletRequest request,
			@RequestParam(value = "messages", required = true) String messages,
			@RequestParam(value = "aimId", required = false, defaultValue = "1") Integer aimId) throws Exception {
		String token = request.getHeader("token");
		if(CMyString.isEmpty(token)||token.equals("undefined")){
			return CtlStatus.failed("你还未登录");
		}
		User user = userService.findByToken(token);
		if(user==null){
			return CtlStatus.failed("该用户不存在，请联系管理员");
		}
		int userId = user.getId();
		Message message = new Message();
		message.setAimId(aimId);
		message.setSendId(userId);
		message.setMessages(messages);
		Date sendTime = DateCommon.getTime();
		message.setSendTime(sendTime);
		message.setStatus(AdminUserConstants.MESSAGE_NOT_READ);
		messageService.save(message);
		user.setSendCount(user.getSendCount()+1);//发送消息数+1
		userService.saveOrUpdate(user);
		return CtlStatus.success("发送成功");
	}
	
	/**
	 * Description:消息总数
	 *
	 * @param aimId
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月13日  上午10:43:13
	 * @version 1.0
	 */
	@RequestMapping("/count")
	public Object count(@RequestParam(value="aimId",required=false,defaultValue="1") Integer aimId){
		return CtlStatus.success(messageService.countAllMessage(aimId));
	}
	
	/**
	 * Description:显示消息列表
	 *
	 * @param aimId
	 * @param status
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月12日  下午9:59:56
	 * @version 1.0
	 */
	@RequestMapping("/listAllMessage")
	public Object  listAllMessage(@RequestParam(value="aimId",required=false,defaultValue="1") Integer aimId,@RequestParam(value="status",required=false) Integer status){
		if(status!=null){
			if(status==2){
				status=null;
			}
		}
		List<Map<String, Object>> mlist = messageService.findByAimId(aimId,status);
		return CtlStatus.success(mlist);
	}
	
	/**
	 * Description:更新信息状态
	 *
	 * @param mid
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月13日  上午9:37:57
	 * @version 1.0
	 */
	@RequestMapping("/update")
	public Object update(@RequestParam(value="mid",required=true) Integer mid){
		Message message = messageService.findById(mid);
		messageService.update(message);
		return CtlStatus.success();
	}
	
	/**
	 * Description:根据用户名查找该用户的所有聊天记录
	 *
	 * @param username
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月15日  上午8:51:51
	 * @version 1.0
	 * @throws Exception 
	 */
	@RequestMapping("/findByUsername")
	public Object findByUsername(@RequestParam(value="username",required=true) String username) throws Exception{
		if(CMyString.isEmpty(username)){
			return listAllMessage(-1, null);
		}
		User user = userService.findByUsername(username);
		if(user==null){
			return CtlStatus.failed("该用户不存在");
		}else{
			List<Map<String, Object>>  result =messageService.findBySendId(user.getId());
			return CtlStatus.success(result);
		}
	}
	
}
