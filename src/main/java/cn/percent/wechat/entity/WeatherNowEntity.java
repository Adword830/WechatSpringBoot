package cn.percent.wechat.entity;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author pengju.zhang
 * @date 2022-08-31 16:20
 */
@Data
public class WeatherNowEntity {

    private String obsTime;
    private String temp;
    private String feelsLike;
    private String icon;
    private String text;
    private String wind360;
    private String windDir;
    private String windScale;
    private String windSpeed;
    private String humidity;
    private String precip;
    private String pressure;
    private String vis;
    private String cloud;
    private String dew;

    public Date getObsTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmX");
        Date date = null;
        try {
            date = sdf.parse(this.obsTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
