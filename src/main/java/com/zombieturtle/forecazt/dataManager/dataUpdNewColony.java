package com.zombieturtle.forecazt.dataManager;

import net.dv8tion.jda.api.entities.MessageChannel;

import javax.xml.bind.JAXBException;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.zombieturtle.forecazt.ForecaZT.*;
import static com.zombieturtle.forecazt.dataManager.dataWorkers.*;
import static com.zombieturtle.forecazt.dataManager.dataMsgBuilder.*;

public class dataUpdNewColony {

    private static dataDay dataHolder = new dataDay();

    private static Integer w;
    private static Boolean bad = false;

    public static void updateData() throws JAXBException, FileNotFoundException {
        MessageChannel control = jda.getTextChannelById(botControl);
        if (startTime == 0) {
            generateWeek(true);
        } else if(startTime >= 1) {
            generateWeek(false);
        }
        Integer begin = currentTime + 1;
        Integer end = currentTime + 7;
        control.sendMessage("Dayweek XML generation completed for weeks " + begin.toString() + " though " + end.toString());
    }

    private static void generateWeek(boolean firstRun) throws JAXBException, FileNotFoundException {
        if(!firstRun) {
            w = currentTime + 1; // get prev dayweek/runtime
        } else if(firstRun) {
            w = 0;
        }
        String d = getSysDate(); //get today's date
        MessageChannel gm = jda.getTextChannelById(botGM);
        // Generate Weather per dayweek"
        gm.sendMessage("Weekly weather preview" + nl + "---");
        for (int i = 1; i <= 7; i++) {
            dataHolder.setColony(0);
            genSeason(w);
            genWeather();
            genNatTemp();
            dataHolder.setRuntime(w);
            dataHolder.setDate(d);
            saveDay(dataHolder, w);
            Calendar cal = Calendar.getInstance();
            Date day = cal.getTime();
            gm.sendMessage("**" + new SimpleDateFormat("M d yyyy", Locale.ENGLISH).format(day.getTime()) + "**" + nl + "---");
            gm.sendMessage(msgBuilder(w));
            w = w + 1;
        }
    }

    //ww is current week
    private static void genSeason(Integer ww) {
        if (ww + 1 >= 1 && ww <= 26) {
            dataHolder.setSeason(1);
        } else if (ww >= 27 && ww <= 52) {
            dataHolder.setSeason(2);
        }
    }

    private static void genWeather() {
        // Generate our wind speed
        double mph = Math.floor(Math.floor(Math.random() * 37) + 1);
        dataHolder.setWindMph((int) mph);
        // Generate and commit our temperatures
        //Get high
        double high = Math.floor(Math.random() * 15) + 80;

        //Get Low
        double low = Math.floor(Math.random() * 15) + 65;
        while(low == high) {
            low = Math.floor(Math.random() * 15) + 65;
        }

        double average = (high + low) / 2;
        dataHolder.setHigh((int) high);
        dataHolder.setLow((int) low);
        dataHolder.setTemp((int) average);

        //Weather types: [5] (Normal) 60%, [+] (Occurrence) 40%
        //Occurrence Types: [1 & 2] (Inclement) 70%, [3] (Storm) 15%, [4] (Powerful Storm) 10%, [set later] (BAD) 5%

        //Normal weather
        if (Math.floor(Math.random() * 100) + 1 <= 60) {
            dataHolder.setWeather(0);
            dataHolder.setBad(false);
        }
        //Occurrence
        else {
            double percentage = Math.floor(Math.random() * 100) + 1;
            //Inclement
            if (percentage <= 97) {

                //Check for type by season
                percentage = Math.floor(Math.random() * 100) + 1;
                //Wet Season
                if (dataHolder.getSeason() == 1) {
                    //Rain
                    if (percentage <= 60) {
                        dataHolder.setWeather(1);
                        dataHolder.setBad(false);
                    }
                    //Fog
                    else if (percentage >= 61 && percentage <= 70) {
                        dataHolder.setWeather(2);
                        dataHolder.setBad(false);
                    }
                    //Storm
                    else if (percentage >= 71 && percentage <= 86) {
                        mph = mph + 5;
                        dataHolder.setWindMph((int) mph);
                        dataHolder.setWeather(3);
                        dataHolder.setBad(false);
                    }
                    //Powerful Storm
                    else if (percentage >= 87 && percentage <= 100) {
                        mph = mph + 8;
                        dataHolder.setWindMph((int) mph);
                        dataHolder.setWeather(4);
                        dataHolder.setBad(false);
                    }
                }
                //Dry Season
                else if (dataHolder.getSeason() == 2) {
                    //Rain
                    if (percentage <= 60) {
                        dataHolder.setWeather(1);
                        dataHolder.setBad(false);
                    }
                    //Fog
                    else if (percentage >= 61 && percentage <= 79) {
                        dataHolder.setWeather(2);
                        dataHolder.setBad(false);
                    }
                    //Storm
                    else if (percentage >= 80 && percentage <= 100) {
                        mph = mph + 5;
                        dataHolder.setWindMph((int) mph);
                        dataHolder.setWeather(3);
                        dataHolder.setBad(false);
                    }
                }
            }
            //BAD
            else if (percentage >= 98 && percentage <= 100 && !bad) {
                bad = true;
                genBadStuff();
            }
            //BAD already triggered
            else if (percentage >= 98 && percentage <= 100 && bad) {
                dataHolder.setWeather(0);
                bad = false;
                dataHolder.setBad(false);
            }
        }
        //double kph = Math.round((mph*1.6));
        //dataHolder.setWindKph((int) kph);
    }

