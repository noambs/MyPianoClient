package com.noam.piano_stern.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.noam.piano_stern.R;

public class BlackLayout extends ViewGroup {


    public BlackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {




        int screenWidth = this.getWidth() ;
        int screenHigh = this.getHeight();
        double whiteKeyWidth = screenWidth / 7.0;
        double childWidth = whiteKeyWidth / 2 ;
        int childHigh = screenHigh / 2;
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);

            if (i == 0 || i == 3 || i == 7 ) {
                child.setVisibility(INVISIBLE);
            }

            int l = (int)(((whiteKeyWidth * (i))) - childWidth / 2);
            int r = (int)(l + childWidth );

            child.layout(l, 0, r, childHigh);
        }
    }

}