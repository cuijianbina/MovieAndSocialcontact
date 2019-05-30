package com.zjweu.other;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zjweu.Movie.MovieApplication;
import com.zjweu.Movie.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MovieApplication.class)
public class RedisTest {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	public void addString() {
		redisTemplate.opsForValue().set("k", "jintianshizhousi");
	}

	@Test
	public void addObject() {
		User user = new User();
		user.setUsername("redis序列化测试");
		user.setGender(1);
		user.setMobile("18257633119");
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.opsForValue().set("users", user);
	}

	@Test
	public void getObjectFromRedis() {
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		User user = (User) redisTemplate.opsForValue().get("users");
		System.out.println(user);
	}

	@Test
	public void objectToJsonRedis() {
		User user = new User();
		user.setUsername("王五疯");
		user.setMobile("123123123");
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(User.class));
		redisTemplate.opsForValue().set("user_json", user);
	}

	@Test
	public void getDataFromRedis() {
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(User.class));
		System.out.println(redisTemplate.opsForValue().get("user_json"));
	}
}
