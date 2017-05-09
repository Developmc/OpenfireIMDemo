package com.example.myopenfiredemo.module.chats.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.model.ChatContent;
import com.example.myopenfiredemo.util.BitmapUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**ChatAdapter
 * Created by clement on 2017/4/19.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    private ArrayList<ChatContent> mChatContents;
    private Context mContext;
    public ChatAdapter(ArrayList<ChatContent> chatContents,Context context){
        this.mChatContents = chatContents;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = 0;
        //如果是发送
        if(viewType == ChatContent.SEND){
            layoutId = R.layout.view_item_chat_right;
        }
        else if(viewType == ChatContent.RECEIVE){
            layoutId = R.layout.view_item_chat_left;
        }
        View itemView = LayoutInflater.from(mContext).inflate(layoutId,parent,false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ChatViewHolder chatViewHolder = (ChatViewHolder) holder;
        final ChatContent chatContent = mChatContents.get(position);
        //如果是文本
        if(chatContent.getMessageType().equals(ChatContent.MessageType.TEXT)){
            chatViewHolder.tvContent.setVisibility(View.VISIBLE);
            chatViewHolder.ivPhoto.setVisibility(View.GONE);
            chatViewHolder.tvContent.setText(chatContent.getContent());
        }
        //如果是图片
        else if(chatContent.getMessageType().equals(ChatContent.MessageType.PHOTO)){
            chatViewHolder.tvContent.setVisibility(View.GONE);
            chatViewHolder.ivPhoto.setVisibility(View.VISIBLE);
            //显示图片
//            Glide.with(mContext).load(chatContent.getContent()).into(chatViewHolder.ivPhoto);
            chatViewHolder.ivPhoto.setImageBitmap(BitmapUtil.string2Bitmap(chatContent.getContent()));
            //为显示的图片设置点击事件
            chatViewHolder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO 不能直接传原图，太大了传不过去的
//                    String photoContent = chatContent.getContent();
//                    Intent intent = new Intent(mContext, ChatPhotoActivity.class);
//                    intent.putExtra(BundleConstant.PHOTO_CONTENT,photoContent);
////                    if (android.os.Build.VERSION.SDK_INT > 20) {
////                        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
////                                (BaseActivity)mContext, view, "shareImageView").toBundle());
////                    } else {
////                        mContext.startActivity(intent);
////                    }
//                    //TODO
//                    mContext.startActivity(intent);
                }
            });
        }
        //显示头像
        Glide.with(mContext).load(chatContent.getAvatar()).into(chatViewHolder.ivIcon);
        chatViewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onIconClickListener!=null){
                    onIconClickListener.onIconClick(v,holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return mChatContents.get(position).getFlag();
    }

    @Override
    public int getItemCount() {
        if(mChatContents == null){
            return 0 ;
        }
        return mChatContents.size();
    }

    /**
     * 左边和右边的控件都是一样的，只是摆放位置不同，用同一个viewHolder来处理(注意：布局中的控件id要一致)
     */
    static class ChatViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public ArrayList<ChatContent> getDatas(){
        return  mChatContents;
    }

    /**
     * 监听头像点击事件，跳转到用户详情页面
     */
    public interface OnIconClickListener{
        void onIconClick(View view,int position);
    }

    private OnIconClickListener onIconClickListener;

    public void setOnIconClickListener(OnIconClickListener onIconClickListener) {
        this.onIconClickListener = onIconClickListener;
    }
}
