package com.zjweu.service;

import com.zjweu.Movie.MovieApplication;
import com.zjweu.Movie.common.DateCommon;
import com.zjweu.Movie.entity.User;
//import com.zjweu.Movie.service.UserService;
import com.zjweu.Movie.service.UserService;
import com.zjweu.Movie.util.MD5Util;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovieApplication.class)
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Test
	public void save() throws Exception {
		User user = new User();
		user.setUsername("新的测试111");
		user.setPassword(MD5Util.encode("88888888"));
		user.setMobile("18257633111");
		user.setLogo("e:/1.jpg");
		user.setStatus(0);
		user.setRegisterTime(DateCommon.getTime());
		userService.saveOrUpdate(user);
	}

	@Test
	public void exist() throws Exception {
		System.out.println(userService.existUsername("1"));
	}

	@Test
	public void findByUsername() throws Exception {
		System.out.println(userService.findByUsername("1"));
	}

	@Test
	public void findById() throws Exception {
		System.out.println(userService.findById(1));
	}

	@Test
	public void findByUsernameOrMobile() throws Exception {
		System.out.println(userService.findByUsernameOrMobile("2222222"));
	}
}
