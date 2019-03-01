package com.example.gityim.wintereaxmination;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndLessOnScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager mLinearLayoutManager;
    private int toltalItemCount;
    private int currentPage=0;
    private int previousTotal=0;
    private int visibleItemCount;
    private int firstVisibleItem;
    private boolean loading=true;

    public EndLessOnScrollListener(LinearLayoutManager mLinearLayoutManager) {
        this.mLinearLayoutManager = mLinearLayoutManager;
    }

    public void onScrolled(RecyclerView recyclerView,int dx,int dy){
        super.onScrolled(recyclerView,dx,dy);
//        visibleItemCount= recyclerView.getChildCount();
//        toltalItemCount=mLinearLayoutManager.getItemCount();
//        firstVisibleItem=mLinearLayoutManager.findFirstVisibleItemPosition();
//        if(loading){
//            if (toltalItemCount>previousTotal){
//                loading=false;
//                previousTotal=toltalItemCount;
//            }
//        }
//        if(!loading&&toltalItemCount-visibleItemCount<=firstVisibleItem){
//            currentPage++;
//            onLoadMore(currentPage);
//            loading=true;
//        }
        boolean state=isSlideToBottom(recyclerView);
        if(state){
            onLoadMore();
        }

    }
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    protected abstract void onLoadMore();
}
