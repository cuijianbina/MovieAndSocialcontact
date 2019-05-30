package com.zjweu.Movie.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.zjweu.Movie.common.DateCommon;
import com.zjweu.Movie.config.ApplicationHelper;
import com.zjweu.Movie.constants.AdminUserConstants;
import com.zjweu.Movie.entity.ChatRoom;
import com.zjweu.Movie.entity.Message;
import com.zjweu.Movie.entity.User;
import com.zjweu.Movie.util.CMyString;

import net.sf.json.JSONObject;

@ServerEndpoint("/websocket/{info}")
@Component
public class WebSocketService {
	private static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// 创建时间格式对象
	// concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketService对象。
	// 创建一个房间的集合，用来存放房间
	private static ConcurrentHashMap<Integer, ConcurrentHashMap<String, WebSocketService>> roomList = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, WebSocketService>>();

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	// 重新加入房间的标示；
	private int rejoin = 0;
	// static {
	// roomList.put("room1", new ConcurrentHashMap<String, WebSocketService>());
	// }

	private ChatRoomService chatRoomService = (ChatRoomService) ApplicationHelper.getBean("chatRoomService");

	private UserService userService = (UserService) ApplicationHelper.getBean("userService");
	
	private MessageService messageService = (MessageService)ApplicationHelper.getBean("messageService");

	/**
	 * Description:获得该聊天室中的所有用户
	 *
	 * @param roomId
	 *            电影Id
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月11日 下午3:30:51
	 * @version 1.0
	 */
	public void getUserInChatRoom(int roomId) throws Exception {
		ChatRoom chatRoom = chatRoomService.findById(roomId);
		String users = chatRoom.getUserIds();
		if (CMyString.isEmpty(users)) {
			roomList.put(roomId, new ConcurrentHashMap<String, WebSocketService>());
			return;
		}
		String[] userIds = users.split(",");
		ConcurrentHashMap<String, WebSocketService> s = new ConcurrentHashMap<String, WebSocketService>();
		for (String userId : userIds) {
			User user = userService.findById(Integer.valueOf(userId));
			String userName = user.getUsername();
			WebSocketService ws = roomList.get(roomId).get(userName);
			if (ws != null) {
				s.put(userName, ws);
			} else {
				s.put(userName, new WebSocketService());
			}
		}
		roomList.put(roomId, s);
	}

	/**
	 * 用户接入
	 * 
	 * @param session
	 * @throws Exception
	 */
	@OnOpen
	public void onOpen(@PathParam(value = "info") String param, Session session) throws Exception {
		this.session = session;
		String[] s = param.split("[|]");
		String flag = s[0]; // 标识
		String member = s[1]; // 房间Id
		getUserInChatRoom(Integer.valueOf(member));
		if (flag.equals("join")) {
			// String user = param.split("[|]")[2];
			String token = s[2]; // 用户名
			joinRoom(Integer.valueOf(member), token);
		}
	}

