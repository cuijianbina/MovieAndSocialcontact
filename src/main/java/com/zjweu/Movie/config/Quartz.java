package com.zjweu.Movie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.zjweu.Movie.quartz.QuartzDemo;

@Configuration
public class Quartz {
	/**
	 * 创建Job对象
	 */
	@Bean
	public JobDetailFactoryBean jobDetailFactoryBean() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		// 关联我们自己的JOb对象
		factory.setJobClass(QuartzDemo.class);
		return factory;
	}

	/**
	 * 创建trigger对象
	 */
	// @Bean
	// public SimpleTriggerFactoryBean
	// simpleTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean) {
	// SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
	// // 关联JobDetail
	// factory.setJobDetail(jobDetailFactoryBean.getObject());
	// factory.setRepeatInterval(2000);// 传入毫秒数
	// factory.setRepeatCount(5);// 执行的次数
	// return factory;
	// }

	@Bean
	public CronTriggerFactoryBean cronTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean) {
		CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
		factory.setJobDetail(jobDetailFactoryBean.getObject());
		factory.setCronExpression("0/2 30 * * * ?");
		return factory;
	}

	/**
	 * 创建scheduler对象
	 */
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(CronTriggerFactoryBean cronTriggerFactoryBean) {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setTriggers(cronTriggerFactoryBean.getObject());
		return factory;
	}
}
