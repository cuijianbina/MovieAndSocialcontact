package com.zjweu.Movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Description:提供直接操作redis服务<BR>
 *
 * @version 1.0
 */
@Service
public class RedisService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * Description:设置缓存(若图片验证码设置imgCode，手机验证码设置phoneCode)
	 *
	 * @param imgCode
	 * @author cui.jianbin
	 * @date 2019年4月29日 下午5:00:11
	 * @version 1.0
	 */
	public void set(String key, String imgCode) {
		redisTemplate.opsForValue().set(key, imgCode);
	}

	/**
	 * Description:通过Key获取缓存
	 *
	 * @param key
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月29日 下午5:02:29
	 * @version 1.0
	 */
	public String get(String key) {
		return (String) redisTemplate.opsForValue().get(key);
	}
	
	
}
