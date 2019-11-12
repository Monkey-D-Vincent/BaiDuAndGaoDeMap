package com.xianzhi.baidumap.baidu.tools;

import android.content.Context;

/**
 * @author liMing
 * @Demo class DpAndPxUtils
 * @Description TODO
 * @date 2019-10-14 16:51
 */
public class DpAndPxUtils {

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
}
