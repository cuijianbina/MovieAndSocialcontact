package com.zjweu.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zjweu.Movie.MovieApplication;
import com.zjweu.Movie.service.MessageService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovieApplication.class)
public class MessageServiceTest {

	@Autowired
	private MessageService messageService;
	
	
	@Test
	public void findByAid() throws Exception{
		//System.out.println(messageService.findByAimId(1));
	}
}
