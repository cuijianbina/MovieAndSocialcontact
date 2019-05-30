package com.zjweu.Movie.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjweu.Movie.entity.ChatRoom;

@Service
public class ChatRoomService {

	@Autowired
	BaseService baseService;

	public void saveOrUpdate(ChatRoom chatRoom) {
		if(chatRoom.getId()==null){
			baseService.save(chatRoom);
		}else{
			baseService.update(chatRoom);
		}
	}
	
	public void delete(ChatRoom chatRoom){
		baseService.delete(chatRoom);
	}

	/***
	 * Description:显示所有聊天室
	 *
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月10日 上午10:23:16
	 * @version 1.0
	 */
	public List<ChatRoom> listAll() {
		String sql = "from ChatRoom";
		return baseService.find(sql, null);
	}

	/**
	 * Description:根据电影Id查找聊天室
	 *
	 * @param movieId
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月10日 上午10:39:56
	 * @version 1.0
	 * @throws Exception
	 */
	public ChatRoom findByMovieId(int movieId) throws Exception {
		return baseService.findObject(ChatRoom.class.getName(), " movieId =?1", movieId);
	}

	/**
	 * Description:根据房间号查找房间
	 *
	 * @param roomId
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月14日 下午4:25:42
	 * @version 1.0
	 */
	public ChatRoom findById(int roomId) throws Exception {
		return baseService.findById(ChatRoom.class, roomId);
	}
}
