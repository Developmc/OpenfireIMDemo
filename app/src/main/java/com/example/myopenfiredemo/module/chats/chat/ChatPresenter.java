package com.example.myopenfiredemo.module.chats.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.myopenfiredemo.constants.BundleConstant;
import com.example.myopenfiredemo.constants.MessageConstant;
import com.example.myopenfiredemo.im.IMHelper;
import com.example.myopenfiredemo.model.ChatContent;
import com.example.myopenfiredemo.module.chats.chat.adapter.ChatAdapter;
import com.example.myopenfiredemo.util.BitmapUtil;
import com.example.myopenfiredemo.util.FileUtil;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**ChatPresenter
 * Created by clement on 2017/4/19.
 */

public class ChatPresenter implements ChatContract.Presenter{
    private Context mContext;
    private ChatContract.View mView;
    //好友的userName和好友的userJID
    private String userName,userJID;
    private ChatAdapter adapter;
    private byte[] leftAvatar, rightAvatar;

    public ChatPresenter(ChatContract.View view,Context context){
        this.mContext = context;
        this.mView = view;
        mView.setPresenter(this);
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case MessageConstant.MESSAGE_RECEIVE_MESSAGE:
                    Message message = (Message) msg.obj;
                    //更新UI
                    refreshUI(message);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void start() {
        initData();
        initView();
        //监听消息
        addMessageListener();
        //监听文件接收
        addReceiveFileListener();
    }

    @Override
    public void initData() {
        userName = mView.getBundle().getString(BundleConstant.USER_NAME,"");
        userJID = mView.getBundle().getString(BundleConstant.USER_JID,"");
        leftAvatar = IMHelper.getInstance().getUserVCard(userJID).getAvatar();
        rightAvatar = IMHelper.getInstance().getSelfVCard().getAvatar();
    }

    @Override
    public void initView() {
        mView.setTitle(userName);
        initRecyclerView();
    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mView.getRecyclerView().setLayoutManager(manager);
        ArrayList<ChatContent> chatContents = new ArrayList<>();
        adapter= new ChatAdapter(chatContents,mContext);
        mView.getRecyclerView().setAdapter(adapter);
        adapter.setOnIconClickListener(new ChatAdapter.OnIconClickListener() {
            @Override
            public void onIconClick(View view, int position) {
                //根据userJID获取对应用户的详情信息；当前登录的账号不需要userJID也能获取详情信息
                Bundle bundle = new Bundle();
                bundle.putString(BundleConstant.USER_JID,adapter.getDatas().get(position).getUserJID());
                mView.intent2UserDetailFragment(bundle);
            }
        });
    }

    /**
     * 监听接收到的message
     */
    @Override
    public void addMessageListener() {
        ChatManager chatmanager = ChatManager.getInstanceFor(IMHelper.getInstance().getConnection());
        chatmanager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                chat.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
                        String content=message.getBody();
                        if (content!=null){
                            Log.e("TAG", "from:" + message.getFrom() + " to:" + message.getTo() + " message:" + message.getBody());
                            android.os.Message Message= android.os.Message.obtain();
                            Message.what= MessageConstant.MESSAGE_RECEIVE_MESSAGE;
                            Message.obj=message;
                            mHandler.sendMessage(Message);
                        }
                    }
                });
            }
        });
    }

    /**根据接收到的message更新界面
     * @param message
     */
    private void refreshUI(@NonNull Message message){
        int messageType = getMessageType(message);
        ChatContent chatContent = new ChatContent();
        //如果是文本
        if(messageType==1){
            //构建一个会话对象
            chatContent = new ChatContent(message.getBody(), ChatContent.MessageType.TEXT,
                    ChatContent.RECEIVE,userJID, leftAvatar);
        }
        else if(messageType==2){
            chatContent = new ChatContent(message.getBody(), ChatContent.MessageType.PHOTO,
                    ChatContent.RECEIVE,userJID, leftAvatar);
        }
        insertRecyclerView(chatContent);
    }

    /**获取返回的消息类型：1代表文本，2代表图片
     * @param message
     * @return
     */
    private int getMessageType(@NonNull Message message){
        int messageType = 1;
        for(Message.Subject subject:message.getSubjects()){
            if("ImageType".equals(subject.getSubject())){
                messageType = 2;
                break;
            }
        }
        return messageType;
    }
    /**向recyclerView插入一条数据
     * @param chatContent
     */
    private void insertRecyclerView(@NonNull ChatContent chatContent){
        adapter.getDatas().add(chatContent);
        //插入一条数据
        adapter.notifyItemInserted(adapter.getItemCount()-1);
        //滚动到当前数据
        mView.getRecyclerView().scrollToPosition(adapter.getItemCount()-1);
    }

    /**发送文本信息
     * @param textContent ：文本的内容
     */
    @Override
    public void sendTextMessage(@NonNull String textContent) {
        try {
            ChatManager chatmanager = ChatManager.getInstanceFor(IMHelper.getInstance().getConnection());
            Chat mChat = chatmanager.createChat(userJID);
            mChat.sendMessage(textContent);
            //构建一个对话的对象,向recycleView插入一条数据
            //该对话对象属于当前登录的账号，不知道如何获取userJID，用空或者null表示该对象属于当前登录的账号
            ChatContent chatContent = new ChatContent(textContent,ChatContent.MessageType.TEXT,
                    ChatContent.SEND,"", rightAvatar);
            insertRecyclerView(chatContent);
            //清空输入框的内容
            mView.clearInput();
        }
        catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**发送图片信息
     * @param photoPath ：图片的路径
     */
    @Override
    public void sendPhotoMessage(@NonNull String photoPath) {
        try {
            ChatManager chatmanager = ChatManager.getInstanceFor(IMHelper.getInstance().getConnection());
            Chat mChat = chatmanager.createChat(userJID);
            Bitmap tempBitmap = BitmapUtil.getBitmapFromPath(photoPath);
            String photoContent = BitmapUtil.bitmap2String(tempBitmap);
            //构造一个message，用于传递图片
            Message message = new Message();
            message.setBody(photoContent);
            //用ImageType标记是图片类型
            message.setSubject("ImageType");
            mChat.sendMessage(message);
            //用发送文件的形式发送图片
//          IMHelper.getInstance().sendPhoto(userJID,content);
            ChatContent chatContent = new ChatContent(photoContent,ChatContent.MessageType.PHOTO,
                    ChatContent.SEND,"", rightAvatar);
            insertRecyclerView(chatContent);
        }
        catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件接收的监听器
     */
    public void addReceiveFileListener() {
        IMHelper.getInstance().addFileTransferListener(new FileTransferListener() {
            @Override
            public void fileTransferRequest(FileTransferRequest request) {
                //接收文件
                IncomingFileTransfer transfer = request.accept();
                try {
                    String description = request.getDescription();
                    //在目录fileDir目录下新建一个名字为request.getFileName()的文件
                    File file = new File(FileUtil.getReceiverFolderPath(),request.getFileName());
                    //开始接收文件(将传输过来的文件内容输出到file中)
                    transfer.recieveFile(file);
                    //此处执行文件传输监听
                } catch (SmackException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
