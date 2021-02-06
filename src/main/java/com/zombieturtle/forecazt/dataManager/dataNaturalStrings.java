package com.zombieturtle.forecazt.dataManager;

import static com.zombieturtle.forecazt.ForecaZT.nl;

public class dataNaturalStrings {

    private static final String[] naturalWeather = {
            /*{
                    "** Inclement weather **" + nl + nl,
                    "** Inclement weather **" + nl + nl,
                    "** Storms **" + nl + nl,
                    "** Powerful Storms **" + nl + nl,
                    "** Normal weather **" + nl + nl,
                    "** Natural Disaster **" + nl + nl
            },*/
            // {
                    "It's a rainy week, with light rains scattered around the colony and surrounding areas. ",
                    "It's a humid, foggy week,with patches of fog throughout the days as a whole. ",
                    "It's a stormy week, with high changes of light-to-moderate thunderstorms at and around the colony. ",
                    "It's a quite stormy week, with many quite powerful thunderstorms rocking the area. ",
                    "It's a clear, humid and sunny week, perfect for exploration and other adventurous exploits. ",
            // }
    };

    private static final String[] badStuff = {
            // {
                    "There has been a Megafauna sighting near the colony. @Colonists, take caution and report any further sightings. ",
                    "An Earthquake has rocked the area. Make sure to check for any damage to and the integrity of buildings and other structures in the colony. ",
                    "Torrential Rains sweep and batter the colony. @Colonists, take caution for small lakes and deep puddles! ",
                    "A tornado has been sighted and has touched down in the colony. @Colonists, be sure to check and repair any damaged or destroyed structures or technology. ",
                    "An unusual heat wave has set in across the colony. Keep hydrated @Colonists and stay cool!"
            /*},
            {

            }*/
    };

    private static final String[] colonyList = {
            "The week's weather report for Siren-3 pops up in the Infosphere:\n\n",
            "Test Colony\n\n"
    };

    public static String getWeather(Integer index) {
        return naturalWeather[index];
    }

    public static String getBadStuff(Integer index) {
        return badStuff[index];
    }

    public static String getColonyList(Integer index) {
        return colonyList[index];
    }
}
