package com.example.myopenfiredemo.im;

import org.jivesoftware.smack.chat.Chat;

/**监听IM接收消息的回调
 * Created by clement on 2017/4/17.
 */

public interface IMReceiveListener {
    void onIMReceive(Chat chat, org.jivesoftware.smack.packet.Message message);
}
