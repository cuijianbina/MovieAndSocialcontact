package com.zjweu.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zjweu.Movie.MovieApplication;
import com.zjweu.Movie.entity.ChatRoom;
import com.zjweu.Movie.service.ChatRoomService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovieApplication.class)
public class ChatRoomServiceTest {

	@Autowired
	private ChatRoomService chatRoomService;

	@Test
	public void save() {
		ChatRoom cr = new ChatRoom();
		cr.setChatRoomName("第二个名字");
		cr.setMovieId(2);
		chatRoomService.saveOrUpdate(cr);
	}
	
	@Test
	public void listAll(){
		List<ChatRoom> crlist = chatRoomService.listAll();
		for(ChatRoom cr:crlist){
			System.out.println(cr);
		}
	}
	
	@Test
	public void findById() throws Exception{
		ChatRoom cr = chatRoomService.findByMovieId(1);
		System.out.println(cr);
	}
}
