package com.zombieturtle.forecazt.dataManager;

import static com.zombieturtle.forecazt.dataManager.dataNaturalStrings.*;
import static com.zombieturtle.forecazt.dataManager.dataWorkers.*;
import static com.zombieturtle.forecazt.dataManager.beaufortScale.beaufortScale.*;
import com.zombieturtle.forecazt.ForecaZT.*;

import javax.xml.bind.JAXBException;

public class dataMsgBuilder {

    private static String[] msgHolder;

    private static String ZTScale(Integer wind) {

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
        String msg;

        msgHolder[0] = getColonyList(dataHolder.getColony());

        if(dataHolder.getBad()) {
            msgHolder[1] = getBadStuff(dataHolder.getWeather());
        } else if(!dataHolder.getBad()) {
            msgHolder[1] = getWeather(dataHolder.getWeather());
        }

        msgHolder[2] = ZTScale(dataHolder.getWindMph());
        msg = msgHolder[0] + msgHolder[1] + msgHolder[2];
        return msg;
    }

    public void msgSender(int startDay) {
    }
}
