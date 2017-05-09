package com.example.myopenfiredemo.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.activity.BaseActivity;
import com.example.myopenfiredemo.im.IMHelper;
import com.example.myopenfiredemo.im.Listener.IMLoginListener;
import com.example.myopenfiredemo.module.navigation.NavigationActivity;
import com.example.myopenfiredemo.util.DialogUtil;
import com.example.myopenfiredemo.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.et_first_name)
    EditText etUserName;
    @BindView(R.id.et_user_password)
    EditText etUserPassword;

    @Override
    public int onBindLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public void initBehavior(Bundle savedInstanceState) {

        etUserName.setText("clement-test2");
        etUserPassword.setText("a12345678");
    }

    @OnClick(R.id.btn_login)
    void onLogin(){
        String userName = etUserName.getText().toString();
        String userPassword = etUserPassword.getText().toString();
        if(userName.isEmpty()){
            ToastUtil.show(LoginActivity.this,"请输入用户名");
            return;
        }
        if(userPassword.isEmpty()){
            ToastUtil.show(LoginActivity.this,"请输入密码");
            return;
        }
        //初始化配置
        IMHelper.getInstance().init();
        DialogUtil.showLoadingDialog(LoginActivity.this,getString(R.string.logging_in));
        //执行连接登录操作
        IMHelper.getInstance().connectServerAndLogin(
                LoginActivity.this, userName,userPassword,new IMLoginListener(){
                    @Override
                    public void onLogin(boolean isLogin, @Nullable Exception exception) {
                        //隐藏弹框
                        DialogUtil.hideLoadingDialog();
                        if(isLogin){
                            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                        else{
                            if(exception!=null){
                                ToastUtil.show(LoginActivity.this,exception.getMessage());
                            }
                            else{
                                ToastUtil.show(LoginActivity.this,"连接失败");
                            }
                        }
                    }
                });
    }

//    @OnClick(R.id.btn_listener)
//    void onListener(){
//        //获取好友列表
//        XMPPConnection connection= IMHelper.getInstance().getConnection();
//        Roster roster = Roster.getInstanceFor(connection);
//        //获取所有好友
//        Collection<RosterEntry> rosterEntries = roster.getEntries();
//        for(RosterEntry rosterEntry:rosterEntries){
//            Log.i("---", rosterEntry.getName());
//        }
//        //获取组
//        Collection<RosterGroup> entriesGroup = roster.getGroups();
//        for(RosterGroup group: entriesGroup){
//            Collection<RosterEntry> entries = group.getEntries();
//            Log.i("---", group.getName());
//            for (RosterEntry entry : entries) {
//                //Presence presence = roster.getPresence(entry.getUser());
//                //Log.i("---", "user: "+entry.getUser());
//                Log.i("---", "name: "+entry.getName());
//                //Log.i("---", "type: "+entry.getType());
//                //Log.i("---", "status: "+entry.getStatus());
//                //Log.i("---", "groups: "+entry.getGroups());
//            }
//        }
//    }

    /**
     * 初始化建立连接的配置
     */
//    private void initXMPPTCPConnection(){
//        //初始化ConnectionConfiguration
//        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
//        //设置服务器名称
//        builder.setServiceName(SERVER_NAME);
//        //设置服务器IP地址
//        builder.setHost(SERVER_IP);
//        //设置服务器端口号
//        builder.setPort(SERVER_PORT);
//        builder.setCompressionEnabled(false);
//        //是否启用debug
//        builder.setDebuggerEnabled(true);
//        builder.setSendPresence(true);
//        //设置安全模式
//        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
//        //初始化连接对象
//        mConnection = new XMPPTCPConnection(builder.build());
//    }

//    /**连接服务器并登录，需要异步处理
//     * @param userName
//     * @param userPassword
//     */
//    private void connectServerAndLogin(final String userName, final String userPassword){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    //网络操作，没有exception就表示连接成功
//                    mConnection.connect();
//                    //执行登录
//                    mConnection.login(userName,userPassword);
//                    Presence presence = new Presence(Presence.Type.available);
//                    presence.setStatus("我是在线状态");
//                    mConnection.sendStanza(presence);
//                    mChatManager = ChatManager.getInstanceFor(mConnection);
//                    String user = mConnection.getUser();
//                    mChatManager.createChat(mConnection.getUser(), "threadId", new ChatMessageListener() {
//                        @Override
//                        public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
//                            String content = message.getBody();
//                        }
//                    });
//                    mChatManagerListener = new ChatManagerListener() {
//                        @Override
//                        public void chatCreated(Chat chat, boolean createdLocally) {
//                            chat.addMessageListener(new ChatMessageListener() {
//                                @Override
//                                public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
//                                    String content=message.getBody();
//                                    if (content!=null){
//                                        Message handleMessage = Message.obtain();
//                                        handleMessage.what=MessageConstant.MESSAGE_RECEIVE_MESSAGE;
//                                        handleMessage.obj="收到消息：" + message.getBody()+" 来自:"+message.getFrom();
//                                        mHandler.sendMessage(handleMessage);
//                                    }
//
//                                }
//                            });
//                        }
//                    };
//                    mChatManager.addChatListener(mChatManagerListener);
//                }
//                catch (XMPPException e) {
//                    e.printStackTrace();
//                }
//                catch (SmackException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                finally {
//                    //成功建立连接
//                    if(mConnection.isConnected()){
//                        mHandler.sendEmptyMessage(MessageConstant.MESSAGE_CONNECTION_SUCCESS);
//                    }
//                    //连接失败
//                    else{
//                        mHandler.sendEmptyMessage(MessageConstant.MESSAGE_CONNECTION_FAIL);
//                    }
//                }
//            }
//        }).start();
//    }

//    private void changeListener(){
//        mChatManager.removeChatListener(mChatManagerListener);
//        mChatManager.addChatListener(new ChatManagerListener() {
//            @Override
//            public void chatCreated(Chat chat, boolean createdLocally) {
//                chat.addMessageListener(new ChatMessageListener() {
//                    @Override
//                    public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
//                        String content=message.getBody();
//                        if (content!=null){
//                            android.os.Message handleMessage = android.os.Message.obtain();
//                            handleMessage.what=MessageConstant.MESSAGE_RECEIVE_MESSAGE;
//                            handleMessage.obj="收到消息：" + message.getBody()+" 来自:"+message.getFrom();
//                            mHandler.sendMessage(handleMessage);
//                        }
//                    }
//                });
//            }
//        });
//    }

}
