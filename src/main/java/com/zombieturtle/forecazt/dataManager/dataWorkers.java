package com.zombieturtle.forecazt.dataManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class dataWorkers {


    public static dataDay loadDay(Integer runtime) throws JAXBException {
        String filename = "FZTDATA-" + runtime + ".xml";
        File file = new File(filename);
        JAXBContext jaxbContext = JAXBContext.newInstance(dataDay.class);
        dataDay data;

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        data = (dataDay) jaxbUnmarshaller.unmarshal(file);
        return data;
    }

    public static void saveDay(dataDay data, Integer runtime) throws JAXBException {
        String filename = "FZTDATA-" + runtime + ".xml";
        File file = new File(filename);
        JAXBContext context = JAXBContext.newInstance(dataDay.class);
        Marshaller mar= context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(data, file);
    }

    public static Integer getSysTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime localTime = localDateTime.toLocalTime();
        return localTime.toSecondOfDay();
    }

    public static String getSysDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate.toString();
    }


}
