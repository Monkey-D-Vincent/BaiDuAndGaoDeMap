package com.xianzhi.map.gaode.util.customize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.xianzhi.map.listener.GaoDeOnClickListener;

/**
 * @author LiMing
 * @Demo class GaoDeInfoWindowUtil
 * @Description TODO 自定义 InfoWindow
 * @date 2019-10-31 15:42
 */
public class GaoDeInfoWindowUtil implements AMap.InfoWindowAdapter {

    private static GaoDeInfoWindowUtil util;
    private static LayoutInflater inflate;
    private static int mLayout;

    private View infoWindow = null;
    private GaoDeOnClickListener.OnRenderInfoWindowClickListener listener;

    private static AMap mAMap;

    private GaoDeInfoWindowUtil() {
        mAMap.setInfoWindowAdapter(this);
    }

    public static GaoDeInfoWindowUtil getInstance(Context context, AMap aMap, int layout, int type) {
        mAMap = aMap;
        mLayout = layout;
        inflate = LayoutInflater.from(context);
        if (util == null) {
            util = new GaoDeInfoWindowUtil();
        }
        return util;
    }

    /**
     * 当实现此方法并返回有效值时（返回值不为空，则视为有效）,SDK 将不会使用默认的样式，而采用此方法返回的样式（即 View）。默认会将Marker 的 title 和 snippet 显示到 InfoWindow 中。
     * 如果此时修改了 Marker 的 title 或者 snippet 内容，再次调用类 Marker 的 showInfoWindow() 方法，InfoWindow 内容不会更新。自定义 InfoWindow 之后所有的内容更新都需要用户自己完成。
     * 当调用 Marker 类的 showInfoWindow() 方法时，SDK 会调用 getInfoWindow（Marker marker） 方法和 getInfoContents(Marker marker) 方法（之后会提到），在这些方法中更新 InfoWindow 的内容即可。
     * 注意：如果此方法返回的 View 没有设置 InfoWindow 背景图，SDK 会默认添加一个背景图。
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     * 此方法和 getInfoWindow（Marker marker） 方法的实质是一样的，唯一的区别是：此方法不能修改整个 InfoWindow 的背景和边框，无论自定义的样式是什么样，SDK 都会在最外层添加一个默认的边框。
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {
        if (infoWindow == null) {
            infoWindow = inflate.inflate(mLayout, null);
        }
        render(marker, infoWindow);
        return infoWindow;
    }

    public void render(Marker marker, View view) {
        //如果想修改自定义Infow中内容，请通过view找到它并修改
        listener.onRenderInfoWindowClick(marker, view);
    }

    /**
     * 自定义infowinfow窗口
     *
     * @see <a href="https://blog.csdn.net/yfloctar/article/details/50474691">自己看吧 它已经写完了 按住 Ctrl 直接点左键就可以了</a>
     */
    public GaoDeInfoWindowUtil setInfoWindowListener(GaoDeOnClickListener.OnRenderInfoWindowClickListener listener) {
        this.listener = listener;
        return util;
    }

    /**
     * 绑定信息窗点击事件
     * {@link AMap#setOnInfoWindowClickListener(AMap.OnInfoWindowClickListener)}
     */
    public void setOnInfoWindowClickListener(final GaoDeOnClickListener.OnInfoWindowClickListener listener) {
        mAMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                listener.onInfoWindowClick(marker);
            }
        });
    }
}
