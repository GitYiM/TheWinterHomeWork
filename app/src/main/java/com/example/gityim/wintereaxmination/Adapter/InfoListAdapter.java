package com.example.gityim.wintereaxmination.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gityim.wintereaxmination.FireNewsActivity;
import com.example.gityim.wintereaxmination.bean.Item;
import com.example.gityim.wintereaxmination.R;
import com.example.gityim.wintereaxmination.RecyclerClickListener;

import java.util.ArrayList;

public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.InfoViewHolder> {
    private ArrayList<Item> mData;
    private Context mContext;


    private RecyclerClickListener mItemClicklistener;


    public class InfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView pic;
        TextView headTitle;
        private RecyclerClickListener mlistener;


        public InfoViewHolder(View itemView, RecyclerClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            pic = itemView.findViewById(R.id.item_image);
            headTitle = itemView.findViewById(R.id.info_title);
            this.mlistener = listener;
            this.itemView.setOnClickListener(this);
        }
//        public InfoViewHolder(View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.item_title);
//            pic = itemView.findViewById(R.id.item_image);
//            headTitle = itemView.findViewById(R.id.info_title);
//        }


        @Override
        public void onClick(View v) {
            if (mlistener != null) {
                mlistener.onItemClick(v, getAdapterPosition());
            }
        }
    }


    public InfoListAdapter(ArrayList<Item> mData, Context mContext) {//接收上下文和展示数据
        this.mData = mData;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.info_item, viewGroup, false);
        final InfoViewHolder holder = new InfoViewHolder(view, mItemClicklistener);
        return holder;

    }

    public void setOnItemClickListener(RecyclerClickListener listener) {
        this.mItemClicklistener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder infoViewHolder, int i) {
        InfoViewHolder holder =(InfoViewHolder) infoViewHolder;
        holder.title.setText(mData.get(i).getTitle());
        Glide.with(mContext).load(mData.get(i).getPicurl()).placeholder(R.drawable.timg).into(holder.pic);
//        final String tag=mData.get(i).getHeadTitle();
//        if (tag!=null){
//            holder.headTitle.setText(mData.get(i).getHeadTitle());
//            holder.headTitle.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    /*
        public static final int TYPE_HEADER =0;//headview
        public static final int TYPE_NORMAL=1;//item
        private View headView;//banner

        public void setHeadView(View headView) {
            this.headView = headView;
            notifyItemInserted(0);
        }
        public View getHeadView(){
            return headView;
        }
        public int getItemViewType(int position){
            if(headView==null){
                return TYPE_NORMAL;
            }
            if(position==0){
                return TYPE_HEADER;
            }
            return TYPE_NORMAL;
        }
        private int getRealPosition(RecyclerView.ViewHolder holder){
            int position=holder.getLayoutPosition();
            return headView==null?position:position-1;
        }*/
    public void resetDatas() {
        mData = new ArrayList<>();
    }
}
