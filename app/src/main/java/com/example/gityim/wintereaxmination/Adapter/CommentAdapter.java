package com.example.gityim.wintereaxmination.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        if(context==null){
            context=viewGroup.getContext();
        }

        LayoutInflater inflater =LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.comment_item,viewGroup,false);
        final CommentViewHolder holder =new CommentViewHolder(view);

        holder.praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPressed=false;
                int position =holder.getAdapterPosition();
                final int nums=mComments.get(position).getLikes();
                int num1=nums,num2=nums;
                if(!isPressed){

                    num1=nums+1;
                    holder.like.setText(num1+"");
                    holder.praise.setImageResource(R.drawable.praised);
                    isPressed=true;
                }else{
                    num2=num1-1;
                    holder.like.setText(num2+"");
                    isPressed=false;
                }


            }
        });
        //设置点赞点击事件
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder commentViewHolder, int i) {
        CommentViewHolder holder =(CommentViewHolder) commentViewHolder;
        holder.name.setText(mComments.get(i).getAuthor());
        holder.comment.setText(mComments.get(i).getContent());
        final int nums= mComments.get(i).getLikes();
        holder.like.setText(nums+"");
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
        ImageView praise;
        TextView like;
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
            like=itemView.findViewById(R.id.praise_text);
            praise=itemView.findViewById(R.id.praise_view);
        }

    }
    public long getItemId(int position){
        return position;
    }


}
