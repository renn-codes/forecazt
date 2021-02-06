// Manning Meindel 5/6/20
///Made for the Starsong Living world Server

const Discord = require('discord.js');
require('dotenv').config();
const client = new Discord.Client();
var token = process.env.TOKEN;

client.login(token);
//client.login("");

var week = 1;
var moon = 1;
var season = 1;
//Time-tracker string
var string = "";
//Forecast string
var string2 = "";
//Weather occurence string
var string3 = "";
//Temperature variables
var high = 0;
var low = 0;
//Wind speed variable
var wind = 0;
//Bad Trigger
var bad = 0;
var border = "===================================\n";
var latestDate = new Date();
//Time-tracker channel
const id = "712449756101869609";
//Weather-forecast channel
const id2 = "712449612199362581";
const http = require('http');

client.on('ready', () => {
  console.log(`Logged in as ${client.user.tag}!`);
  var date;
  const channel = client.channels.cache.get(id);

  //client.users.cache.get("183065668856315904").send("Readied");
  client.users.cache.get("183065668856315904").send("Starsong is active!");

  getLastValues();

  //Change values dynamically
  client.on('message', msg => {
   //Change week
   if (msg.content.startsWith("!Week") && msg.channel.id === id) {
     var test = msg.content.split(' ');
     var newWeek = parseInt(test[1],10);
     console.log(newWeek);
     //Invalid number
     if(newWeek > 52 || newWeek < 1 ) {
       msg.channel.send('Invalid number ' + newWeek + ': Must be between 1 and 52.');
     }
     else {
       week = newWeek;
     }
   }
   //Change moon
   if (msg.content.startsWith("!Moon") && msg.channel.id === id) {
     var test = msg.content.split(' ');
     var newMoon = parseInt(test[1],10);
     console.log(newMoon);
     //Invalid number
     if(newMoon > 8 || newMoon < 1 ) {
       msg.channel.send('Invalid number ' + newMoon + ': Must be between 1 and 52.');
     }
     else {
       moon = newMoon;
     }
   }
   //Display Current values
   if (msg.content.startsWith("!Display") && msg.channel.id === id) {
     msg.channel.send("Current values: Week: " + week + " Moon: " + moon + " Season: " + season);
   }
   //Display possible values (Moon)
   if (msg.content.startsWith("!MoonValues") && msg.channel.id === id) {
     msg.channel.send("Possible moon values:\n 1 (New Moon) <:new_moon:529421884299804692>\n 2 (Waxing Cresent) <:waxing_crescent_moon:529421976482349076>\n 3 (First Quarter) <:first_quarter_moon:529422288836231168>\n 4 (Waxing Gibbous) <:waxing_gibbous_moon:529422456096686101>\n 5 (Full Moon) <:full_moon:529422530839183370>\n 6 (Waning Gibbous) <:waning_gibbous_moon:529422592721944586>\n 7 (Last Quarter) <:last_quarter_moon:529422691682222090>\n 8 (Waning Cresent) <:waning_crescent_moon:529422909543022612>\n");
   }
   //Display possible values (Season)
   if (msg.content.startsWith("!SeasonValues") && msg.channel.id === id) {
     msg.channel.send("Possible season values:\n 1 (Wet) <1-26>\n 2 (Dry) <27-52>\n");
   }
   //Displays time until next update is posted.
   if (msg.content.startsWith("!NextUpdate") && msg.channel.id === id) {
     date = new Date();
     date.setHours(date.getHours() - 5);
     msg.channel.send( ((23-(date.getHours()))) + " Hours, " + (58 - (date.getMinutes())) + " minutes, and " + (60 - (date.getSeconds())) + " seconds until 12AM");
     msg.channel.send( (( ((23-(date.getHours()))) *60*60 + (58 - (date.getMinutes()))*60 + (60 - (date.getSeconds())) )*1000) + " milliseconds until 12AM");
   }
   if (msg.content.startsWith("!Post") && msg.channel.id === id) {
     timeTrackerUpdate();
   }
   //Help
   if (msg.content.startsWith("!Help") && msg.channel.id === id) {
    msg.channel.send("Commands:\n !Week <1-52>    Sets the week value to an integer entered after !Week.\n !Moon <1-8>      Sets the moon value to an integer entered after !Moon.\n !Display               Displays the current values of week, moon, and season.\n !MoonValues     Displays all possible moon values.\n !SeasonValues   Displays all possible season values.\n !NextUpdate       Displays the time until next output.\n !Post                     Causes weather posting.\n\n Hosted through Heroku deployment.");
   }
  });

  //Check the current hour compared against 12AM. Get the time difference and convert to milliseconds. Do a timeout call to start the interval system.
  //https://en.wikipedia.org/wiki/24-hour_clock

  date = new Date();
  date.setHours(date.getHours() - 5);

  console.log("Current Date: " + date.getDate() + " Last Date: " + latestDate.getDate());
  console.log( ((23-(date.getHours()))) + " Hours, " + (58 - (date.getMinutes())) + " minutes, and " + (60 - (date.getSeconds())) + " seconds until 11:59PM");


  setTimeout(function(){

    if(date.getDate() != latestDate.getDate())  {

      timeTrackerUpdate();
    }

    //date.setHours(date.getHours() - 0);

    //Activates every 24 hours.
    var interval = setInterval (function () {
     timeTrackerUpdate();

   }, 86340000); // time between each interval in milliseconds

 }, (( ((23-(date.getHours()))) *60*60 + (58 - (new Date().getMinutes()))*60 + (60 - (new Date().getSeconds())) )*1000));

});

