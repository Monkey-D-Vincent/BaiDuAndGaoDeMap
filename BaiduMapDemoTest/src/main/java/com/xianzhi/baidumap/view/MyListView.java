package com.xianzhi.baidumap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author LiMing
 * @Demo class MyListView
 * @Description TODO
 * @date 2019-09-26 09:36
 */
public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
