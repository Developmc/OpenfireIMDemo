package com.example.myopenfiredemo.module.chats;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.myopenfiredemo.constants.BundleConstant;
import com.example.myopenfiredemo.custom.recyclerview.OnClickListener;
import com.example.myopenfiredemo.custom.recyclerview.OnLongClickListener;
import com.example.myopenfiredemo.im.IMHelper;
import com.example.myopenfiredemo.model.ChatPerson;
import com.example.myopenfiredemo.util.DialogUtil;

import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**ChatListPresenter
 * Created by clement on 2017/4/19.
 */

public class ChatListPresenter implements ChatListContract.Presenter{
    private Context mContext;
    private ChatListContract.View mView;

    private ChatListAdapter adapter;

    public ChatListPresenter(ChatListContract.View view,Context context){
        this.mView = view;
        this.mContext = context;
        mView.setPresenter(this);
    }


    @Override
    public void start() {
        initTitle();
        initRecyclerView();
    }

    private void initTitle(){
        mView.setTitle("好友列表");
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mView.getRecyclerView().setLayoutManager(manager);
        adapter = new ChatListAdapter(getCharPersons(),mContext);
        mView.getRecyclerView().setAdapter(adapter);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(BundleConstant.USER_NAME,adapter.getDatas().get(position).getNickName());
                bundle.putString(BundleConstant.USER_JID,adapter.getDatas().get(position).getUserJID());
                //跳转到ChatFragment
                mView.toChatFragment(bundle);
            }
        });
        adapter.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view, final int position) {
                String nickName = adapter.getDatas().get(position).getNickName();
                final String userJID = adapter.getDatas().get(position).getUserJID();
                DialogUtil.showCommonDialog(mContext, "删除好友", "是否要删除 " + nickName, "确定",
                        new DialogUtil.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Dialog dialog, @NonNull DialogUtil.ButtonType which) {
                                dialog.dismiss();
                                //移除好友
                                IMHelper.getInstance().removeFriend(userJID);
                                //更新列表
                                adapter.notifyItemRemoved(position);
                                adapter.getDatas().remove(position);
                            }
                        }, "取消", new DialogUtil.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Dialog dialog, @NonNull DialogUtil.ButtonType which) {
                                dialog.dismiss();
                            }
                        });
                return true;
            }
        });
    }

    /**获取所有的好友列表
     * @return
     */
    @Override
    public List<ChatPerson> getCharPersons() {
        List<ChatPerson> chatFriends = new ArrayList<>();
        //获取所有的好友列表(这里包含userJID)
        Collection<RosterEntry> rosterEntries = IMHelper.getInstance().getAllFriends();
        //获取所有好友的详细信息(这里没有包含userJID)
        List<VCard> vCards = IMHelper.getInstance().getAllFriendsInfo();
        //拼凑一个用于展示内容的chatFriends
        int index = 0;
        for(RosterEntry rosterEntry : rosterEntries){
            ChatPerson chatFriend = new ChatPerson();
            chatFriend.setUserJID(rosterEntry.getUser());
            chatFriend.setNickName(rosterEntry.getName());
            chatFriend.setAvatar(vCards.get(index).getAvatar());
            chatFriends.add(chatFriend);
            index++;
        }
        return chatFriends;
    }

    @Override
    public void refreshList() {
        //清除原来的数据
        adapter.getDatas().clear();
        adapter.getDatas().addAll(getCharPersons());
        adapter.notifyDataSetChanged();
    }
}