	/**
	 * Description:加入聊天室
	 *
	 * @param member
	 *            房间Id
	 * @param user
	 * @author cui.jianbin
	 * @date 2019年4月11日 下午3:33:35
	 * @version 1.0
	 * @throws Exception
	 */
	public void joinRoom(int member, String token) throws Exception {
		ConcurrentHashMap<String, WebSocketService> r = roomList.get(member);
		User u = userService.findByToken(token);
		String user = u.getUsername();
		if (r.get(user) != null) { // 该用户有没有出
			this.rejoin = 1;// 用户在此聊天室中标识
		}
		r.put(user, this);// 将此用户加入房间中
		String userId = u.getId().toString();
		ChatRoom chatRoom = chatRoomService.findById(member);
		String userIds = chatRoom.getUserIds();
		if (CMyString.isEmpty(userIds)) {
			chatRoom.setUserIds(userId);
		} else {
			StringBuffer sb = new StringBuffer(chatRoom.getUserIds());
			chatRoom.setUserIds(sb.append(",").append(userId).toString());
		}
		chatRoomService.saveOrUpdate(chatRoom);
	}

	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 接收到来自用户的消息
	 * 
	 * @param message
	 * @param session
	 * @throws Exception
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws Exception {
		// 把用户发来的消息解析为JSON对象
		JSONObject obj = JSONObject.fromObject(message);
		//System.out.println(obj);
		User user = userService.findByToken(obj.get("nickname").toString());
		String userName = user.getUsername();
		obj.put("nickname", userName);
		//obj.put("nickname", java.net.URLDecoder.decode(obj.get("nickname").toString(), "UTF-8"));
		if (obj.get("flag").toString().equals("exitroom")) { // 退出房间操作
			int roomid = Integer.valueOf(obj.get("roomid").toString());
			// 将用户从聊天室中移除
			int f2 = 1;
			roomList.get(roomid).remove(userName);// 将用户直接移除
			//User uu = userService.findByUsername(userName);
			int userId = user.getId();
			ChatRoom chatRoom = chatRoomService.findById(roomid);
			String userIds = chatRoom.getUserIds();
			String[] us = userIds.split(",");
			List<String> ids = new ArrayList<>();
			for (String id : us) {
				if (!id.equals(String.valueOf(userId))) {
					ids.add(id);
				}
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < ids.size(); i++) {
				if (i == (ids.size() - 1)) {
					sb.append(ids.get(i));
				} else {
					sb.append(ids.get(i)).append(",");
				}
			}
			chatRoom.setUserIds(sb.toString());
			chatRoomService.saveOrUpdate(chatRoom);
			if (f2 == 1) { // 证明该房间还有其它成员，则通知其它成员更新列表
				obj.put("flag", "exitroom");
				String m = obj.get("nickname").toString() + " 退出了房间";
				obj.put("message", m);
				ConcurrentHashMap<String, WebSocketService> r = roomList.get(roomid);
				List<String> uname = new ArrayList<String>();
				for (String u : r.keySet()) {
					uname.add(u);
				}
				obj.put("uname", uname.toArray());
				for (String i : r.keySet()) { // 遍历该房间
					r.get(i).sendMessage(obj.toString());// 调用方法 将消息推送
				}
			}
		} else if (obj.get("flag").toString().equals("chatroom")) { // 聊天室的消息
																	// 加入房间/发送消息
			// 向JSON对象中添加发送时间
			obj.put("date", df.format(new Date()));
			// 获取客户端发送的数据中的内容---房间�? 用于区别该消息是来自于哪个房间
			int roomid = Integer.valueOf(obj.get("target").toString());
			// 获取客户端发送的数据中的内容---用户
			String content = obj.getString("content");
			if(!CMyString.isEmpty(content)){
				Message ms = new Message();
				ms.setAimId(-1);
				ms.setSendId(user.getId());
				ms.setMessages(obj.getString("content"));
				ms.setStatus(AdminUserConstants.MESSAGE_NOT_READ);
				ms.setSendTime(DateCommon.getTime());
				messageService.save(ms);//保存消息到数据库
				user.setSendCount(user.getSendCount() + 1);
				userService.saveOrUpdate(user);
			}
			// 从房间列表中定位到该房间
			ConcurrentHashMap<String, WebSocketService> r = roomList.get(roomid);
			List<String> uname = new LinkedList<String>();
			//List<String> logo = new LinkedList<String>();
			for (String u : r.keySet()) {
				uname.add(u);
				//String logoB = userService.findByUsername(u).getLogo();
				//logoB.lastIndexOf("/");
				//logo.add(logoB.substring(logoB.lastIndexOf("/")+1));
			}
			obj.put("uname", uname.toArray());
			//obj.put("logo", logo.toArray());
			//System.out.println(obj.toString()+"lllllllllllllll");
			if (r.get(userName).rejoin == 0) { // 证明不是退出重连
				for (String i : r.keySet()) { // 遍历该房间
					obj.put("isSelf", userName.equals(i));// 设置消息是否为自己的
					r.get(i).sendMessage(obj.toString());// 调用方法 将消息推送
				}
			} else {
				obj.put("isSelf", true);
				r.get(userName).sendMessage(obj.toString());
			}
			r.get(userName).rejoin = 0;
		}

	}

	/**
	 * 用户断开
	 * 
	 * @param session
	 */
	@OnClose
	public void onClose(Session session) {
	}

	/**
	 * 用户连接异常
	 * 
	 * @param t
	 */
	@OnError
	public void onError(Throwable t) {

	}
}
