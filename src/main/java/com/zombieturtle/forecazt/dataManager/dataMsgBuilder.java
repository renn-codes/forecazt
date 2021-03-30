package com.zombieturtle.forecazt.dataManager;

import static com.zombieturtle.forecazt.ForecaZT.currentTime;
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

    public static String msgBuilder(Integer current) throws JAXBException {
        dataDay dataHolder = loadDay(current);

        String message = "[ERROR: Failure constructing message]";
        Integer sweek;
        //if(currentTime >= 26)

        if(dataHolder.getBad()) {
            message = "*Today's weather report for **" + getColonyList(dataHolder.getColony()) + "** comes through over the Infosphere.* " + nl + nl + "---" + nl + nl + "Here's the weather for the %YRth week of the year, the %WKth week of the %SEASON." + nl + nl + getBadStuff(dataHolder.getWeather());
        } else if(!dataHolder.getBad()) {
            message = "*Today's weather report for **" + getColonyList(dataHolder.getColony()) + "** comes through over the Infosphere.* " + nl + nl + "---" + nl + nl + "Here's the weather for the %YRth week of the year, the %WKth week of the %SEASON." + nl + nl + getWeather(dataHolder.getWeather());
        }
        message = message.replace("%WND", getBeaufortScale(ZTScale(dataHolder.getWindMph())))
                .replace("%TMP", getNaturalTemps(dataHolder.getNatTemp()))
                .replace("%YR", currentTime.toString())
                .replace("%WK", sweek.toString())
                .replace("%SEASON", getSeason(dataHolder.getSeason()));
        return message;
    }

}
