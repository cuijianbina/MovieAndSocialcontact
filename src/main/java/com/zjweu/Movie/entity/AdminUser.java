package com.zjweu.Movie.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 管理员实体类
 * 
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "admin_user")
public class AdminUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String username;
	private String password;
}
