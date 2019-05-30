package com.zjweu.service;

import com.zjweu.Movie.MovieApplication;
import com.zjweu.Movie.common.DateCommon;
import com.zjweu.Movie.entity.User;
import com.zjweu.Movie.service.AdminUserService;
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
public class AdminUserServiceTest {

	@Autowired
	private AdminUserService adminUserService;

	@Test
	public void find() throws Exception {
		int count = adminUserService.countUser();
		System.out.println(count+"......");
	}

}
