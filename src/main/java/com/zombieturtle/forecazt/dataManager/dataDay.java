package com.zombieturtle.forecazt.dataManager;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "week")
@XmlType(propOrder = { "runtime", "date", "high", "low", "windmph", /*"windkph", "moon",*/ "season", "weather", "bad", "colony" })
public class dataDay {

    private Integer runtime;
    private String date;
    private Integer high;
    private Integer low;
    private Integer windmph;
    // private Integer windkph; Unused, remove later?
    // private Integer moon; Deprecated, remove later?
    private Integer season;
    private Integer weather;
    private Boolean bad;
    private Integer colony;

    public Integer getRuntime() {
        return runtime;
    }

    @XmlElement
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getDate() {
        return date;
    }

    @XmlElement
    public void setDate(String date) {
        this.date = date;
    }

    public Integer getHigh() {
        return high;
    }

    @XmlElement
    public void setHigh(Integer high) {
        this.high = high;
    }

    public Integer getLow() {
        return low;
    }

    @XmlElement
    public void setLow(Integer low) {
        this.low = low;
    }

    public Integer getWindMph() {
        return windmph;
    }

    @XmlElement
    public void setWindMph(Integer windmph) {
        this.windmph = windmph;
    }

    /* Unused, remove later?
    public Integer getWindKph() {
        return windkph;
    }

    @XmlElement
    public void setWindKph(Integer windkph) {
        this.windkph = windkph;
    }
*/

    /* Deprecated, remove later?
    public Integer getMoon() {
        return moon;
    }

    @XmlElement
    public void setMoon(Integer moon) {
        this.moon = moon;
    }
    */

    public Integer getSeason() {
        return season;
    }

    @XmlElement
    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getWeather() {
        return weather;
    }

    @XmlElement
    public void setWeather(Integer weathera) {
        this.weather = weathera;
    }

    public Boolean getBad() {
        return this.bad;
    }

    @XmlElement
    public void setBad(Boolean bad) {
        this.bad = bad;
    }

    public Integer getColony() {
        return colony;
    }

    @XmlElement
    public void setColony(Integer colony) {
        this.colony = colony;
    }
}