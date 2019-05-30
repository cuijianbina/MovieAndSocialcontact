package com.zjweu.Movie.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 聊天室
 * 
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "chatroom")
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer movieId;// 电影Id
	private String chatRoomName;// 聊天室名
	private String userIds;// 当前聊天室的用户，多个用户的话保存Id,中间用“，”隔开
}
