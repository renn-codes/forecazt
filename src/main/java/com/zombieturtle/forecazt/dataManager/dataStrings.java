package com.zombieturtle.forecazt.dataManager;

import static com.zombieturtle.forecazt.ForecaZT.nl;

public class dataStrings {

/* // Moon data feature deprecated, kept in case of future implementation
    private final String[] moonData = {

            "<:new_moon:529421884299804692> New Moon" + nl + nl,
            "<:waxing_crescent_moon:529421976482349076> Waxing Cresent" + nl + nl,
            "<:first_quarter_moon:529422288836231168> First Quarter" + nl + nl,
            "<:waxing_gibbous_moon:529422456096686101> Waxing Gibbous" + nl + nl,
            "<:full_moon:529422530839183370> Full Moon" + nl + nl,
            "<:waning_gibbous_moon:529422592721944586> Waning Gibbous" + nl + nl,
            "<:last_quarter_moon:529422691682222090> Last" + nl + nl,
            "<:waning_crescent_moon:529422909543022612> Waning Cresent" + nl + nl
    };
 */
    private final String[][] weatherData = {
            {
                    "**Weather Description:** Inclement weather" + nl,
                    "**Weather Description:** Inclement weather" + nl,
                    "**Weather Description:** Storms" + nl,
                    "**Weather Description:** Powerful Storms" + nl,
                    "**Weather Description:** Normal weather" + nl,
                    "**Weather Description:** Natural Disaster" + nl
            },
            {
                    "**Precipitation:** $ hour(s) of rain" + nl + nl,
                    "**Precipitation:** $ hour(s) of fog" + nl + nl,
                    "**Precipitation:** $ hour(s) of thunderstorms" + nl + nl,
                    "**Precipitation:** $ hour(s) of powerful thunderstorms" + nl + nl,
                    "**Weather Description:** Normal weather" + nl, //Placeholder text for Normal weather
                    "**Weather Description:** Natural Disaster" + nl //Placeholder text for Disaster weather
            }
    };

    private final String[] seasonData = {
            "Wet Season",
            "Dry Season"
    };

    private final String[][] badStuff = {
            {
                    "**Megafauna:** There has been a Megafauna sighting near Siren-3." + nl,
                    "**Earthquake** There has been a Earthquake." + nl,
                    "**Natural Disaster:** Torrential rain floods the streets." + nl,
                    "**Natural Disaster:** A tornado rips through the colony, causing devastating winds." + nl,
                    "**Heat Wave:** An unusual heat wave spreads over Siren-3.\n",
                    "" //used for non-disaster weather
            },
            {
                    "@everyone Colonists, there has been a Megafauna sighting near Siren-3! Take caution and report any further sightings!" + nl,
                    "@everyone Colonists, a earthquake has occurred! Make sure to check for any damage to structures and buildings!" + nl,
                    "@everyone Colonists, torrential rains have swept through Siren-3! Watch out for small lakes and deep puddles!" + nl,
                    "@everyone Colonists, a tornado has moved through Siren-3! Hope you didn't leave anything outside!" + nl,
                    "@everyone Colonists, an unu    sual heat wave has spread over the colony! Please don't cook any eggs on the street...\n",
                    "" //used for non-disaster weather
            }
    };

    /* Moon data feature deprecated, kept in case of future implementation
    public String getMoonData(Short moonIndex) {
        return moonData[moonIndex];
    }
    */

    public  String getWeatherData(Short weatherOuterIndex, Short weatherInnerIndex) {
        return weatherData[weatherOuterIndex][weatherInnerIndex];
    }

    public String getSeasonData(Short seasonIndex) {
        return seasonData[seasonIndex];
    }

    public String getBadStuffData(Short badStuffOuterIndex, Short badStuffInnerIndex) {
        return badStuff[badStuffOuterIndex][badStuffInnerIndex];
    }
}
