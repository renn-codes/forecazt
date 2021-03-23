package com.zombieturtle.forecazt;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.xml.bind.JAXBException;

import static com.zombieturtle.forecazt.ForecaZT.*;
import static com.zombieturtle.forecazt.dataManager.dataMsgBuilder.*;

public class jobPostWeek implements Job {

    public void execute(JobExecutionContext context) {
        MessageChannel control = jda.getTextChannelById(botControl);
        MessageChannel channel = jda.getTextChannelById(botWeather);
        String msg = "[ERROR: No weather data!]";

        try {
            msg = msgBuilder(currentTime);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        System.out.println(msg);

        channel.sendMessage(msg).queue();
        control.sendMessage("Weather report fired at " + context.getFireTime() + ", next runtime is " + context.getNextFireTime()).queue();
        currentTime++;
    }
}
