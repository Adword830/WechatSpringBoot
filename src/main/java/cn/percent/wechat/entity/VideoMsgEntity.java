package cn.percent.wechat.entity;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * <xml>
 *   <ToUserName><![CDATA[toUser]]></ToUserName>
 *   <FromUserName><![CDATA[fromUser]]></FromUserName>
 *   <CreateTime>12345678</CreateTime>
 *   <MsgType><![CDATA[video]]></MsgType>
 *   <Video>
 *     <MediaId><![CDATA[media_id]]></MediaId>
 *     <Title><![CDATA[title]]></Title> 不必填
 *     <Description><![CDATA[description]]></Description> 不必填
 *   </Video>
 * </xml>
 *
 * @author pengju.zhang
 * @date 2022-09-02 15:54
 */
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class VideoMsgEntity extends OutMsgEntity{

    /**
     * 图片消息媒体id，可以调用多媒体文件下载接口拉取数据
     */
    @XmlElementWrapper(name="Video")
    @XmlElement(name="MediaId")
    private String[] mediaId ;

}
