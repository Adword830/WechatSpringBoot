package cn.percent.wechat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import cn.percent.wechat.constant.Constant;
import cn.percent.wechat.entity.*;
import cn.percent.wechat.enums.MsgType;
import cn.percent.wechat.service.SendMessage;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author pengju.zhang
 * @date 2022-08-30 16:32
 */
@Slf4j
@Service
public class SendMessageImpl implements SendMessage {

    @Value("${wechat.send.templateId}")
    private String templateId;

    @Value("${wechat.send.toUser}")
    private String toUser;

    @Value("${hf.weather.url}")
    private String weatherUrl;

    @Value("${wechat.create.menu}")
    private String menu;

    @Value("${wechat.upload.file}")
    private String upload;

    @Value("${wechat.get.mediaList}")
    private String mediaList;

    @Value("${wechat.imge}")
    private String imge;

    @Value("${wechat.video}")
    private String video;

    @Value("${wechat.access.token}")
    private String token;

    @Value("${holiday.url}")
    private String holiday;

    @Autowired
    private WxMpService wxMpService;


    @Override
    public String send(int flag) {
        log.info("进入发送模版信息");
        // 实例化模板对象
        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        // 设置模板ID
        wxMpTemplateMessage.setTemplateId(templateId);
        // 设置发送给哪个用户
        wxMpTemplateMessage.setToUser(toUser);
        // 天气类
        String weather = this.getWeather();
        WeatherEntity weatherEntity = null;
        // Date date = DateUtil.parse("2022-09-23").toJdkDate();
        Date date = new Date();
        // 构建消息格式
        List<WxMpTemplateData> listData = new ArrayList<>(12);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 节假日
        Map<String, String> holiday = this.getHoliday(date);
        // 休息日
        Map<String, String> restDay = this.getRestDay(date, holiday);
        String format = simpleDateFormat.format(date);

        if (holiday == null || holiday.get(format) == null) {
            if (restDay == null || restDay.size() == 0) {
                weatherEntity = this.createWorkTemplate(date, flag, listData, weather);
            } else {
                weatherEntity = this.createRestDayTemplate(date, flag, listData, restDay, format, weather);
            }
        } else {
            weatherEntity = this.createHolidayTemplate(date, flag, listData, holiday, format, weather);
        }
        // 设置跳转url
        wxMpTemplateMessage.setUrl(weatherEntity.getFxLink());
        String wxTemplateResult = null;
        // 放进模板对象。准备发送
        wxMpTemplateMessage.setData(listData);
        try {
            if (weatherEntity != null) {
                // 发送模板
                wxTemplateResult = wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
                if (StrUtil.isNotEmpty(wxTemplateResult)) {
                    log.info("发送成功");
                }
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return wxTemplateResult;
    }

    @Override
    public OutMsgEntity inMessage(InMsgEntity inMsgEntity) {
        if (inMsgEntity == null || StrUtil.isEmpty(inMsgEntity.getContent())) {
            return null;
        }
        OutMsgEntity outMsgEntity = new OutMsgEntity();
        long time = System.currentTimeMillis();
        switch (inMsgEntity.getContent()) {
            case Constant.message_text_1:
                outMsgEntity.setContent("爱老婆syy");
                outMsgEntity.setMsgType(MsgType.TEXT.getKey());
                break;
            case Constant.message_text_2:
                // 视频消息
                VideoMsgEntity videoMsg = new VideoMsgEntity();
                videoMsg.setMediaId(new String[]{video});
                videoMsg.setMsgType(MsgType.VIDEO.getKey());
                videoMsg.setFromUserName(inMsgEntity.getToUserName());
                videoMsg.setToUserName(inMsgEntity.getFromUserName());
                videoMsg.setCreateTime(time);
                return videoMsg;
            case Constant.message_text_3:
                // 图片消息
                ImgMsgEntity imgMsg = new ImgMsgEntity();
                imgMsg.setMediaId(new String[]{imge});
                imgMsg.setMsgType(MsgType.IMAGE.getKey());
                imgMsg.setFromUserName(inMsgEntity.getToUserName());
                imgMsg.setToUserName(inMsgEntity.getFromUserName());
                imgMsg.setCreateTime(time);
                return imgMsg;
            default:
                outMsgEntity.setMsgType(MsgType.TEXT.getKey());
                outMsgEntity.setContent(Constant.message_1 + "  "
                        + Constant.message_2 + "  "
                        + Constant.message_3);
                break;
        }

        outMsgEntity.setFromUserName(inMsgEntity.getToUserName());
        outMsgEntity.setToUserName(inMsgEntity.getFromUserName());
        outMsgEntity.setCreateTime(time);
        return outMsgEntity;
    }

    @Override
    public String checkSignature(String signature, String timestamp, String nonce, String echostr) {
        // 第一步：自然排序
        String[] tmp = {token, timestamp, nonce};
        Arrays.sort(tmp);
        // 第二步：sha1 加密
        String sourceStr = StringUtils.join(tmp);
        String localSignature = DigestUtils.sha1Hex(sourceStr);
        // 第三步：验证签名
        if (signature.equals(localSignature)) {
            return echostr;
        }
        return null;
    }

    @Override
    public String createMenu(String jsonStr) {
        String post = null;
        try {
            post = HttpUtil.post(menu + wxMpService.getAccessToken(), jsonStr);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile, String description, String type) {
        // &type=image
        String post = null;
        File file = null;
        try {
            String[] split = multipartFile.getOriginalFilename().split("\\.");
            file = File.createTempFile(UUID.randomUUID().toString(), "." + split[1]);
            multipartFile.transferTo(file);
            post = HttpUtil.createPost(upload + wxMpService.getAccessToken() + "&type=" + type)
                    .form("description", description)
                    .form("media", file).execute().body();
        } catch (WxErrorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                file.deleteOnExit();
            }
        }
        return post;
    }

    @Override
    public String getMediaList(String count, String offset, String type) {
        Map<String, Object> map = new HashMap<>(12);
        map.put("count", count);
        map.put("offset", offset);
        map.put("type", type);
        String post = null;
        try {
            post = HttpUtil.post(mediaList + wxMpService.getAccessToken(), JSONObject.toJSONString(map));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * 获取实时天气
     */
    private String getWeather() {
        String json = "";
        try {
            json = HttpUtil.get(weatherUrl);
        } catch (Exception e) {
            json = HttpUtil.get(weatherUrl);
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 获取第二行文本
     *
     * @param text
     * @param temp
     * @param weatherEntity
     * @param third
     * @return
     */
    private WxMpTemplateData getThird(String text, int temp, WeatherEntity weatherEntity, WxMpTemplateData third) {
        if (temp <= 15) {
            third = new WxMpTemplateData("third", text + weatherEntity.getNow().getText() + "  温度："
                    + weatherEntity.getNow().getTemp() + "度 天气微凉带好外套，", "#173177");
        } else if (temp > 15 && temp <= 25) {
            third = new WxMpTemplateData("third", text + weatherEntity.getNow().getText() + "  温度："
                    + weatherEntity.getNow().getTemp() + "度 天气较热，无需增加衣物", "#173177");
        } else if (temp > 25) {
            third = new WxMpTemplateData("third", text + weatherEntity.getNow().getText() + "  温度："
                    + weatherEntity.getNow().getTemp() + "度 天气炎热预防中暑", "#173177");
        } else if (temp < 5) {
            third = new WxMpTemplateData("third", text + weatherEntity.getNow().getText() + "  温度："
                    + weatherEntity.getNow().getTemp() + "度 天气转凉注意增加衣物", "#173177");
        }
        return third;
    }

    /**
     * 计算距离生日 2000-04-04 2000-12-06
     *
     * @param birthStr
     * @param date
     * @return
     */
    private int calBirth(String birthStr, Date date) {
        int day = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birth = simpleDateFormat.parse(birthStr);
            // 存当前时间
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            // 存生日
            Calendar birthCal = Calendar.getInstance();
            birthCal.setTime(birth);
            // 把生日设置为今年
            birthCal.set(Calendar.YEAR, now.get(Calendar.YEAR));

            if (now.get(Calendar.DAY_OF_YEAR) - birthCal.get(Calendar.DAY_OF_YEAR) > 0) {
                // 生日已过
                birthCal.set(Calendar.YEAR, now.get(Calendar.YEAR) + 1);
                day = now.getActualMaximum(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR) + birthCal.get(Calendar.DAY_OF_YEAR);
            } else {
                // 生日未过
                day = birthCal.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 计算在一起
     *
     * @param together
     * @param date
     * @return
     */
    private int calTogether(String together, Date date) {
        int day = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birth = simpleDateFormat.parse(together);
            // 存当前时间
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            // 开始时间
            Calendar toge = Calendar.getInstance();
            toge.setTime(birth);
            now.get(Calendar.YEAR);
            toge.get(Calendar.YEAR);
            if (now.get(Calendar.YEAR) != toge.get(Calendar.YEAR)) {
                // 不是同一年
                int timeDistance = 0;
                for (int i = toge.get(Calendar.YEAR); i < now.get(Calendar.YEAR); i++) {
                    if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                        timeDistance += 366;
                    } else {
                        timeDistance += 365;
                    }
                }
                day = timeDistance + (now.get(Calendar.DAY_OF_YEAR) - toge.get(Calendar.DAY_OF_YEAR));
            } else {
                day = now.get(Calendar.DAY_OF_YEAR) - toge.get(Calendar.DAY_OF_YEAR);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 节假日假日名称获取
     *
     * @param date
     * @return
     */
    private Map<String, String> getHoliday(Date date) {
        Map<String, String> result = new HashMap<>(12);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String yearMonth = DateUtil.format(DateUtil.date(), "yyyy-MM");
        String url = holiday + yearMonth;
        // System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
        String body = HttpUtil.createGet(url).timeout(30000).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject holiday = jsonObject.getJSONObject("holiday");
        int i = holiday.size();
        for (Map.Entry<String, Object> entry : holiday.entrySet()) {
            JSONObject value = (JSONObject) entry.getValue();
            Boolean holidayFlag = value.getBoolean("holiday");
            if (holidayFlag) {
                String name = value.getString("name");
                String time = value.getString("date");
                result.put(time, name + "," + i);
                i--;
            }
        }
        return result;
    }

    /**
     * 补班日获取
     *
     * @param date
     * @return
     */
    private Map<String, String> getMakeDay(Date date) {
        Map<String, String> result = new HashMap<>(12);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String yearMonth = DateUtil.format(DateUtil.date(), "yyyy-MM");
        String url = holiday + yearMonth;
        // System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
        String body = HttpUtil.createGet(url).timeout(30000).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject holiday = jsonObject.getJSONObject("holiday");
        int i = holiday.size();
        for (Map.Entry<String, Object> entry : holiday.entrySet()) {
            JSONObject value = (JSONObject) entry.getValue();
            Boolean holidayFlag = value.getBoolean("holiday");
            if (!holidayFlag) {
                String name = value.getString("name");
                String time = value.getString("date");
                result.put(time, name + "," + i);
                i--;
            }
        }
        return result;
    }

    /**
     * 休息日
     *
     * @param date
     * @return
     */
    private Map<String, String> getRestDay(Date date, Map<String, String> holiday) {
        // 补班日期
        Map<String, String> makeDay = getMakeDay(date);
        Map<String, String> result = new HashMap<>(12);
        Calendar cld = Calendar.getInstance(Locale.CHINA);
        cld.setTime(date);
        // 以周一为首日
        cld.setFirstDayOfWeek(Calendar.MONDAY);
        // 周日
        cld.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String timeSaturday = DateUtil.format(cld.getTime(), "yyyy-MM-dd");
        cld.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String timeSunday = DateUtil.format(cld.getTime(), "yyyy-MM-dd");
        String time = DateUtil.format(date, "yyyy-MM-dd");
        Map<String, String> restDay = new HashMap<>(12);
        // 当前是周六还是周日
        if (time.equalsIgnoreCase(timeSaturday)) {
            restDay.put(timeSaturday, "周六");
        } else if (time.equalsIgnoreCase(timeSunday)) {
            restDay.put(timeSunday, "周日");
        }
        for (Map.Entry<String, String> rest : restDay.entrySet()) {
            // 判断是否有周六或者周日补班
            if (makeDay.size() == 0) {
                return restDay;
            } else {
                for (Map.Entry<String, String> make : makeDay.entrySet()) {
                    if (!make.getKey().equalsIgnoreCase(rest.getKey())) {
                        result.put(rest.getKey(), rest.getValue());
                    }
                }
            }
            // 判断是否有假期和休息日冲突
            if (holiday.size() == 0) {
                return restDay;
            } else {
                for (Map.Entry<String, String> holi : holiday.entrySet()) {
                    if (!holi.getKey().equalsIgnoreCase(rest.getKey())) {
                        result.put(rest.getKey(), rest.getValue());
                    }
                }
            }
        }
        return result;
    }

    /**
     * 构建工作日模板
     *
     * @param date
     * @param flag
     * @param listData
     */
    private WeatherEntity createWorkTemplate(Date date, int flag, List<WxMpTemplateData> listData,
                                             String weather) {
        WeatherEntity weatherEntity = null;
        // 第一行数据内容 {{first.DATA}}
        WxMpTemplateData first = null;
        WxMpTemplateData third = null;
        // 第二行数据内容 {{second.DATA}}
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        WxMpTemplateData second = new WxMpTemplateData("second", "当前是: " + format, "#173177");

        // 早上
        if (flag == 1) {
            weatherEntity = JSONObject.parseObject(weather, WeatherEntity.class);
            first = new WxMpTemplateData("first", "一日之计在于晨，早上起床记得刷牙洗脸"
                    , "#173177");
            String text = "早上天气: ";
            int temp = Integer.parseInt(weatherEntity.getNow().getTemp());
            third = this.getThird(text, temp, weatherEntity, third);
        }
        // 中午下班前半小时
        if (flag == 2) {
            weatherEntity = JSONObject.parseObject(weather, WeatherEntity.class);
            first = new WxMpTemplateData("first", "干饭人，干饭魂马上到中午吃饭啦"
                    , "#173177");
            String text = "中午天气: ";
            int temp = Integer.parseInt(weatherEntity.getNow().getTemp());
            third = this.getThird(text, temp, weatherEntity, third);
        }
        // 下班
        if (flag == 3) {
            weatherEntity = JSONObject.parseObject(weather, WeatherEntity.class);
            first = new WxMpTemplateData("first", "下班啦下班啦，回家路上要注意安全"
                    , "#173177");
            String text = "下午天气: ";
            int temp = Integer.parseInt(weatherEntity.getNow().getTemp());
            third = this.getThird(text, temp, weatherEntity, third);
        }
        int syyBirth = this.calBirth("2000-04-04", date);
        int zpjBirth = this.calBirth("2000-12-06", date);
        int together = this.calTogether("2020-07-15", date);
        WxMpTemplateData fourth = new WxMpTemplateData("fourth", "当前距离syy生日还有" + syyBirth + "天"
                + " 距离zpj生日还有" + zpjBirth + "天"
                , "#173177");
        WxMpTemplateData fifth = new WxMpTemplateData("fifth", "我们已经在一起" + together + "天"
                , "#173177");
        // 第二行数据内容 {{first.DATA}}
        listData.add(first);
        // 第二行数据内容 {{second.DATA}}
        listData.add(second);
        // 第三行数据内容 {{third.DATA}}
        listData.add(third);
        // 第四行数据内容 {{fourth.DATA}}
        listData.add(fourth);
        // 第五行数据内容 {{fifth.DATA}}
        listData.add(fifth);
        return weatherEntity;
    }

    /**
     * 节假日模版
     *
     * @param date
     * @param flag
     * @param listData
     * @param holiday
     * @param format
     * @param weather
     * @return
     */
    private WeatherEntity createHolidayTemplate(Date date, int flag, List<WxMpTemplateData> listData,
                                                Map<String, String> holiday, String format, String weather) {
        WeatherEntity weatherEntity = null;
        String holidayName = holiday.get(format);
        String[] split = holidayName.split(",");
        // 第一行数据内容 {{first.DATA}}
        WxMpTemplateData first = null;
        WxMpTemplateData third = null;
        // 第二行数据内容 {{second.DATA}}
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format2 = simpleDateFormat.format(date);
        WxMpTemplateData second = new WxMpTemplateData("second", "当前是: " + format2, "#173177");
        // 早上
        if (flag == 1) {
            weatherEntity = JSONObject.parseObject(weather, WeatherEntity.class);
            if (split[1].equals(String.valueOf(holiday.size()))) {
                first = new WxMpTemplateData("first", "今天是" + split[0] + "假期最后一天,明天就要上班了要注意早点休息预防打瞌睡"
                        , "#173177");
            } else {
                first = new WxMpTemplateData("first", "今天是" + split[0] + "假期第" + split[1] + "天,祝老婆"
                        + split[0] + "假期快乐"
                        , "#173177");
            }
            String text = "今天天气: ";
            int temp = Integer.parseInt(weatherEntity.getNow().getTemp());
            third = this.getThird(text, temp, weatherEntity, third);
        }
        int syyBirth = this.calBirth("2000-04-04", date);
        int zpjBirth = this.calBirth("2000-12-06", date);
        int together = this.calTogether("2020-07-15", date);
        WxMpTemplateData fourth = new WxMpTemplateData("fourth", "当前距离syy生日还有" + syyBirth + "天"
                + " 距离zpj生日还有" + zpjBirth + "天"
                , "#173177");
        WxMpTemplateData fifth = new WxMpTemplateData("fifth", "我们已经在一起" + together + "天"
                , "#173177");
        // 第二行数据内容 {{first.DATA}}
        listData.add(first);
        // 第二行数据内容 {{second.DATA}}
        listData.add(second);
        // 第三行数据内容 {{third.DATA}}
        listData.add(third);
        // 第四行数据内容 {{fourth.DATA}}
        listData.add(fourth);
        // 第五行数据内容 {{fifth.DATA}}
        listData.add(fifth);
        return weatherEntity;
    }


    /**
     * 休息日模版
     *
     * @param date
     * @param flag
     * @param listData
     * @param rest
     * @param format
     * @param weather
     * @return
     */
    private WeatherEntity createRestDayTemplate(Date date, int flag, List<WxMpTemplateData> listData,
                                                Map<String, String> rest, String format, String weather) {
        WeatherEntity weatherEntity = null;
        String holidayName = rest.get(format);
        // 第一行数据内容 {{first.DATA}}
        WxMpTemplateData first = null;
        WxMpTemplateData third = null;
        // 第二行数据内容 {{second.DATA}}
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format2 = simpleDateFormat.format(date);
        WxMpTemplateData second = new WxMpTemplateData("second", "当前是: " + format2, "#173177");
        // 早上
        if (flag == 1) {
            weatherEntity = JSONObject.parseObject(weather, WeatherEntity.class);
            first = new WxMpTemplateData("first", "今天是" + holidayName + ",是休息日要好好休息不要熬夜哦"
                    , "#173177");
            String text = "今天天气: ";
            int temp = Integer.parseInt(weatherEntity.getNow().getTemp());
            third = this.getThird(text, temp, weatherEntity, third);
        }
        int syyBirth = this.calBirth("2000-04-04", date);
        int zpjBirth = this.calBirth("2000-12-06", date);
        int together = this.calTogether("2020-07-15", date);
        WxMpTemplateData fourth = new WxMpTemplateData("fourth", "当前距离syy生日还有" + syyBirth + "天"
                + " 距离zpj生日还有" + zpjBirth + "天"
                , "#173177");
        WxMpTemplateData fifth = new WxMpTemplateData("fifth", "我们已经在一起" + together + "天"
                , "#173177");
        // 第二行数据内容 {{first.DATA}}
        listData.add(first);
        // 第二行数据内容 {{second.DATA}}
        listData.add(second);
        // 第三行数据内容 {{third.DATA}}
        listData.add(third);
        // 第四行数据内容 {{fourth.DATA}}
        listData.add(fourth);
        // 第五行数据内容 {{fifth.DATA}}
        listData.add(fifth);
        return weatherEntity;
    }

}
