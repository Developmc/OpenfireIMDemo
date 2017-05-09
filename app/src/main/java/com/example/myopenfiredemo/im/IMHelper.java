package com.example.myopenfiredemo.im;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.myopenfiredemo.im.Listener.IMChangePasswordListener;
import com.example.myopenfiredemo.im.Listener.IMLoginListener;
import com.example.myopenfiredemo.model.SearchPerson;
import com.example.myopenfiredemo.util.ComparatorUtil;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**即时通讯帮助类
 * Created by clement on 2017/4/17.
 */

public class IMHelper {

    //服务器IP地址(当前以本机作为服务器，所以就是本机的ip地址)
    private final static String SERVER_IP = "169.254.144.135";
    //服务器端口号
    private final static int SERVER_PORT = 5222;
    //服务器名称,也是服务器域名
    private final static String SERVER_NAME = "127.0.0.1";

    private static IMHelper sInstance = null;
    private XMPPTCPConnection mConnection;

    public static IMHelper getInstance(){
        if(sInstance==null){
            synchronized (IMHelper.class){
                if(sInstance==null){
                    sInstance = new IMHelper();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化配置
     */
    public void init(){
        //初始化ConnectionConfiguration
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        //设置服务器名称
        builder.setServiceName(SERVER_NAME);
        //设置服务器IP地址
        builder.setHost(SERVER_IP);
        //设置服务器端口号
        builder.setPort(SERVER_PORT);
        builder.setCompressionEnabled(false);
        //是否启用debug
        builder.setDebuggerEnabled(true);
        builder.setSendPresence(true);
        //设置安全模式
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        //初始化连接对象
        mConnection = new XMPPTCPConnection(builder.build());
    }

    /**连接服务器并登录，需要异步处理
     * @param userName
     * @param userPassword
     */
    public void connectServerAndLogin(final Activity activity, final String userName, final String userPassword,
                                      final IMLoginListener loginListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Exception tempException = new Exception() ;
                try{
                    if(!isConnected()){
                        //网络操作，没有exception就表示连接成功
                        mConnection.connect();
                    }
                    //执行登录
                    mConnection.login(userName,userPassword);
                    //设置为在线状态
                    Presence presence = new Presence(Presence.Type.available);
                    presence.setStatus("在线（默认状态）");
                    presence.setMode(Presence.Mode.available);
                    mConnection.sendStanza(presence);
                }
                catch (XMPPException e) {
                    tempException = e;
                    e.printStackTrace();
                }
                catch (SmackException e) {
                    tempException = e;
                    e.printStackTrace();
                } catch (IOException e) {
                    tempException = e;
                    e.printStackTrace();
                }
                finally {
                    final Exception exception = tempException;
                    //切换回主线程
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(loginListener!=null){
                                //成功登录
                                if(mConnection.isAuthenticated()){
                                    loginListener.onLogin(true,null);
                                }
                                //连接或登录失败
                                else{
                                    loginListener.onLogin(false,exception);
                                }
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**修改用户密码:修改密码必须在登录的情况下
     * @param activity
     * @param newPassword
     * @param listener
     */
    public void changePassword(final Activity activity, final String newPassword,
                               final IMChangePasswordListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Exception tempException = null ;
                try {
                    //如果还没连接，就先连接
                    if(!isConnected()){
                        mConnection.connect();
                    }
                    //判断是否已经登录
                    if(mConnection.isAuthenticated()){
                        //开始修改密码
                        AccountManager accountManager = AccountManager.getInstance(mConnection);
                        accountManager.changePassword(newPassword);
                    }
                    else{
                        //抛出异常
                        tempException = new Exception("Please login first");
                    }
                }
                catch (XMPPException e) {
                    tempException = e;
                    e.printStackTrace();
                }
                catch (SmackException e) {
                    tempException = e;
                    e.printStackTrace();
                } catch (IOException e) {
                    tempException = e;
                    e.printStackTrace();
                }
                finally {
                    final Exception exception = tempException;
                    //切换回主线程
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(listener!=null){
                                //如果没有异常
                                if(exception==null){
                                    listener.onChangePassword(true,null);
                                }
                                //发生异常
                                else{
                                    listener.onChangePassword(false,exception);
                                }
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**设置是否在线，以及在线状态
     * @param type  设置当前状态，在线，离线等
     * @param mode  在type的基础上，设置在线的状态：忙碌，空闲，离开
     */
    @Deprecated
    public boolean updateUserState(@NonNull Presence.Type type, @NonNull Presence.Mode mode){
        if (!isConnected()){
            return false;
        }
        Presence presence = null;
        //设置离线
        if(type == Presence.Type.unavailable){
            presence = new Presence(Presence.Type.unavailable);
        }
        //如果是在线
        else if(type == Presence.Type.available){
            presence = new Presence(Presence.Type.available);
            //设置在线情况下的状态
            presence.setMode(mode);
            //设置对应状态下的文字描述
            if(Presence.Mode.available.equals(mode)){
                presence.setStatus("在线");
            }
            else if(Presence.Mode.away.equals(mode)){
                presence.setStatus("离开");
            }
            else if(Presence.Mode.chat.equals(mode)){
                presence.setStatus("空闲");
            }
            else if(Presence.Mode.dnd.equals(mode)){
                presence.setStatus("忙碌");
            }
            else if(Presence.Mode.xa.equals(mode)){
                presence.setStatus("长时间离开");
            }
            else{
                presence.setStatus("在线");
            }
        }
        //其他情况,暂不考虑
        else{
            presence = new Presence(Presence.Type.available);
        }
        try {
            mConnection.sendStanza(presence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**更新状态
     * @param status
     * @return
     */
    public boolean updateUserState(@NonNull String status){
        if (!isConnected()){
            return false;
        }
        Presence presence = null;
        if(status.equals("在线")){
            presence = new Presence(Presence.Type.available);
            //设置在线情况下的状态
            presence.setMode(Presence.Mode.available);
            //设置对应状态下的文字描述
            presence.setStatus("在线（默认状态）");
        }
        else if(status.equals("离开")){
            presence = new Presence(Presence.Type.available);
            presence.setMode(Presence.Mode.away);
            presence.setStatus("离开一会噢");
        }
        else if(status.equals("空闲")){
            presence = new Presence(Presence.Type.available);
            presence.setMode(Presence.Mode.chat);
            presence.setStatus("喜欢我，就扫我吧");
        }
        else if(status.equals("忙碌")){
            presence = new Presence(Presence.Type.available);
            presence.setMode(Presence.Mode.dnd);
            presence.setStatus("我很忙");
        }
        else if(status.equals("长时间离开")){
            presence = new Presence(Presence.Type.available);
            presence.setMode(Presence.Mode.xa);
            presence.setStatus("半天之后回来");
        }
        else if(status.equals("离线")){
            presence = new Presence(Presence.Type.unavailable);
            presence.setStatus("已离线");
        }
        else {
            presence = new Presence(Presence.Type.available);
            presence.setMode(Presence.Mode.available);
            presence.setStatus("在线（默认状态）");
        }
        try {
            mConnection.sendStanza(presence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**获取到XMPPTCPConnection实例
     * @return
     */
    public XMPPTCPConnection getConnection() {
        if(mConnection==null){
            //抛出一个运行时错误
            throw new RuntimeException("mConnection is null,Please init first");
        }
        return mConnection;
    }

    /**判断当前是否处于连接状态
     * @return
     */
    public boolean isConnected(){
        if(getConnection().isConnected()){
            return true;
        }
        return false;
    }

    /**获取好友列表(需要在服务器互相添加成为好友才能拉去)
     * @return
     */
    public Collection<RosterEntry> getAllFriends(){
        Roster roster = Roster.getInstanceFor(mConnection);
        return roster.getEntries();
    }

    /**获取所有分组
     * @return
     */
    public Collection<RosterGroup> getRosterGroups(){
        Roster roster = Roster.getInstanceFor(mConnection);
        return roster.getGroups();
    }

    /**获取所有好友的详细信息
     * @return
     */
    public List<VCard> getAllFriendsInfo(){
        List<VCard> vCards = new ArrayList<>();
        for(RosterEntry rosterEntry : getAllFriends()){
            String userJID = rosterEntry.getUser();
            VCard vCard = getUserVCard(userJID);
            vCards.add(vCard);
        }
        return vCards;
    }

    /**根据userJID获取指定账号的好友信息：这里的信息是运行时信息，基本信息使用VCard获取
     * @param userJID
     * @return
     */
    public RosterEntry getFriend(String userJID){
        if(isConnected()){
            return Roster.getInstanceFor(mConnection).getEntry(userJID);
        }
        return null;
    }

    /**获取AccountAttributes,好像没什么卵用
     * @return
     */
    @Deprecated
    public Set getAccountAttributes(){
        if(isConnected()){
            try {
                return AccountManager.getInstance(mConnection).getAccountAttributes();
            } catch (SmackException.NoResponseException e) {
                e.printStackTrace();
            } catch (XMPPException.XMPPErrorException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**获取当前登录用户的VCard信息 (用户的基本信息使用VCard进行读取)
     * @return
     */
    public VCard getSelfVCard(){
        VCardManager cardManager = VCardManager.getInstanceFor(getConnection());
        try {
            //如果已经登录
            if(mConnection.isAuthenticated()){
                return cardManager.loadVCard();
            }
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**获取指定userJID的VCard信息
     * @param userJID
     * @return
     */
    public VCard getUserVCard(String userJID){
        VCardManager cardManager = VCardManager.getInstanceFor(getConnection());
        try {
            //如果已经登录
            if(mConnection.isAuthenticated()){
                return cardManager.loadVCard(userJID);
            }
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**根据userJID获取用户头像
     * @param userJID
     * @return
     */
    public byte[] getAvatar(String userJID){
        VCard vCard = getUserVCard(userJID);
        if(vCard!=null){
            return vCard.getAvatar();
        }
        return null;
    }

    /**更新当前用户的基本信息
     * @return
     */
    public boolean updateVCard(String firstName,String middleName,String lastName,String nickName,
                               String emailHome,byte[] avatars){
        VCardManager cardManager = VCardManager.getInstanceFor(getConnection());
        try {
            //如果已经登录
            if(mConnection.isAuthenticated()){
                VCard vCard =  cardManager.loadVCard();
                //更新基本信息
                vCard.setFirstName(firstName);
                vCard.setMiddleName(middleName);
                vCard.setLastName(lastName);
                vCard.setNickName(nickName);
                vCard.setEmailHome(emailHome);
                //修改图片
                vCard.setAvatar(avatars);
                //提交更改
                cardManager.saveVCard(vCard);
                return true;
            }
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**查找用户（模糊查找）
     * @param userName 所要查找的用户名
     * @return List<SearchPerson>
     */
    public List<SearchPerson> searchUsers(String userName){
        //拼凑searchService: "search."+服务器名称（也是服务器域名，这里是"127.0.0.1"）
        String searchService = "search." + getConnection().getServiceName();
        List<SearchPerson> searchPersons = new ArrayList<>();
        UserSearchManager userSearchManager = new UserSearchManager(getConnection());
        try {
            Form searchForm = userSearchManager.getSearchForm(searchService);
            Form answerForm = searchForm.createAnswerForm();
            //这两个值是特定的，不要更改大小写
            answerForm.setAnswer("Username", true);
            answerForm.setAnswer("search", userName);
            ReportedData data = userSearchManager.getSearchResults(answerForm, searchService);
            for(ReportedData.Row row : data.getRows()){
                List<String> emails = row.getValues("Email");
                List<String> userJIDs = row.getValues("jid");
                List<String> userNames = row.getValues("Username");
                List<String> names = row.getValues("Name");
                SearchPerson person = new SearchPerson();
                //虽然说返回的都是List<String>，但好像都只有一项数据，故这里只取第一项数据
                if(emails!=null && !emails.isEmpty()){
                    person.setEmail(emails.get(0));
                }
                if(userJIDs!=null && !userJIDs.isEmpty()){
                    person.setUserJID(userJIDs.get(0));
                }
                if(userNames!=null && !userNames.isEmpty()){
                    person.setUserName(userNames.get(0));
                }
                if(names!=null && !names.isEmpty()){
                    person.setName(names.get(0));
                }
                searchPersons.add(person);
            }
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        //将查询结果进行排序
        if(!searchPersons.isEmpty()){
            Collections.sort(searchPersons, ComparatorUtil.searchPersonComparator());
        }
        return searchPersons;
    }

    /**添加好友
     * @param userJID 用户名（不可更改）
     * @param nickName 昵称
     * @param groups   组
     * @return
     */
    public boolean addFriend(String userJID, String nickName, String... groups){
        Roster roster = Roster.getInstanceFor(mConnection);
        try {
            roster.createEntry(userJID,nickName,groups);
            return true;
        } catch (SmackException.NotLoggedInException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**删除好友
     * @param userJID
     * @return
     */
    public boolean removeFriend(@NonNull String userJID){
        Roster roster = Roster.getInstanceFor(mConnection);
        RosterEntry entry = roster.getEntry(userJID);
        try {
            roster.removeEntry(entry);
            return true;
        } catch (SmackException.NotLoggedInException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**获取发送文件的发送转换器，需使用该对象处理发送文件
     * @param userJID 注意这里的JID是：clement-test1@127.0.0.1/Smack的格式
     * @return
     */
    public OutgoingFileTransfer getSendFileTransfer(String userJID){
        if(isConnected()){
            ServiceDiscoveryManager sdm = ServiceDiscoveryManager
                    .getInstanceFor(mConnection);
            sdm.addFeature("http://jabber.org/protocol/disco#info");
            sdm.addFeature("jabber:iq:privacy");
            FileTransferNegotiator.IBB_ONLY = true;
            FileTransferNegotiator.getInstanceFor(mConnection);
            return FileTransferManager.getInstanceFor(mConnection).createOutgoingFileTransfer(userJID);
        }
        else {
            throw new NullPointerException("连接服务器失败");
        }
    }

    /**发送文件
     * @param userJID
     * @param file
     * @param description
     */
    public void sendFile(String userJID,final File file,final String description){
        //拼凑符合要求的userJID
        userJID = userJID + "/Smack";
        //获取文件传输对象
        final OutgoingFileTransfer transfer = getSendFileTransfer(userJID);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //发送文件
                try {
                    transfer.sendFile(file,description);
                } catch (SmackException e) {
                    e.printStackTrace();
                }

                //文件传输过程中的状态监听分析
                if(transfer.getProgress() < 1) {//开始传输
                    //传输进度，值为0~1
                }
                while(!transfer.isDone()) {//判断传输是否完成，传输取消、传输完成、传输发生错误都会返回true
                    try {
                        Thread.sleep(200);
                        Log.d("进度：",transfer.getProgress()+"");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(FileTransfer.Status.complete.equals(transfer.getStatus())) {
                    //传输完成
                    Log.d("FileTransfer: ","完成");
                } else if(FileTransfer.Status.cancelled.equals(transfer.getStatus())) {
                    //传输取消
                    Log.d("FileTransfer: ","取消");
                } else if(FileTransfer.Status.error.equals(transfer.getStatus())) {
                    //传输错误
                    Log.d("FileTransfer: ","错误");
                } else if(FileTransfer.Status.refused.equals(transfer.getStatus())) {
                    //传输拒绝
                    Log.d("FileTransfer: ","拒绝");
                }
            }
        }).start();

    }

    /**发送图片
     * @param userJID
     * @param photoPath
     */
    public void sendPhoto(@NonNull String userJID,@NonNull String photoPath){
        File photoFile = new File(photoPath);
        String description = "image";
        sendFile(userJID,photoFile,description);
    }

    /**添加文件接收的监听
     * @param fileTransferListener
     */
    public void addFileTransferListener(FileTransferListener fileTransferListener){
        if(isConnected()){
            FileTransferManager.getInstanceFor(mConnection).addFileTransferListener(fileTransferListener);
        }
    }

    /**退出登录
     * @return
     */
    public boolean logout(){
        try{
            mConnection.instantShutdown();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
