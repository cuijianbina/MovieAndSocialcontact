package com.zjweu.Movie.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzMain {

	public static void main(String[] args) throws Exception {
		JobDetail job = JobBuilder.newJob(QuartzDemo.class).build();

		//两种方式
		// Trigger trigger =
		// TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever())
		// .build();
		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
				.build();

		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		scheduler.scheduleJob(job, trigger);

		//scheduler.start();
	}
}
