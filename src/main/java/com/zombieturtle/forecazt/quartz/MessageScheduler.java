package com.zombieturtle.forecazt.quartz;

import com.zombieturtle.forecazt.jobPostWeek;
import org.quartz.*;
import static com.zombieturtle.forecazt.ForecaZT.*;

public class MessageScheduler {

    public static void test(Integer hour, Integer minute, Integer second) throws SchedulerException {

        JobDetail jobDetail = JobBuilder.newJob(jobPostWeek.class).withIdentity("jobPostWeek", "group1").build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 1 * * * ?"))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