//Puts together the output  of the time-tracker and ouputs it when called.
function timeTrackerUpdate(){
  //52 weeks in a year
  //7 days per update
  //364 days total.

  //TESTING
  //console.log("Week: " + week + " Moon " + moon);

  //Wet Season
  if(week >= 1 && week <= 26) {
    string = ":\n"+ border + week + "/52 Annual Weeks\n\n" + week + "/26 Wet Season\n\n";
    getMoon();
    season = 1;
  }

  //Dry Season
  if(week >= 27 && week <= 52) {
    string = ":\n"+ border + week + "/52 Annual Weeks\n\n" + (week-26) + "/26 Dry Season\n\n";
    getMoon();
    season = 3;
  }

  //Add final border
  string = string + border;

  //Output message and update values
  const channel = client.channels.cache.get(id);
  channel.send(string);
  getForecast();
  //console.log(string);
  week++;
  moon++;
  //Reset week count for end of year
  if(week > 52) {
    week = 1;
  }
  //Reset moon for end of moon cycle
  if(moon > 8) {
    moon = 1;
  }
}

//Adds the moon status attachment to the string to be displayed.
function getMoon(){
  // New Moon
  if(moon == 1) {
    string = string + "<:new_moon:529421884299804692> New Moon\n\n";
  }
  // Waxing Cresent
  if(moon == 2) {
    string = string + "<:waxing_crescent_moon:529421976482349076> Waxing Cresent\n\n"
  }
  // First Quarter
  if(moon == 3) {
    string = string + "<:first_quarter_moon:529422288836231168> First Quarter\n\n"
  }
  // Waxing Gibbous
  if(moon == 4) {
    string = string + "<:waxing_gibbous_moon:529422456096686101> Waxing Gibbous\n\n"
  }
  // Full Moon
  if(moon == 5) {
    string = string + "<:full_moon:529422530839183370> Full Moon\n\n"
  }
  // Waning Gibbous
  if(moon == 6) {
    string = string + "<:waning_gibbous_moon:529422592721944586> Waning Gibbous\n\n"
  }
  // Last Quarter
  if(moon == 7) {
    string = string + "<:last_quarter_moon:529422691682222090> Last Quarter\n\n"
  }
  // Waning Cresent
  if(moon == 8) {
    string = string + "<:waning_crescent_moon:529422909543022612> Waning Cresent\n\n"
  }

}

//Get the weather forecast for this week.
function getForecast(){
  var i;
  string = ":\n===================================\n";
  for( i = 1; i < 8; i++) {
    string = string + "**Day:** " + i + "\n";
    string2 = "";
    /*Wet Season
    Low: 65 F High: 95 F
    Winds speeds, Low: 0 High: 10
    Precipitation types: Rain, Heavy Rain*/

    /*Dry Season
    Low: 65 F High: 95 F
    Winds speeds, Low: 0 High: 10
    Precipitation types: Rain*/

    if(season == 3 || season == 4) {
      //Get high
      high = Math.floor(Math.random() * 15)+80;

      //Get Low
      low = Math.floor(Math.random() * 15)+65;
      while(low == high) {
        low = Math.floor(Math.random() * 15)+65;
      }

      //Get Wind speeds
      wind = Math.floor(Math.random() * 10)+1;
    }

    //Get weather type
    getWeather();

    //output
    string = string + "**Temperature:** High " + high + "°F(" + Math.round(((high-32)*(5/9))) + "°C)/Low " + low + "°F(" + Math.round(((low-32)*(5/9))) + "°C)\n";
    string = string + "**Wind Speed:** " + wind  + " mph (" + Math.round((wind*1.6)) + " kph)\n";
    string = string + string2 + "===================================\n";
  }
  bad = 0;
  channel2 = client.channels.cache.get(id2);
  channel2.send(string);
  if(string3 !== "") {
    channel2.send(string3);
    string3 = "";
  }
  //console.log(string);
}

