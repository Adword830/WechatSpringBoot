package cn.percent.wechat.service;

import cn.percent.wechat.entity.InMsgEntity;
import cn.percent.wechat.entity.OutMsgEntity;
import org.springframework.web.multipart.MultipartFile;
/**
 * @author pengju.zhang
 * @date 2022-08-30 16:32
 */
public interface SendMessage {

    /**
     * 定时通知用户相关的信息
     *
     * @param flag
     * @return
     */
    String send(int flag);

    /**
     * 用户发送过来对应的消息进行对应的回复
     *
     * @param inMsgEntity inMessage
     * @return
     */
    OutMsgEntity inMessage(InMsgEntity inMsgEntity);

    /**
     * 校验微信公众号相关信息
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    String checkSignature(String signature, String timestamp, String nonce, String echostr);

    /**
     *
     * @param jsonStr
     * @return
     */
    String createMenu(String jsonStr);

    /**
     * uploadFile
     * @param multipartFile
     * @param description
     * @param type
     */
    String uploadFile(MultipartFile multipartFile, String description,String type);

    /**
     * getMediaList
     * @param count
     * @param offset
     * @param type
     * @return
     */
    String getMediaList(String count, String offset, String type);
}
