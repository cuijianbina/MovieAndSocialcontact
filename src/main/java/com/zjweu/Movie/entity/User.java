package com.zjweu.Movie.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 用户实体类
 * 
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "user")
public class User implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String username;
	private String password;
	private String mobile;// 手机号
	private String idNumber;// 身份证
	private Integer gender;// 0是男，1是女
	private String logo;// 头像
	private Integer status;// 用户禁言状态(0是启用，1是停用)
	private Date registerTime; // 注册时间
	private int sendCount;// 发送消息数
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastLoginTime;// 最后的登录时间
	private String token;
}
