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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

import static com.zombieturtle.forecazt.dataManager.dataUpdSiren3.updateData;

public class ForecaZT extends ListenerAdapter {

    // 'Directive' vars
    public final static String nl = System.getProperty("line.separator");

    // Bot specific vars
    public static String botWeather;
    public static String botToken;
    public static String botControl;
    public static String botGM;
    public static Integer startTime; // the 0-based time the bot is starting at, passed in via args[3]
    public static Integer currentTime;
    public static Integer totalYears = 2;  // starting on year 2, change to be whatever you need
    public static JDA jda;
    public static Scheduler scheduler;


    public static void main(String[] args)
            throws LoginException, SchedulerException, InterruptedException, JAXBException, FileNotFoundException {

        if (args.length < 5) {
            System.out.println("You have to provide the [Token] [WeatherChannelId] [ControlChannelId] [GMChannelId] [StartTime]");
            System.exit(1);
        }

        // args[0] Discord bot token
        // args[1] Channel Id for weather output
        // args[2] Channel Id for Admin Log output
        // args[3] Channel Id for GM weather preview output
        // args[4] Starting dayweek

        botToken = args[0];
        botWeather = args[1];
        botControl = args[2];
        botGM = args[3];
        startTime = Integer.parseInt(args[4]);
        currentTime = startTime;

        // We only need 2 intents in this bot. We only respond to messages in guilds and private channels.
        // All other events will be disabled.
        jda = JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new ForecaZT())
                .setActivity(Activity.playing("Starfinder"))
                .build()
                .awaitReady();

        while(currentTime > 365) {
            totalYears ++;
            currentTime = currentTime - 365;
        }

        if(isGen()) {
            updateData();
        }

        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();

        JobDetail jobDetail = JobBuilder.newJob(jobPostWeek.class).withIdentity("jobPostWeek", "group1").build();
        JobDetail genDetail = JobBuilder.newJob(jobGenWeek.class).withIdentity("jobGenWeek", "group2").build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("postDay", "group1")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
                .build();

        Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("genWeek", "group2")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 5 0 ? * MON"))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.scheduleJob(genDetail, trigger2);
        scheduler.start();

        String gen;
        if (isGen()) {
            gen = "Yes";
        } else {
            gen = "No";
        }
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        MessageChannel control = jda.getTextChannelById(botControl);
        control.sendMessage("Starting dayweek: " + startTime.toString() + " initiated on " + new SimpleDateFormat("EEEE MM dd YYYY", Locale.ENGLISH).format(date.getTime())  + nl + nl + "postWeek:" + nl + "First fire: " + trigger.getNextFireTime().toString() + nl + nl + "genWeek:" + nl + "First fire: " + trigger2.getNextFireTime().toString() + nl + nl + "Create data on fire?: " + gen).queue();
    }

    public static Boolean isGen() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        Boolean gen = false;
        if (new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()) == "Sun") {
           gen = true;
        }
        return gen;
    }

    // IT#0015 start -----

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

        //IT#0015 End -----
    }
}

