package com.zjweu.Movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zjweu.Movie.common.CtlStatus;
import com.zjweu.Movie.entity.ChatRoom;
import com.zjweu.Movie.service.ChatRoomService;

@CrossOrigin
@RestController
@RequestMapping("/chatRoom")
public class ChatRoomController {

	@Autowired
	private ChatRoomService chatRoomService;

	/**
	 * Description:根据电影编号查找房间
	 *
	 * @param movieId
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月14日 下午4:34:09
	 * @version 1.0
	 */
	@RequestMapping("/findByMovieId")
	public Object list(@RequestParam(value = "movieId", required = true) Integer movieId) throws Exception {
		ChatRoom chatRoom = chatRoomService.findByMovieId(movieId);
		return CtlStatus.success(chatRoom);
	}
}
