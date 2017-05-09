package com.example.myopenfiredemo.module.search;

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
import com.example.myopenfiredemo.model.SearchPerson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**SearchUserAdapter
 * Created by clement on 2017/4/22.
 */
public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private List<SearchPerson> mSearchPerson;
    private Context mContext;

    public SearchUserAdapter(List<SearchPerson> searchPersons, Context context){
        this.mSearchPerson = searchPersons;
        this.mContext = context;
    }

    @Override
    public SearchUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_list,parent,false);
        return new SearchUserAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SearchUserAdapter.ViewHolder holder, int position) {
        SearchPerson searchPerson = mSearchPerson.get(position);
        //获取昵称
        holder.tvName.setText(searchPerson.getUserName());
        //显示头像
        Glide.with(mContext).load(searchPerson.getAvatar()).into(holder.ivIcon);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onClick(v,holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mSearchPerson ==null){
            return 0;
        }
        return mSearchPerson.size();
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

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public List<SearchPerson> getDatas(){
        return mSearchPerson;
    }

}