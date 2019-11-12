package com.xianzhi.map.activity.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.xianzhi.map.R;
import com.xianzhi.map.adapter.SearchAdapter;
import com.xianzhi.map.base.BaseActivity;
import com.xianzhi.map.gaode.GaoDeMapManager;
import com.xianzhi.map.gaode.bean.SearchPlaceBean;
import com.xianzhi.map.listener.GaoDeOnClickListener;
import com.xianzhi.map.view.RecycleViewDivider;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LiMing
 * @Demo class NavigationActivity
 * @Description TODO 导航
 * @date 2019-11-01 14:15
 */
public class NavigationActivity extends BaseActivity implements INaviInfoCallback, GaoDeOnClickListener.OnSearchActivityClickListener, SearchAdapter.OnSearchClickListener {

    @BindView(R.id.et_start)
    EditText etStart;
    @BindView(R.id.et_end)
    EditText etEnd;
    @BindView(R.id.mMapView)
    MapView mMapView;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.rv_address)
    RecyclerView rvAddress;

    private SearchAdapter adapter;
    private String city;
    private int position = 0;

    private GaoDeMapManager manager;
    private PoiItem startBean;
    private PoiItem endBean;

    @Override
    protected void onCreateView() {
        setContentView(R.layout.activity_navigation_controller);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        rvAddress.setLayoutManager(new LinearLayoutManager(context));
        rvAddress.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 3, getResources().getColor(R.color.white)));
        adapter = new SearchAdapter(context, this);
        rvAddress.setAdapter(adapter);

        manager = GaoDeMapManager.getInstance(context, mMapView);
        manager.onCreate(savedInstanceState);
        manager.setLocation(new GaoDeOnClickListener.OnLocationActivityClickListener() {
            @Override
            public void onLocationClick(RegeocodeResult regeocodeResult) {
                city = regeocodeResult.getRegeocodeAddress().getCity();
                if (regeocodeResult.getRegeocodeAddress() != null && regeocodeResult.getRegeocodeAddress().getPois() != null && !regeocodeResult.getRegeocodeAddress().getPois().isEmpty()) {
                    position = 1;
                    etStart.setText(regeocodeResult.getRegeocodeAddress().getPois().get(0).getTitle());
                    startBean = new PoiItem(regeocodeResult.getRegeocodeAddress().getPois().get(0).getPoiId(), regeocodeResult.getRegeocodeAddress().getPois().get(0).getLatLonPoint(), regeocodeResult.getRegeocodeAddress().getPois().get(0).getTitle(), regeocodeResult.getRegeocodeAddress().getPois().get(0).getSnippet());
                }
            }
        });
        setEditTextListener();
    }

    /**
     * 开始导航  不要怀疑 就是这么一点代码  其他的都是内部处理的
     */
    private void startNavigation() {
        LatLng startLatLng = new LatLng(startBean.getLatLonPoint().getLatitude(), startBean.getLatLonPoint().getLongitude());
        LatLng endLatLng = new LatLng(endBean.getLatLonPoint().getLatitude(), endBean.getLatLonPoint().getLongitude());
        //第三个参数兴趣 ID
        AmapNaviParams params = new AmapNaviParams(new Poi(startBean.getTitle(), startLatLng, ""), null, new Poi(endBean.getTitle(), endLatLng, ""), AmapNaviType.DRIVER);
        params.setUseInnerVoice(true);
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, this);
    }

    private void setEditTextListener() {
        etStart.setFocusable(false);
        etStart.setFocusableInTouchMode(false);
        etEnd.setFocusable(true);
        etEnd.setFocusableInTouchMode(true);
        etStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tvMsg.getVisibility() == View.VISIBLE) {
                    tvMsg.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(s) && position != 1) {
                    manager.searchPlace(getApplicationContext(), s.toString(), city, false, NavigationActivity.this);
                } else {
                    rvAddress.setVisibility(View.GONE);
                }
                position = 2;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tvMsg.getVisibility() == View.VISIBLE) {
                    tvMsg.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(s)) {
                    manager.searchPlace(getApplicationContext(), s.toString(), city, false, NavigationActivity.this);
                } else {
                    rvAddress.setVisibility(View.GONE);
                }
                position = 3;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.et_start, R.id.et_end, R.id.btn_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_start:
                etStart.setFocusable(true);
                etStart.setFocusableInTouchMode(true);
                break;
            case R.id.et_end:
                break;
            case R.id.btn_start:
                if (startBean == null) {
                    Toast.makeText(context, "请输入出发点", Toast.LENGTH_SHORT).show();
                } else if (endBean == null) {
                    Toast.makeText(context, "请输入目的地", Toast.LENGTH_SHORT).show();
                } else {
                    startNavigation();
                }
                break;
        }
    }

    /**
     * 查询结果
     *
     * @param bean
     */
    @Override
    public void onSearchClick(SearchPlaceBean bean) {
        adapter.addData(bean.getPlaces());
        rvAddress.setVisibility(View.VISIBLE);
    }

    /**
     * item 点击事件
     *
     * @param bean
     */
    @Override
    public void onSearchClick(PoiItem bean) {
        if (bean != null) {
            if (position < 3 && position > 0) {
                startBean = bean;
                etStart.setText(bean.getTitle());
            } else if (position == 3) {
                etEnd.setText(bean.getTitle());
                endBean = bean;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        manager.onSaveInstanceState(outState);
    }

    /**
     * 导航初始化失败时的回调函数。
     */
    @Override
    public void onInitNaviFailure() {

    }

    /**
     * 导航播报信息回调函数。
     *
     * @param s
     */
    @Override
    public void onGetNavigationText(String s) {

    }

    /**
     * 当GPS位置有更新时的回调函数。
     *
     * @param aMapNaviLocation
     */
    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        //获取时间  角度  速度 上传服务器
        Log.i("aaaaaaaaaaaaaaaaaaaaaaaaaaa", aMapNaviLocation.toString());
    }

    /**
     * 到达目的地后回调函数。
     *
     * @param b
     */
    @Override
    public void onArriveDestination(boolean b) {
        //结束录制视频服务
    }

    /**
     * 启动导航后的回调函数
     *
     * @param i
     */
    @Override
    public void onStartNavi(int i) {
        //开始录制视频
    }

    /**
     * 算路成功回调
     *
     * @param ints
     */
    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    /**
     * 驾车路径规划失败后的回调函数。
     *
     * @param i
     */
    @Override
    public void onCalculateRouteFailure(int i) {

    }

    /**
     * 已过时。
     */
    @Override
    public void onStopSpeaking() {

    }

    /**
     * 重新规划的回调
     *
     * @param i
     */
    @Override
    public void onReCalculateRoute(int i) {

    }

    /**
     * 退出组件或退出组件导航的回调函数
     *
     * @param i
     */
    @Override
    public void onExitPage(int i) {
        //停止录制视频服务
    }

    /**
     * 切换算路偏好回调
     *
     * @param i
     */
    @Override
    public void onStrategyChanged(int i) {

    }

    /**
     * 获取导航地图自定义View，该View在导航整体界面的下面，注意要设置setLayoutParams并且设置高度
     *
     * @return
     */
    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

    /**
     * 获取导航地图自定义View,该View在导航界面的当前路名位置，使用该方法以后，将不会显示当前路名
     *
     * @return
     */
    @Override
    public View getCustomNaviView() {
        return null;
    }

    /**
     * 驾车路径导航到达某个途经点的回调函数。
     *
     * @param i
     */
    @Override
    public void onArrivedWayPoint(int i) {

    }

    /**
     * 组件地图白天黑夜模式切换回调
     *
     * @param i
     */
    @Override
    public void onMapTypeChanged(int i) {

    }

    /**
     * 获取导航地图自定义View,该View在导航界面的垂直居中，水平靠左位置
     *
     * @return
     */
    @Override
    public View getCustomMiddleView() {
        return null;
    }

}
