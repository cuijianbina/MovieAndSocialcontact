package com.zjweu.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

import com.zjweu.base.BaseTest;

public class UserControllerTest extends BaseTest{
	
	@Test
	public void testFindAll() throws Exception{
		String uri = "/user/findAll";
		String contentAsString = this.mockMvc
				.perform(post(uri).session(session))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println(formatJson(contentAsString));
	}
}
