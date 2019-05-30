package com.zjweu.Movie.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zjweu.Movie.common.CtlStatus;
import com.zjweu.Movie.entity.MovieType;
import com.zjweu.Movie.service.TypeService;

@CrossOrigin
@RestController
@RequestMapping("/type")
public class TypeController {

	@Autowired
	private TypeService typeService;

	/**
	 * Description:显示所有的电影类型
	 *
	 * @param request
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月25日 上午11:05:38
	 * @version 1.0
	 */
	@RequestMapping("/listAll")
	public Object listAll(HttpServletRequest request) {
		List<MovieType> list = typeService.listAll();
		return CtlStatus.success(list);
	}
}
