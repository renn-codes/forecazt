package com.zombieturtle.forecazt;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;


import com.zombieturtle.forecazt.dataManager.dataDay;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.security.auth.login.LoginException;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

import static com.zombieturtle.forecazt.dataManager.dataMsgBuilder.msgBuilder;
import static com.zombieturtle.forecazt.dataManager.dataUpdater.updateData;

public class ForecaZT extends ListenerAdapter {

    // 'Directive' vars
    public final static String nl = System.getProperty("line.separator");

    // Bot specific vars
    public static String botWeather;
    // public static String botCalendar; Deprecated, likely removing
    public static String botToken;
    public static String botControl;
    public static Integer startTime; // the 0-based time the bot is starting at, passed in via args[3]
    public static Integer currentTime;
    public static Integer hour, minute, second;

    public static ArrayList<dataDay> thisWeek = new ArrayList<dataDay>();

    public static JDA jda;
    public static Scheduler scheduler;


    public static void main(String[] args)
            throws LoginException, SchedulerException, InterruptedException, JAXBException, FileNotFoundException {

        if (args.length < 4) {
            System.out.println("You have to provide the [Token] [WeatherChannelId] [ControlChannelId] [StartTime] [Hour] [Minute] [Second]");
            System.exit(1);
        }
        botToken = args[0];
        botWeather = args[1];
        // botCalendar = args[2]; Deprecated, likely removing
        botControl = args[2];
        startTime = Integer.parseInt(args[3]);
        currentTime = startTime;

        hour = Integer.parseInt(args[4]);
        minute = Integer.parseInt(args[5]);
        second = Integer.parseInt(args[6]);

        // args[0] should be the token
        // We only need 2 intents in this bot. We only respond to messages in guilds and private channels.
        // All other events will be disabled.
        jda = JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new ForecaZT())
                .setActivity(Activity.playing("Starfinder"))
                .build()
                .awaitReady();

        Date startHMS = DateBuilder.todayAt(hour, minute, second);

        // Remove this after commented logic is finished
        updateData();

        /*
        if(startTime == 0) {
            updateData();
        } else if(startTime >= 1 && startTime <= 7){

        }
        */


        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();

        JobDetail jobDetail = JobBuilder.newJob(jobPostWeek.class).withIdentity("jobPostWeek", "group1").build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(second + " " + minute + " " + hour + " * * ?"))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();

        MessageChannel control = jda.getTextChannelById(botControl); //jda.getTextChannelsByName(botWeather, true).get(0);
        control.sendMessage("Starting day: " + currentTime.toString() + nl + "First fire: " + startHMS.toString()).queue();
        String msg = "[ERROR: No weather data!]";

        for(int i = 0; i < 7; i++) {
            try {
                msg = msgBuilder(i);
            } catch (JAXBException e) {
                e.printStackTrace();
            }

            System.out.println(msg + nl + nl);
        }


    }

    private void shutdown(boolean now) throws SchedulerException, InterruptedException {
        MessageChannel control = jda.getTextChannelById(botControl);
        control.sendMessage("Shutting down ForecaZT...").queue();
        Thread.sleep(3000);
        if (!now) {
            scheduler.shutdown(true);
            jda.shutdown();
            System.exit(0);
        } else {
            scheduler.shutdown(false);
            jda.shutdown();
            System.exit(0);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel channel = event.getChannel();
        if (botControl.compareTo(channel.getName()) == 0) {
            switch (msg.getContentRaw()) {
                case "!dismiss":
                    try {
                        shutdown(false);
                    } catch (SchedulerException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case "!dismiss now":
                    try {
                        shutdown(true);
                    } catch (SchedulerException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}

