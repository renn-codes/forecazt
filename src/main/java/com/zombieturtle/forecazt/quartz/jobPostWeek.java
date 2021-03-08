package com.zombieturtle.forecazt.quartz;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.xml.bind.JAXBException;

import static com.zombieturtle.forecazt.ForecaZT.*;
import static com.zombieturtle.forecazt.dataManager.dataMsgBuilder.*;

public class jobPostWeek implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        MessageChannel control = jda.getTextChannelById(botControl);
        MessageChannel channel = jda.getTextChannelById(botWeather);
        try {
            channel.sendMessage(msgBuilder()).queue();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        control.sendMessage("Weather report fired at " + context.getJobRunTime() + ", next runtime is " + context.getScheduledFireTime()).queue();
    }
}