//Gets the weather type
function getWeather(){
  //Weather types: 1 (Normal) 60%, 2 (Occurance) 40%
  //Occurance Types: 1 (Inclement) 70%, 2 (Storm) 15%, 3 (Powerful Storm) 10%, 4 (BAD) 5%

  //Normal weather
  if( Math.floor(Math.random() * 100) + 1 <= 60 ) {
    string = string + "**Weather Description:** Normal weather\n";
  }
  //Occurance
  else {
    var percentage = Math.floor(Math.random() * 100) + 1;
    //Inclement
    if(percentage <= 97) {

      //Check for type by season
      percentage = Math.floor(Math.random() * 100) + 1;
      //Wet Season
      if(season == 1 ||season == 2) {
        //Rain
        if(percentage <= 60) {
          string = string + "**Weather Description:** Inclement weather\n";
          string2 = string2 + "**Precipitation:** " + (Math.floor(Math.random() * 20) + 1) + " hour(s) of rain\n";
        }
        //Fog
        else if(percentage >= 61 && percentage <= 70) {
          string = string + "**Weather Description:** Inclement weather\n";
          string2 = string2 + "**Precipitation:** " + (Math.floor(Math.random() * 10) + 1) + " hour(s) of fog\n";
        }
        //Storm
        else if(percentage >= 71 && percentage <= 86) {
          wind = wind + 5;
          string = string + "**Weather Description:** Storms\n";
          string2 = string2 + "**Precipitation:** " + (Math.floor(Math.random() * 15) + 1) + " hour(s) of thunderstorms and rain\n";
        }
        //Powerful Storm
        else if(percentage >= 87 && percentage <= 100) {
          wind = wind + 8;
          string = string + "**Weather Description:** Powerful Storms\n";
          string2 = string2 + "**Precipitation:** " + (Math.floor(Math.random() * 15) + 1) + " hour(s) of heavy thunderstorms and rain\n";
        }
      }
      //Dry Season
      else if(season == 3 || season == 4) {
          //Rain
          if(percentage <= 60) {
            string = string + "**Weather Description:** Inclement weather\n";
            string2 = string2 + "**Precipitation:** " + (Math.floor(Math.random() * 3) + 1) + " hour(s) of rain\n";
          }
          //Fog
          else if(percentage >= 61 && percentage <= 79) {
            string = string + "**Weather Description:** Inclement weather\n";
            string2 = string2 + "**Precipitation:** " + (Math.floor(Math.random() * 2) + 1) + " hour(s) of fog\n";
          }
          //Storm
          else if(percentage >= 80 && percentage <= 100) {
            wind = wind + 5;
            string = string + "**Weather Description:** Storms\n";
            string2 = string2 + "**Precipitation:** " + (Math.floor(Math.random() * 8) + 1) + " hour(s) of thunderstorms and rain\n";
          }
      }
    }
    //BAD
    else if(percentage >= 98 && percentage <= 100 && bad == 0) {
      string = string + "**Weather Description:** Natural Disaster\n";
      bad == 1;
      getBAD();
    }
    //BAD already triggered
    else if(percentage >= 98 && percentage <= 100 && bad == 1) {
      string = string + "**Weather Description:** Normal weather\n";
    }
  }
}

