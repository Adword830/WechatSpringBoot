package cn.percent.wechat.entity;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author pengju.zhang
 * @date 2022-08-31 15:34
 */
@Data
public class WeatherEntity {


    /**
     * 返回code
     */
    private String code;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 详情链接
     */
    private String fxLink;
    /**
     *
     */
    private WeatherNowEntity now;

    public Date getUpdateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmX");
        Date date = null;
        try {
            date = sdf.parse(this.updateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
