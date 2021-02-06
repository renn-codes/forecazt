package com.zombieturtle.forecazt.dataManager;

import javax.xml.bind.JAXBException;

import static com.zombieturtle.forecazt.ForecaZT.*;
import static com.zombieturtle.forecazt.dataManager.dataWorkers.*;

public class dataUpdater {

    private static dataDay dataHolder = new dataDay();
    private static String[] weekOut;

    private static Integer w;
    private static String d;
    private static Boolean bad;

    public static void updateData() throws JAXBException {
        if (startTime == 0) {
            generateWeek();
        }
    }

    private static void generateWeek() throws JAXBException {
        w = thisWeek.get(0).getRuntime(); // get prev week/runtime
        d = getSysDate(); //get today's date

        // Generate Weather per dayweek
        for (int i = 1; i <= 7; i++) {
            Integer ww = w + 1;
            genSeason(ww);
            genWeather();
            dataHolder.setRuntime(ww);
            dataHolder.setDate(d);
            saveDay(dataHolder, ww);
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
        double mph = Math.floor(Math.random() * 10) + 1;

        // Generate and commit our temperatures
        //Get high
        double high = Math.floor(Math.random() * 15)+80;

        //Get Low
        double low = Math.floor(Math.random() * 15)+65;
        while(low == high) {
            low = Math.floor(Math.random() * 15) + 65;
        }
        dataHolder.setHigh((int) high);
        dataHolder.setLow((int) low);

        //Weather types: [5] (Normal) 60%, [+] (Occurrence) 40%
        //Occurrence Types: [1 & 2] (Inclement) 70%, [3] (Storm) 15%, [4] (Powerful Storm) 10%, [set later] (BAD) 5%

        //Normal weather
        if (Math.floor(Math.random() * 100) + 1 <= 60) {
            dataHolder.setWeather(5);
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
                dataHolder.setWeather(5);
                bad = false;
                dataHolder.setBad(false);
            }
        }
        double kph = Math.round((mph*1.6));
        dataHolder.setWindKph((int) kph);
    }

    private static void genBadStuff() {
        //Percentage Generator
        double percentage = Math.floor(Math.random() * 100) + 1;
        //console.log(percentage);

        //Wet Season
        if(dataHolder.getSeason() == 1) {
            //Megafauna
            if(percentage >= 1 && percentage <= 30) {
                dataHolder.setWeather(1);
            }
            //Earthquake
            if(percentage >= 31 && percentage <= 50) {
                dataHolder.setWeather(2);
            }
            //Torrential Rains
            if(percentage >= 51 && percentage <= 80) {
                dataHolder.setWeather(3);
            }
            //Tornado
            if(percentage >= 81) {
                double mph = dataHolder.getWindMph() + Math.floor(Math.random() * 60) + 70;
                dataHolder.setWindMph((int) mph);
                dataHolder.setWeather(4);
            }
        }
        //Dry Season
        if(dataHolder.getSeason() == 2) {
            //Megafauna
            if(percentage >= 1 && percentage <= 30) {
                dataHolder.setWeather(1);
            }
            //Earthquake
            if(percentage >= 31 && percentage <= 50) {
                dataHolder.setWeather(2);
            }
            //Record Heat
            if(percentage >= 51 && percentage <= 80) {
                double high = dataHolder.getHigh() + 15;
                double low = dataHolder.getLow() + 15;
                dataHolder.setHigh((int) high);
                dataHolder.setLow((int) low);
                dataHolder.setWeather(5);
            }
            //Tornado
            if(percentage >= 81) {
                double mph = dataHolder.getWindMph() +  Math.floor(Math.random() * 60) + 70;
                dataHolder.setWindMph((int) mph);
                dataHolder.setWeather(4);
            }
        }
    }
}