//Deals with the really bad weather occurances
function getBAD(){
  //Percentage Generator
  var percentage = Math.floor(Math.random() * 100) + 1;
  //console.log(percentage);

    //Wet Season
    if(season == 1 || season == 2) {
      //Megafauna
      if(percentage >= 1 && percentage <= 30) {
        string2 = string2 + "**Megafauna:** There has been a Megafauna sighting near Siren-3.\n";
        string3 = "@everyone Colonists, there has been a Megafauna sighting near Siren-3! Take caution and report any further sightings!\n";
      }
      //Earthquake
      if(percentage >= 31 && percentage <= 50) {
        string2 = string2 + "**Earthquake** There has been a Earthquake.\n";
        string3 = "@everyone Colonists, a earthquake has occured! Make sure to check for any damage to structures and buildings!\n";
      }
      //Torrential Rains
      if(percentage >= 51 && percentage <= 80) {
        string2 = string2 + "**Natural Disaster:** Torrential rain floods the streets.\n";
        string3 = "@everyone Colonists, torrential rains have swept through Siren-3! Watch out for small lakes and deep puddles!\n";
      }
      //Tornado
      if(percentage >= 81) {
        wind = wind +  Math.floor(Math.random() * 60) + 70;
        string2 = string2 + "**Natural Disaster:** A tornado rips through the colony, causing devastating winds.\n";
        string3 = "@everyone Colonists, a tornado has moved through Siren-3! Hope you didn't leave anything outside!\n";
      }
    }
    //Dry Season
    if(season == 3 || season == 4) {
      //Megafauna
      if(percentage >= 1 && percentage <= 30) {
        string2 = string2 + "**Megafauna:** There has been a Megafauna sighting near Siren-3.\n";
        string3 = "@everyone Colonists, there has been a Megafauna sighting near Siren-3! Take caution and report any further sightings!\n";
      }
      //Earthquake
      if(percentage >= 31 && percentage <= 50) {
        string2 = string2 + "**Earthquake** There has been a Earthquake.\n";
        string3 = "@everyone Colonists, a earthquake has occured! Make sure to check for any damage to structures and buildings!\n";
      }
      //Record Heat
      if(percentage >= 51 && percentage <= 80) {
        high = high + 15;
        low = low + 15;
        string2 = string2 + "**Heat Wave:** An unsual heat wave spreads over Siren-3.\n";
        string3 = "@everyone Colonists, an unsual heat wave has spread over the colony! Please don't cook any eggs on the street...\n";
      }
      //Tornado
      if(percentage >= 81) {
        wind = wind +  Math.floor(Math.random() * 60) + 70;
        string2 = string2 + "**Natural Disaster:** A tornado rips through the colony, causing devastating winds.\n";
        string3 = "@everyone Colonists, a tornado has moved through Siren-3! Hope you didn't leave anything outside!\n";
      }
    }
}

//Get last values of time and update accordingly.
function getLastValues(){

  //Get last valid date message in time-tracker.
  var channel = client.channels.cache.get(id);

  //Get last 10 messages. If not valid within 10 messages then problem-in-chair.
  channel.messages.fetch({limit:10}).then(messages => {

    messages = Array.from(messages);

    //Check for border in message.
    var i;
    var found = false;
    for(i = 0; i < 10 && found == false; i++) {

      if(messages[i][1].content.includes("===================================")) found = true;
    }
    if(found == false){
      console.log("Message not found!");
      week = 0;
      moon = 0;
      return 1;
    }
    i--;


    //Get latest Date
    latestDate = messages[i][1].createdAt;

    var last = messages[i][1].content.split("\n");


    //Get and set week value.
    var newWeek = parseInt(last[2].split("/")[0]) + 1;

    //console.log("newWeek is " + newWeek);
    if(newWeek > 52) {
      week = 1;
    }
    else {
      week = newWeek;
    }
    //console.log(week);

    //Get and set moon value.
    var newMoon = last[6].split(" ")[0];
    //console.log(newMoon);
    if(newMoon === ":new_moon:") {
      moon = 2;
    }
    if(newMoon === ":waxing_crescent_moon:") {
      moon = 3;
    }
    else if(newMoon === ":first_quarter_moon:") {
      moon = 4;
    }
    else if(newMoon === ":waxing_gibbous_moon:") {
      moon = 5;
    }
    else if(newMoon === ":full_moon:") {
      moon = 6;
    }
    else if(newMoon === ":waning_gibbous_moon:") {
      moon = 7;
    }
    else if(newMoon === ":last_quarter_moon:") {
      moon = 8;
    }
    else if(newMoon === ":waning_crescent_moon:") {
      moon = 1;
    }
    //TESTING
    //console.log("Week is " + week);
    return 0;
  }).catch(err => {
    console.log(err);
  });

}

process
  .on('SIGTERM', shutdown('SIGTERM'))
  .on('SIGINT', shutdown('SIGINT'))
  .on('uncaughtException', shutdown('uncaughtException'));

//setInterval(console.log.bind(console, 'tick'), 1000);
http.createServer((req, res) => res.end('hi'))
  .listen(process.env.PORT || 3000, () => console.log('Listening'));

function shutdown(signal) {
  return (err) => {
    console.log(`${ signal }...`);
    if (err) console.error(err.stack || err);
    setTimeout(() => {
      console.log('...waited 5s, exiting.');
      process.exit(err ? 1 : 0);
    }, 5000).unref();
  };
}
