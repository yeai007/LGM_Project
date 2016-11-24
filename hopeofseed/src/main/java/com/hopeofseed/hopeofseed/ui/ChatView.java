package com.hopeofseed.hopeofseed.ui;
import java.util.List;
import cn.jpush.im.android.api.model.Message;

/**
 * 聊天界面的接口
 */
public interface ChatView extends MvpView {

    /**
     * 显示消息
     */
    void showMessage(Message message);

    /**
     * 显示消息
     */
    void showMessage(List<Message> messages);

    /**
     * 清除所有消息(离线恢复),并等待刷新
     */
    void clearAllMessage();

    /**
     * 发送消息成功
     *
     * @param message 返回的消息
     */
    void onSendMessageSuccess(Message message);

    /**
     * 发送消息失败
     *
     * @param code 返回码
     * @param desc 返回描述
     * @param message 发送的消息
     */
    void onSendMessageFail(int code, String desc, Message message);


    /**
     * 发送图片消息
     *
     */
    void sendImage();


    /**
     * 发送照片消息
     *
     */
    void sendPhoto();


    /**
     * 发送文字消息
     *
     */
    void sendText();

    /**
     * 发送文件
     *
     */
    void sendFile();


    /**
     * 开始发送语音消息
     *
     */
    void startSendVoice();


    /**
     * 结束发送语音消息
     *
     */
    void endSendVoice();


    /**
     * 发送小视频消息
     *
     * @param fileName 文件名
     */
    void sendVideo(String fileName);


    /**
     * 结束发送语音消息
     *
     */
    void cancelSendVoice();

    /**
     * 正在发送
     *
     */
    void sending();

    /**
     * 显示草稿
     *
     */
   // void showDraft(TIMMessageDraft draft);


}