    private static void genBadStuff() {
        //Percentage Generator
        double percentage = Math.floor(Math.random() * 100) + 1;
        //console.log(percentage);

        //Wet Season
        if(dataHolder.getSeason() == 1) {
            //Megafauna
            if(percentage >= 1 && percentage <= 30) {
                dataHolder.setWeather(0);
            }
            //Earthquake
            if(percentage >= 31 && percentage <= 50) {
                dataHolder.setWeather(1);
            }
            //Torrential Rains
            if(percentage >= 51 && percentage <= 80) {
                dataHolder.setWeather(2);
            }
            //Tornado
            if(percentage >= 81) {
                double mph = dataHolder.getWindMph() + Math.floor(Math.random() * 60) + 70;
                dataHolder.setWindMph((int) mph);
                dataHolder.setWeather(3);
            }
        }
        //Dry Season
        if(dataHolder.getSeason() == 2) {
            //Megafauna
            if (percentage >= 1 && percentage <= 30) {
                dataHolder.setWeather(0);
            }
            //Earthquake
            if (percentage >= 31 && percentage <= 50) {
                dataHolder.setWeather(1);
            }
            //Record Heat
            if (percentage >= 51 && percentage <= 80) {
                double high = dataHolder.getHigh() + 15;
                double low = dataHolder.getLow() + 15;
                double average = (high + low) / 2;
                dataHolder.setHigh((int) high);
                dataHolder.setLow((int) low);
                dataHolder.setTemp((int) average);
                dataHolder.setWeather(4);
            }
            //Tornado
            if (percentage >= 81) {
                double mph = dataHolder.getWindMph() + Math.floor(Math.random() * 60) + 70;
                dataHolder.setWindMph((int) mph);
                dataHolder.setWeather(3);
            }
        }
    }

    private static void genNatTemp() {
        if (dataHolder.getTemp() <= 0) {
            dataHolder.setNatTemp(0);
        }
        if (dataHolder.getTemp() >= 1 && dataHolder.getTemp() <= 35) {
            dataHolder.setNatTemp(1);
        }
        if (dataHolder.getTemp() >= 36 && dataHolder.getTemp() <= 55) {
            dataHolder.setNatTemp(2);
        }
        if (dataHolder.getTemp() >= 56 && dataHolder.getTemp() <= 70) {
            dataHolder.setNatTemp(3);
        }
        if (dataHolder.getTemp() >= 71 && dataHolder.getTemp() <= 99) {
            dataHolder.setNatTemp(4);
        }
        if (dataHolder.getTemp() >= 100) {
            dataHolder.setNatTemp(5);
        }
    }

}
