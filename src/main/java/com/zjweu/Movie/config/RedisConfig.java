package com.zjweu.Movie.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

	/**
	 * 创建jedisPoolConfig中完成一些连接池配置 
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix="spring.redis.pool")//会将前缀相同的内容创建一个实体。后缀即属性 
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
//		config.setMaxIdle(10);// 最大空闲数
//		config.setMinIdle(5);// 最小空闲数
//		config.setMaxTotal(20);// 最大连接数
		return config;
	}

	/**
	 * 配置连接信息
	 * 
	 * @param config
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix="spring.redis")
	public JedisConnectionFactory jedisConnectFactory(JedisPoolConfig config) {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setPoolConfig(config);
//		factory.setHostName("127.0.0.1");
//		factory.setPort(6379);
//		factory.setPassword("123456");
		return factory;
	}

	/**
	 * 用于执行redis操作的方法
	 * 
	 * @param factory
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		return template;
	}
}
