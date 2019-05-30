package com.zjweu.Movie.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 电影实体类
 * 
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "movie")
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String movieName;// 电影名
	private String coverImg;// 封面图路径
	private Integer status;// 待定，启用（0是启用,1是下架）
	private String playTime;// 时长
	private int searchTime;// 分钟，用于查询
	private String createTime;// 发布时间
	private long hot;// 热度
	private String introduce;// 电影简介
	private Integer type;// 类型
	private String actors;// 主演(多个主演用逗号隔开)
	private String director;// 导演
	private String country;// 国家
	private String path;// 电影路径
	private long pageviews;//浏览量
}
