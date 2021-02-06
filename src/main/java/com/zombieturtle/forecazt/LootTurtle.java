package com.zombieturtle.forecazt;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LootTurtle extends ListenerAdapter {

    public final String nl = System.getProperty("line.separator");

    public static String botChannel;
    public static String listChannel;

    public static void main(String[] args)
        throws LoginException
    {
        if (args.length < 2) {
            System.out.println("You have to provide the token and channel.");
            System.exit(1);
        }



        listChannel = args[2];
        botChannel = args[1];
        // args[0] should be the token
        // We only need 2 intents in this bot. We only respond to messages in guilds and private channels.
        // All other events will be disabled.
        JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new LootTurtle())
                .setActivity(Activity.playing("Pathfinder 2e"))
                .build();
    }

    public boolean isNum(Object obj, MessageChannel channel) {
        Integer test = Integer.parseInt(obj.toString());
        if (test <=0 ) {
            channel.sendMessage("Lurtle cannot use a letter as a number...");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event){

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel channel = event.getChannel();
        MessageChannel output = event.getGuild().getTextChannelsByName(listChannel,true).get(0);
        String[] parts = msg.getContentRaw().split(" ", 2);
        if(botChannel.compareTo(channel.getName()) == 0) {
            switch (parts[0]) {
                case "!init":
                    output.sendMessage("Lurtle is currently holding:" + nl + "================================================").queue();
                    channel.sendMessage("*~happy turtle noises~*").queue();
                    break;
                case "!loothelp":
                    channel.sendMessage("You can speak in **turtle** to Lurtle by typing:" + nl + "**loothelp** *(displays this message)*" + nl + "**!poke** - Pokes Lurtle" + nl + "**!addloot** *<amount> <item desc>* - Adds loot to the list, with the specified description" + nl + "**!remloot** *<line number> <amount to remove>* - Removes the specified amount of loot at the chosen line number, and deletes the item if all of it is removed" + nl + "**!delloot** *<line number>* - Deleted the Item at the specified line" + nl + "**!dismiss** - **For developer use only!!!!** Shuts down the bot." + nl + nl+  "*~starts making various turtle noises~*").queue();
                    break;
                case "!turtle":
                    channel.sendMessage("Congrats, you now have spoken in **turtle**!").queue();
                    break;
                case "!poke":
                    channel.sendMessage("It's literally just an adorable !turtle with a backpack...don't expect much :3").queue();
                    break;
                case "!addloot":
                    if(parts.length != 2) {
                        channel.sendMessage("Lurtle looks at you confused.").queue();
                    }else{
                        String[] partsub = parts[1].split(" ", 2);
                        if(partsub.length != 2) {
                            channel.sendMessage("Lurtle looks at you blankly").queue();
                        }else{
                            String id = output.getLatestMessageId();
                            output.getHistoryFromBeginning(1).queue(history ->
                            {
                                if (!history.isEmpty()) {
                                    Message firstMsg = history.getRetrievedHistory().get(0);
                                    List<String> items = Arrays.asList(firstMsg.getContentDisplay().split(nl));
                                    Integer size = items.size() + -1;
                                    if(isNum(partsub[0], channel) == true) {
                                        String added = firstMsg.getContentDisplay() + nl + size.toString() + ". -- <" + partsub[0] + "x> " + partsub[1];
                                        output.editMessageById(id, added).queue();
                                    }
                                }

                            });
                        }
                    }
                    break;
                case "!delloot":
                    if(parts.length != 2) {
                        channel.sendMessage("Lurtle looks at you confused.").queue();
                    } else {
                        output.getHistoryFromBeginning(1).queue(history ->
                        {
                            if (!history.isEmpty()) {
                                String id = output.getLatestMessageId();
                                Message firstMsg = history.getRetrievedHistory().get(0);
                                ArrayList<String> items = new ArrayList<>(Arrays.asList(firstMsg.getContentDisplay().split(nl)));
                                int index = Integer.valueOf(parts[1]) + 1;

                                items.remove(index);
                                if(Integer.valueOf(parts[1]) != 1) {
                                    for (int i = index; i < items.size(); i++) {
                                        Integer ii = i - 1;
                                        String content[] = items.get(i).split(". -- <");
                                        String newItem = ii + ". -- <" + content[1];
                                        items.set(i, newItem);
                                    }
                                }
                                StringBuilder newMsg = new StringBuilder("Lurtle is currently holding:" + nl + "================================================");
                                if(items.size() > 2) {
                                    for (int i = 2; i < items.size() || items.size() == 2; i++) {
                                        newMsg.append(nl + items.get(i));
                                    }
                                }
                                output.editMessageById(id, newMsg).queue();
                            }
                        });
                    }
                    break;
                case "!remloot":
                    if(parts.length != 2) {
                        channel.sendMessage("Lurtle looks at you confused.").queue();
                    }else {
                        String[] partsub = parts[1].split(" ", 2);
                        if (partsub.length != 2) {
                            channel.sendMessage("Lurtle looks at you blankly").queue();
                        } else {
                            output.getHistoryFromBeginning(1).queue(history ->
                            {
                                if (!history.isEmpty()) {
                                    String id = output.getLatestMessageId();
                                    Message firstMsg = history.getRetrievedHistory().get(0);
                                    ArrayList<String> items = new ArrayList<>(Arrays.asList(firstMsg.getContentDisplay().split(nl)));
                                    int index = Integer.valueOf(partsub[0]) + 1;
                                    int qtyrm = Integer.valueOf(partsub[1]);
                                    int realIndex = index - 1;

                                    ArrayList<String> divIt = new ArrayList<>(Arrays.asList(items.get(index).split(". -- <")));

                                    ArrayList divItMore = new ArrayList<>(Arrays.asList(divIt.get(1).split("x>")));

                                    String conv = divItMore.get(0).toString();
                                    Integer qtyold = Integer.parseInt(conv);
                                    if (qtyrm >= qtyold && items.size() > 2) {
                                        items.remove(index);
                                        if(Integer.valueOf(partsub[0]) != 1) {
                                            for (int i = index; i < items.size(); i++) {
                                                Integer ii = i - 1;
                                                String content[] = items.get(i).split(". -- <");
                                                String newItem = ii + ". -- <" + content[1];
                                                items.set(i, newItem);
                                            }
                                        }
                                    }
                                    if(qtyrm < qtyold && items.size() > 2) {
                                        Integer qtynew = qtyold - qtyrm;
                                        String newItem = realIndex + ". -- <" + qtynew + "x> " + divItMore.get(1);
                                        items.set(index, newItem);
                                    }
                                    StringBuilder newMsg = new StringBuilder("Lurtle is currently holding:" + nl + "================================================");
                                    if(items.size() > 2) {
                                        for (int i = 2; i < items.size() || items.size() == 2; i++) {
                                            newMsg.append(nl + items.get(i));
                                        }
                                    }
                                    output.editMessageById(id, newMsg).queue();
                                }
                            });
                        }
                    }
                    break;
                case "!dismiss":
                    channel.sendMessage("They thank you for the adventure politely in turtle, then scuttle away.").queue();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(1);
                    break;
            }
        }
    }
}
