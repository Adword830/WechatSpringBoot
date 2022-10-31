package cn.percent.wechat.enums;

import java.util.Arrays;

/**
 * @author pengju.zhang
 * @date 2022-09-02 11:56
 */
public enum MsgType {

    /**
     * 消息类型
     * text 文本消息
     * image 图片消息
     * voice 语音消息
     * video 视频消息
     * music 音乐消息
     */

    TEXT("text","文本消息"),
    VOICE("voice","语音消息"),
    VIDEO("video","视频消息"),
    MUSIC("music","音乐消息"),
    IMAGE("image","图片消息");

    private String key;
    private String value;

    MsgType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 通过key值获取value
     *
     * @param  key
     * @return enum
     */
    public static MsgType resovle(Integer key) {
        return Arrays.stream(values())
                .filter(status -> status.value.equals(key))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Illegal code: " + key));
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
