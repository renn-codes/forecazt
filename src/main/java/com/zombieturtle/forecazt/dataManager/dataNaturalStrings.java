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
                    "It's a %TMP and clear, sunny week, %WND. It's perfect for exploration, research, and other adventurous exploits. ",
                    "It's a %TMP and rainy week, %WND. There will be light rains scattered around the colony and surrounding areas. ",
                    "It's a %TMP and quite foggy week, %WND. There will be patchy fog throughout the week as a whole. ",
                    "It's a %TMP and stormy week, %WND. There will be high chances of light-to-moderate thunderstorms at and around the colony. ",
                    "It's a %TMP and quite stormy week, %WND. There is the strong possibility of many quite powerful thunderstorms rocking the area. ",
            // }
    };

    private static final String[] badStuff = {
            // {
                    "It's a %TMP and clear week, %WND. There has been a **Megafauna** sighting near the colony. @Colonists, take caution and report any further sightings. ",
                    "It's a %TMP and clear week, %WND. An **Earthquake** has rocked the area. Make sure to check for any damage to and the integrity of buildings and other structures in the colony. ",
                    "It's a %TMP and wet week, %WND. **Torrential Rains** sweep and batter the colony. @Colonists, take caution for small lakes and deep puddles! ",
                    "It's a %TMP and cloudy week, %WND. A **Tornado** has been sighted and has touched down in the colony. @Colonists, be sure to check and repair any damaged or destroyed structures or technology. ",
                    "It's a %TMP and clear, sunny week, %WND. An unusual heat wave has set in across the colony. Keep hydrated @Colonists and stay cool!"
            /*},
            {

            }*/
    };

    private static final String[] naturalTemps = {
            "freezing",
            "cold",
            "chilly",
            "warm",
            "hot",
            "sweltering"
    };

    private static final String[] beaufortScale = {
            "with light, soft breezes",
            // Beaufort Scale 3/4/5
            "rather breezy with some of the smaller trees beginning to sway",
            // Beaufort Scale 6/7
            ". Near-to-gale force winds wisp through the area, which can be heard whistling through electrical wires and waving many of the trees",
            // Beaufort Scale 8
            "with stronger gale-force winds shoot through the area, sending many leaves and smaller twigs from the trees",
            // Beaufort Scale 9/10
            "but dangerously windy, with the potential for roofing damage and toppling trees...recommended to only be outdoors at your own discretion",
            //Skipping B11
            // Beaufort Scale 12+
            "Devastating winds ravage through the area, putting the surrounding area on high alert. It is advised to seek shelter immediately."
    };

    private static final String[] colonyList = {
            "Siren-3",
            "Test Colony"
    };

    public static String getWeather(Integer index) {
        return naturalWeather[index];
    }

    public static String getBadStuff(Integer index) {
        return badStuff[index];
    }

    public static String getColonyList(Integer index) { return colonyList[index]; }

    public static String getNaturalTemps(Integer index) { return naturalTemps[index]; }

    public static String getBeaufortScale(Integer index) { return beaufortScale[index]; }
}
