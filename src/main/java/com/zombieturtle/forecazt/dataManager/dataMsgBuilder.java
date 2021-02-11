package com.zombieturtle.forecazt.dataManager;

import static com.zombieturtle.forecazt.dataManager.dataNaturalStrings.*;
import static com.zombieturtle.forecazt.dataManager.dataWorkers.*;
import static com.zombieturtle.forecazt.ForecaZT.nl;

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
        String message;
        message = "Here's today's weather for " + getColonyList(dataHolder.getColony()) + ":" + nl;
        StringBuilder msgHolder = new StringBuilder(message);

        if(dataHolder.getBad()) {
            msgHolder.append(getBadStuff(dataHolder.getWeather()));
        } else if(!dataHolder.getBad()) {
            msgHolder.append(getWeather(dataHolder.getWeather()));
        }
        msgHolder.toString().replace("%WND", getBeaufortScale(ZTScale(dataHolder.getWindMph())));
        msgHolder.toString().replace("%TMP", dataHolder.getHigh().toString());
        return msgHolder.toString();
    }

    public void msgSender(int startDay) {

    }
}
