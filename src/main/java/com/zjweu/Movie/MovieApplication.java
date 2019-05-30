package com.zjweu.Movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class }) // 忽略数据库配置
@ServletComponentScan("com.zjweu.Movie") // 在springboot启动时扫描webServlet
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class MovieApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieApplication.class, args);
	}

//	@Bean
//	public MultipartConfigElement multipartConfigElement() {
//		MultipartConfigFactory factory = new MultipartConfigFactory();
//		// 单个文件最大
//		factory.setMaxFileSize("10240KB"); // KB,MB
//		/// 设置总上传数据总大小
//		factory.setMaxRequestSize("102400KB");
//		return factory.createMultipartConfig();
//	}

}
