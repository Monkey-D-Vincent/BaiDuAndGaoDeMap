package com.xianzhi.baidumap.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.adpater.RouteTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiMing
 * @Demo class RouteTypePopup
 * @Description TODO
 * @date 2019-10-08 11:14
 */
public class RouteTypePopup extends PopupWindow {

    /**
     *
     * @param context
     * @param listener
     * @param type 0 路线规划   1 导航
     */
    public RouteTypePopup(final Activity context, final OnRouteTypeClickListener listener, int type) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_routetype_popup, null);
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);

        List<String> routeType = new ArrayList<>();
        /**路线类型*/
        if(type == 0) {
            routeType.add("时间优先");
            routeType.add("躲避拥堵");
            routeType.add("最短距离");
            routeType.add("较少费用");
        } else {
            routeType.add("智能推荐");
            routeType.add("时间最短");
            routeType.add("少收费");
            routeType.add("躲避拥堵");
            routeType.add("不走高速");
            routeType.add("高速优先");
            routeType.add("距离最短");
        }

        RouteTypeAdapter adapter = new RouteTypeAdapter(context, routeType, new RouteTypeAdapter.OnRouteTypeClickListener() {
            @Override
            public void onRouteTypeClick(int index, String name) {
                listener.onRouteTypeClick(index, name);
            }
        });
        ListView lvTag = view.findViewById(R.id.lv_tag);
        lvTag.setAdapter(adapter);

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.myDialog);
    }

    public interface OnRouteTypeClickListener {
        void onRouteTypeClick(int index, String name);
    }

}
