package com.xianzhi.baidumap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author LiMing
 * @Demo class MyGridView
 * @Description TODO
 * @date 2019-10-08 15:02
 */
public class MyGridView extends GridView {

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
