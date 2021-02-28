package com.zombieturtle.forecazt.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class MessageScheduler {

    public static Scheduler scheduler;

    public static void test(Integer hour, Integer minute, Integer second) throws SchedulerException {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        JobDetail jobDetail = JobBuilder.newJob(jobPostWeek.class).withIdentity("jobPostWeek", "group1").build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger", "group1")
                .withIdentity("myTrigger", "group1")
                .startAt(DateBuilder.todayAt(hour, minute, second))
                .withSchedule(CronScheduleBuilder.cronSchedule("2/2 * * * *"))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
