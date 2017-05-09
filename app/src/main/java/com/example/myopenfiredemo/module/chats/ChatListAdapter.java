package com.example.myopenfiredemo.module.chats;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.custom.recyclerview.OnClickListener;
import com.example.myopenfiredemo.custom.recyclerview.OnLongClickListener;
import com.example.myopenfiredemo.model.ChatPerson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**ChatListAdapter
 * Created by clement on 2017/4/18.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private List<ChatPerson> mChatFriends;
    private Context mContext;

    public ChatListAdapter(List<ChatPerson> chatFriends, Context context){
        this.mChatFriends = chatFriends;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ChatPerson chatFriend = mChatFriends.get(position);
        //获取昵称
        holder.tvName.setText(chatFriend.getNickName());
        //显示头像
        Glide.with(mContext).load(chatFriend.getAvatar()).into(holder.ivIcon);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onClick(v,holder.getAdapterPosition());
                }
            }
        });
        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onClickListener!=null){
                    return onLongClickListener.onLongClick(v,holder.getAdapterPosition());
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mChatFriends==null){
            return 0;
        }
        return mChatFriends.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.rootView)
        LinearLayout rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public List<ChatPerson> getDatas(){
        return mChatFriends;
    }

}
