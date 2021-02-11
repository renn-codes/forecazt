package com.zombieturtle.forecazt.dataManager;

import static com.zombieturtle.forecazt.dataManager.dataNaturalStrings.*;
import static com.zombieturtle.forecazt.dataManager.dataWorkers.*;
import static com.zombieturtle.forecazt.dataManager.beaufortScale.beaufortScale.*;
import static com.zombieturtle.forecazt.ForecaZT.nl;

import javax.xml.bind.JAXBException;

public class dataMsgBuilder {



    private static String ZTScale(Integer wind, String msg) {

        String scale = "[ERROR: Negative wind speed]";

        if(wind > 0 && wind < 10) {
            scale = bZT1;
        } else if(wind > 11 && wind < 25) {
            scale = bZT2;
        } else if(wind > 26 && wind < 37) {
            scale = bZT3;
        } else if(wind > 38 && wind < 45) {
            scale = bZT4;
        } else if(wind > 46 && wind < 65) {
            scale = bZT5;
        } else if(wind > 66) {
            scale = bZT6;
        }

        return scale;
    }

    private static String msgBuilder(Integer today) throws JAXBException {
        dataDay dataHolder = loadDay(today);
        String message;
        message = "Here's today's weather for " + getColonyList(dataHolder.getColony()) + ":" + nl;
        StringBuilder msgHolder = new StringBuilder(message);

        if(dataHolder.getBad()) {
            msgHolder.append(getBadStuff(dataHolder.getWeather()));
        } else if(!dataHolder.getBad()) {
            msgHolder.append(getWeather(dataHolder.getWeather()));
        }
        // this next ------->       msgHolder.toString().replace()
        return message;
    }

    public void msgSender(int startDay) {
    }
}
