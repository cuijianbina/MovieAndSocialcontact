package com.zjweu.service;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zjweu.Movie.MovieApplication;
import com.zjweu.Movie.common.Page;
import com.zjweu.Movie.entity.Movie;
import com.zjweu.Movie.service.MovieService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovieApplication.class)
public class MovieServiceTest {

	@Autowired
	private MovieService movieService;

	@Test
	public void listAll() throws Exception {
//		Page<Map<String, Object>> result = movieService.findAll(0, 2);
//		System.out.println(result.getLdata().toString());
	}

	@Test
	public void save() {
		Movie movie = new Movie();
		movie.setActors("周星驰，吴孟达，朱茵");
		movie.setCountry("中国");
		movie.setCoverImg("f:2.jpg");
		//movie.setCreateTime(new Date());
		movie.setDirector("周星驰");
		movie.setHot(10000000);
		movie.setIntroduce("这是一部可以让你痛哭流涕的电影");
		movie.setMovieName("大话西游");
		movie.setStatus(0);
		movie.setType(1);
		movieService.saveOrUpdate(movie);
	}

}
