package com.zombieturtle.forecazt;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;


import static com.zombieturtle.forecazt.MessageScheduler.*;
import static com.zombieturtle.forecazt.dataManager.dataWorkers.*;
import com.zombieturtle.forecazt.dataManager.dataDay;
import org.quartz.SchedulerException;

import javax.security.auth.login.LoginException;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;

public class ForecaZT extends ListenerAdapter {

    // 'Directive' vars
    public final static String nl = System.getProperty("line.separator");

    // Bot specific vars
    public static String botWeather;
    // public static String botCalendar; Deprecated, likely removing
    public static String botToken;
    public static String botControl;
    public static Integer startTime; // the 0-based time the bot is starting at, passed in via args[3]

    public static ArrayList<dataDay> thisWeek = new ArrayList<dataDay>();

    public static JDA jda;

    public static void main(String[] args)
            throws LoginException, JAXBException {
        if (args.length < 4) {
            System.out.println("You have to provide the [Token] [WeatherChannel] [ControlChannel] [StartTime]");
            System.exit(1);
        }
        botToken = args[0];
        botWeather = args[1];
        // botCalendar = args[2]; Deprecated, likely removing
        botControl = args[2];
        startTime = Integer.parseInt(args[4]);

        // args[0] should be the token
        // We only need 2 intents in this bot. We only respond to messages in guilds and private channels.
        // All other events will be disabled.
        jda = JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new ForecaZT())
                .setActivity(Activity.playing("Starfinder"))
                .build();

        if (startTime > 0 && startTime < 7) {
            System.out.println("IT#0005");// IT#0005
        } else if (startTime >= 7) {
            for (int i = startTime - 6; i <= startTime; i++) {
                thisWeek.add(loadDay(i));
                System.out.println(loadDay(i).getRuntime());
            }
        } else if (startTime == 0) {
            thisWeek.add(loadDay(0));
        }
    }

    private void shutdown(boolean now) throws SchedulerException, InterruptedException {
        MessageChannel control = jda.getTextChannelsByName(botControl, true).get(0);
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

}

