package com.zjweu.Movie.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 电影类型实体类
 * 
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "movie_type")
public class MovieType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String type; // 类型名
}
