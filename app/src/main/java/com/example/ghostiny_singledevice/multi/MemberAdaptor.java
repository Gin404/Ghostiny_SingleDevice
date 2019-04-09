package com.example.ghostiny_singledevice.multi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ghostiny_singledevice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MemberAdaptor extends RecyclerView.Adapter<MemberAdaptor.ViewHolder> {
    private Context mContext;
    private List<String> mMemberList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textView;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            textView = (TextView)view.findViewById(R.id.name_txt);
        }
    }

    public MemberAdaptor(Set<String> mMemberSet){
        this.mMemberList = new ArrayList<>();
        this.mMemberList.addAll(mMemberSet);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null){
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.member_name, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String name = mMemberList.get(i);
        viewHolder.textView.setText(name);
    }

    @Override
    public int getItemCount() {
        return mMemberList.size();
    }
}
