package com.example.aliyunplayer.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class CustomNestedScrollView extends NestedScrollView {
    public CustomNestedScrollView(@NonNull  Context context) {
        super(context);
    }

    public CustomNestedScrollView(@NonNull  Context context, @Nullable  AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollChangedListener!=null){
            mOnScrollChangedListener.scrollChangedListener(t);
        }
    }

    //滑动屏幕时y轴顶部的距离
  public   interface OnScrollChangedListener{
        void scrollChangedListener(int y);
    }
    private OnScrollChangedListener mOnScrollChangedListener;

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener){
        this.mOnScrollChangedListener=onScrollChangedListener;
    }
}
