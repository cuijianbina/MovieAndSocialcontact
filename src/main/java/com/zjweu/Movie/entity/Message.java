package com.zjweu.Movie.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 消息实体类
 * 
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer sendId;// 发送用户Id
	private Integer aimId;// 接受用户Id
	private String messages;
	private Date sendTime;
	private Integer status;// 0是未查看，1是已查看
}
