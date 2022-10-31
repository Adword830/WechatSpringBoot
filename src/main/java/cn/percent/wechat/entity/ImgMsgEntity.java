package cn.percent.wechat.entity;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * <xml>
 *   <ToUserName><![CDATA[toUser]]></ToUserName>
 *   <FromUserName><![CDATA[fromUser]]></FromUserName>
 *   <CreateTime>12345678</CreateTime>
 *   <MsgType><![CDATA[image]]></MsgType>
 *   <Image>
 *     <MediaId><![CDATA[media_id]]></MediaId>
 *   </Image>
 * </xml>
 * @author pengju.zhang
 * @date 2022-09-02 16:41
 */
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ImgMsgEntity extends OutMsgEntity{

    /**
     * 图片消息媒体id，可以调用多媒体文件下载接口拉取数据
     */
    @XmlElementWrapper(name="Image")
    @XmlElement(name="MediaId")
    private String[] mediaId ;
}
