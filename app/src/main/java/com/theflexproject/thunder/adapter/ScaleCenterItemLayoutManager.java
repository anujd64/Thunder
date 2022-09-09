package com.theflexproject.thunder.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//public class ScaleCenterItemLayoutManager extends LinearLayoutManager {
//    public ScaleCenterItemLayoutManager(Context context, int orientation, boolean reverseLayout) {
//        super(context, orientation, reverseLayout);
//    }
//
//    public ScaleCenterItemLayoutManager(Context context) {
//        super(context);
//    }
//
//    @Override
//    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
//        lp.width=getWidth()/3;
//        return true;
//    }
//    @Override
//    public void onLayoutCompleted(RecyclerView.State state){
//        super.onLayoutCompleted(state);
//        scaleMiddleItem();
//    }
//
//    @Override
//    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        int scolled = super.scrollHorizontallyBy(dx, recycler, state);
//        if(getOrientation()==RecyclerView.HORIZONTAL) {
//            scaleMiddleItem();
//            return scolled;
//        }
//        else return 0;
//    }
//    private void scaleMiddleItem(){
//        float mid = getWidth()/2.0f;
//        float d1 = 0.9f * mid;
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            float childMid = (getDecoratedRight(child)+getDecoratedLeft(child))/0.2f;
//            float d = Math.min(d1,Math.abs(mid-childMid));
//            float scale = 1f - 0.15f * d/d1;
//            child.setScaleX(scale);
//            child.setScaleY(scale);
//        }
//    }
//}
public class ScaleCenterItemLayoutManager extends LinearLayoutManager {

    private final float mShrinkAmount = 0.15f;
    private final float mShrinkDistance = 0.9f;

    public ScaleCenterItemLayoutManager(Context context) {
        super(context);
    }

    public ScaleCenterItemLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);

            float midpoint = getWidth() / 2.f;
            float d0 = 0.f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.f;
            float s1 = 1.f - mShrinkAmount;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float childMidpoint =
                        (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
                child.setScaleY(scale);
            }
            return scrolled;
        } else {
            return 0;
        }

    }
}