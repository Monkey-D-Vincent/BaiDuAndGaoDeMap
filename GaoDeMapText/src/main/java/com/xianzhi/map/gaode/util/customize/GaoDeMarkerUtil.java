package com.xianzhi.map.gaode.util.customize;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.xianzhi.map.gaode.bean.GaoDeMarkerBean;
import com.xianzhi.map.listener.GaoDeOnClickListener;

/**
 * @author LiMing
 * @Demo class GaoDeMarkerUtil
 * @Description TODO 绘制 Marker
 * @date 2019-10-31 14:57
 */
public class GaoDeMarkerUtil {

    private static GaoDeMarkerUtil util;
    private static MarkerOptions options;
    private static AMap mAMap;

    private Marker marker;

    private GaoDeMarkerUtil() {

    }

    public static GaoDeMarkerUtil getInstance(AMap aMap) {
        mAMap = aMap;
        options = new MarkerOptions();
        if (util == null) {
            util = new GaoDeMarkerUtil();
        }
        return util;
    }

    /**
     * 在地图上标记位置的经纬度值。必填参数
     *
     * @param latLng
     * @return {@link MarkerOptions#position(LatLng)}
     */
    public GaoDeMarkerUtil setPosition(LatLng latLng) {
        options.position(latLng);
        return util;
    }

    /**
     * 点标记的标题
     *
     * @param title
     * @return {@link MarkerOptions#title(String)}
     */
    public GaoDeMarkerUtil setTitle(String title) {
        options.title(title);
        return util;
    }

    /**
     * 点标记的内容
     *
     * @param draggable
     * @return {@link MarkerOptions#draggable(boolean)}
     */
    public GaoDeMarkerUtil setDraggable(boolean draggable) {
        options.draggable(draggable);
        return util;
    }

    /**
     * 点标记的内容
     *
     * @param content
     * @return {@link MarkerOptions#snippet(String)}
     */
    public GaoDeMarkerUtil setSnippet(String content) {
        options.snippet(content);
        return util;
    }

    /**
     * 点标记的内容
     *
     * @param visible
     * @return {@link MarkerOptions#visible(boolean)}
     */
    public GaoDeMarkerUtil setVisiblet(boolean visible) {
        options.visible(visible);
        return util;
    }

    /**
     * 点标记的内容
     *
     * @param hor 横向
     * @param ver 竖向
     * @return {@link MarkerOptions#anchor(float, float)}
     */
    public GaoDeMarkerUtil setAnchor(float hor, float ver) {
        options.anchor(hor, ver);
        return util;
    }

    /**
     * 点标记的内容
     *
     * @param alpha
     * @return {@link MarkerOptions#alpha(float)}
     */
    public GaoDeMarkerUtil setAlpha(float alpha) {
        options.alpha(alpha);
        return util;
    }

    /**
     * 设置marker平贴地图效果
     *
     * @param flat
     * @return {@link MarkerOptions#setFlat(boolean)}
     */
    public GaoDeMarkerUtil setFlat(boolean flat) {
        options.setFlat(flat);
        return util;
    }

    /**
     * 设置 Marker 的 icon
     *
     * @param icon
     * @return {@link MarkerOptions#icon(BitmapDescriptor)}
     */
    public GaoDeMarkerUtil setIcon(Context context, int icon) {
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), icon)));
        return util;
    }

    /**
     * 自定义 Marker
     * 在地图上标记位置的经纬度值。必填参数 必须先调用 请调用{@link GaoDeMarkerUtil#setPosition(LatLng)}
     * 点标记的标题 请先调用{@link GaoDeMarkerUtil#setTitle(String)}
     * 点标记的内容 请先调用{@link GaoDeMarkerUtil#setSnippet(String)}
     * 点标记是否可拖拽 请先调用{@link GaoDeMarkerUtil#setDraggable(boolean)}
     * 点标记是否可见 请先调用{@link GaoDeMarkerUtil#setVisiblet(boolean)}
     * 点标记的锚点 请先调用{@link GaoDeMarkerUtil#setAnchor(float, float)}
     * 点标记的透明度 请先调用{@link GaoDeMarkerUtil#setAlpha(float)}
     * 点标记的图标 请先调用{@link GaoDeMarkerUtil#setIcon(Context, int)}
     * 点标记贴地显示 请先调用{@link GaoDeMarkerUtil#setFlat(boolean)}
     * 点标记的动画 请之后调用{@link GaoDeMarkerUtil#setAnimation(Animation)}
     * 点标记的点击事件 请之后调用{@link GaoDeMarkerUtil#setOnMarkerClickListener(GaoDeOnClickListener.OnMarkerClickListener)} ()}
     * 点标记的拖拽事件 请先允许拖拽 之后调用{@link GaoDeMarkerUtil#setOnMarkerDragListener(GaoDeOnClickListener.OnMarkerDraggablegClickListener)} ()}
     * @see GaoDeMarkerBean
     */
    public void customizeMarker() {
        marker = mAMap.addMarker(options);
    }

    /**
     * 设置动画
     *
     * @param animation 一定是高德的 Animation 类
     */
    public void setAnimation(Animation animation) {
//        animation = new RotateAnimation(marker.getRotateAngle(),marker.getRotateAngle()+180,0,0,0);
//        long duration = 1000L;
//        animation.setDuration(duration);
//        animation.setInterpolator(new LinearInterpolator());

        marker.setAnimation(animation);
        marker.startAnimation();
    }

    /**
     * 绑定 Marker 被点击事件
     */
    public void setOnMarkerClickListener(final GaoDeOnClickListener.OnMarkerClickListener listener) {
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                listener.onMarkerClick(marker);
                return false;
            }
        });
    }

    /**
     * 绑定 Marker 拖拽事件
     */
    private void setOnMarkerDragListener(final GaoDeOnClickListener.OnMarkerDraggablegClickListener listener) {
        mAMap.setOnMarkerDragListener(new AMap.OnMarkerDragListener() {

            /**
             * 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
             * 这个位置可能与拖动的之前的marker位置不一样。
             * marker 被拖动的marker对象。
             * @param marker
             */
            @Override
            public void onMarkerDragStart(Marker marker) {
                listener.onMarkerDragStart(marker);
            }

            /**
             * 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
             * 这个位置可能与拖动的之前的marker位置不一样。
             * marker 被拖动的marker对象。
             * @param marker
             */
            @Override
            public void onMarkerDragEnd(Marker marker) {
                listener.onMarkerDragEnd(marker);
            }

            /**
             * 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
             * 这个位置可能与拖动的之前的marker位置不一样。
             * marker 被拖动的marker对象。
             * @param marker
             */
            @Override
            public void onMarkerDrag(Marker marker) {
                listener.onMarkerDrag(marker);
            }
        });
    }
}
