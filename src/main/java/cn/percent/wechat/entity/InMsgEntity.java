package cn.percent.wechat.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author pengju.zhang
 * @date 2022-09-02 10:05
 */
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class InMsgEntity {

    /**
     * 开发者微信号
     */
    @XmlElement(name="FromUserName")
    protected String fromUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    @XmlElement(name="ToUserName")
    protected String toUserName;
    /**
     * 消息创建时间
     */
    @XmlElement(name="CreateTime")
    protected Long createTime;
    /**
     * 消息类型
     * text 文本消息
     * image 图片消息
     * voice 语音消息
     * video 视频消息
     * music 音乐消息
     */
    @XmlElement(name="MsgType")
    protected String msgType;
    /**
     * 消息id
     */
    @XmlElement(name="MsgId")
    protected Long msgId;
    /**
     * 文本内容
     */
    @XmlElement(name="Content")
    private String content;
    /**
     * 图片链接（由系统生成）
     */
    @XmlElement(name="PicUrl")
    private String picUrl;
    /**
     * 图片消息媒体id，可以调用多媒体文件下载接口拉取数据
     */
    @XmlElement(name="MediaId")
    private String mediaId;
}
