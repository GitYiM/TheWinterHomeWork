package com.example.gityim.wintereaxmination.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gityim.wintereaxmination.R;
import com.example.gityim.wintereaxmination.bean.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private ArrayList<Comment> mComments;
    private Context context;
    CommentViewHolder holder=null;


    public CommentAdapter(ArrayList<Comment> mComments, Context context) {
        this.mComments = mComments;
        this.context = context;
    }


    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view=LayoutInflater.from(context).inflate(R.layout.comment_item,viewGroup,false);
//        holder =new CommentViewHolder(view);
//        return holder;
        LayoutInflater inflater =LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.comment_item,viewGroup,false);
        holder =new CommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder commentViewHolder, int i) {
        String name=mComments.get(i).getAuthor();
        holder.name.setTag(name);
        if(name==holder.name.getTag()){
            holder.name.setText(mComments.get(i).getAuthor());
        }
        String comment =mComments.get(i).getContent();
        holder.comment.setTag(comment);
        if(comment.equals(holder.comment.getTag())){
            holder.comment.setText(mComments.get(i).getContent());
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeLong = Long.valueOf(mComments.get(i).getTime());
        holder.time.setText(sdf.format(new Date(timeLong*1000L)));
        Log.d("时间", "onBindViewHolder: "+sdf.format(new Date(mComments.get(i).getTime())));
        Glide.with(context).load(mComments.get(i).getAvatar()).placeholder(R.drawable.timg).into(holder.personPhoto);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView comment;
        TextView time;
        CircleImageView personPhoto;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.time_text);
            name=itemView.findViewById(R.id.person_name);
            comment=itemView.findViewById(R.id.comment_text);
            personPhoto=itemView.findViewById(R.id.person_photo);
        }
    }
    public long getItemId(int position){
        return position;
    }


}
