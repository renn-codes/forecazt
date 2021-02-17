package com.zombieturtle.forecazt.dataManager;

import com.zombieturtle.forecazt.ForecaZT;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import static com.zombieturtle.forecazt.dataManager.dataNaturalStrings.*;
import static com.zombieturtle.forecazt.dataManager.dataWorkers.*;
import static com.zombieturtle.forecazt.ForecaZT.nl;
import static com.zombieturtle.forecazt.ForecaZT.jda;

import javax.xml.bind.JAXBException;

public class dataMsgBuilder {



    private static Integer ZTScale(Integer wind) {

        Integer scale = 0;

        if(wind > 0 && wind < 10) {
            scale = 1;
        } else if(wind > 11 && wind < 25) {
            scale = 2;
        } else if(wind > 26 && wind < 37) {
            scale = 3;
        } else if(wind > 38 && wind < 45) {
            scale = 4;
        } else if(wind > 46 && wind < 65) {
            scale = 5;
        } else if(wind > 66) {
            scale = 6;
        }

        return scale;
    }

    private static String msgBuilder(Integer today) throws JAXBException {
        dataDay dataHolder = loadDay(today);
        String message = "%WND %TMP";

        if(dataHolder.getBad()) {
            message = "Here's today's weather for " + getColonyList(dataHolder.getColony()) + ":" + nl + getBadStuff(dataHolder.getWeather());
        } else if(!dataHolder.getBad()) {
            message = "Here's today's weather for " + getColonyList(dataHolder.getColony()) + ":" + nl + getWeather(dataHolder.getWeather());
        }
        message = message.replace("%WND", getBeaufortScale(ZTScale(dataHolder.getWindMph())))
            .replace("%TMP", dataHolder.getHigh().toString());
        return message;
    }

    public void msgSender(int startDay, String chnl) throws JAXBException, NullPointerException {

        TextChannel txtChannel = jda.getTextChannelById(chnl);

        //assert txtChannel != null;
        if (txtChannel.canTalk()) {
            txtChannel.sendMessage(msgBuilder(startDay)).queue();
        } else {
            System.out.println("[ERROR] Cannot talk here!");
        }
    }
}
