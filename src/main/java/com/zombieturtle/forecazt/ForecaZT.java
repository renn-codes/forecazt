package com.zombieturtle.forecazt;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import static com.zombieturtle.forecazt.dataManager.dataWorkers.*;
import com.zombieturtle.forecazt.dataManager.dataDay;

import javax.security.auth.login.LoginException;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;

public class ForecaZT extends ListenerAdapter {

    // 'Directive' vars
    public final static String nl = System.getProperty("line.separator");

    // Bot specific vars
    public static String botWeather;
    public static String botCalendar;
    public static String botToken;
    public static Integer startTime; // the 0-based time the bot is starting at, passed in via args[3]

    public static ArrayList<dataDay> thisWeek = new ArrayList<dataDay>();

    public static JDA jda;

    public static void main(String[] args)
            throws LoginException, JAXBException {
        if (args.length < 4) {
            System.out.println("You have to provide the [Token] [WeatherChannel] [CalendarChannel] [StartTime]");
            System.exit(1);
        }
        botToken = args[0];
        botWeather = args[1];
        botCalendar = args[2];
        startTime = Integer.parseInt(args[3]);

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

}

