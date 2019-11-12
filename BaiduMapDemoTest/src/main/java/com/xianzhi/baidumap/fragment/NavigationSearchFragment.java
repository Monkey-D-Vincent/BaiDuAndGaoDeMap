package com.xianzhi.baidumap.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRouteResultManager;
import com.baidu.navisdk.adapter.struct.BNRouteDetail;
import com.baidu.navisdk.adapter.struct.BNRoutePlanItem;
import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.adpater.RouteResultAdapter;
import com.xianzhi.baidumap.baidu.tools.DpAndPxUtils;
import com.xianzhi.baidumap.baidu.view.BNRecyclerView;
import com.xianzhi.baidumap.baidu.view.BNScrollLayout;
import com.xianzhi.baidumap.baidu.view.BNScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author liMing
 * @Demo class NavigationSearchFragment
 * @Description TODO
 * @date 2019-10-14 16:38
 */
public class NavigationSearchFragment extends Fragment {

    @BindView(R.id.rl_button)
    RelativeLayout rlButton;
    @BindView(R.id.layout_3tab)
    LinearLayout layout3tab;
    @BindView(R.id.rv_route)
    BNRecyclerView rvRoute;
    @BindView(R.id.content_scroll)
    BNScrollView contentScroll;
    @BindView(R.id.route_0)
    LinearLayout route0;
    @BindView(R.id.route_1)
    LinearLayout route1;
    @BindView(R.id.route_2)
    LinearLayout route2;
    @BindView(R.id.layout_scroll)
    BNScrollLayout layoutScroll;
    Unbinder unbinder;

    private View mRootView;
    private RouteResultAdapter adapter;

    private Context context;
    private List<BNRoutePlanItem> mRoutePlanItems;
    private List<BNRouteDetail> mRouteList = new ArrayList<>();
    private Bundle mRouteDetails = new Bundle();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.frag_navigation_search, null);
        unbinder = ButterKnife.bind(this, mRootView);
        BaiduNaviManagerFactory.getRouteResultSettingManager().setRouteMargin(100, 100, 100, 500);
        //路线点击事件监听
        BaiduNaviManagerFactory.getRouteResultManager().setRouteClickedListener(new IBNRouteResultManager.IRouteClickedListener() {
            @Override
            public void routeClicked(int index) {
                //根据路线索引选择路线
                BaiduNaviManagerFactory.getRouteGuideManager().selectRoute(index);
            }
        });
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        contentScroll.setVerticalScrollBarEnabled(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                layoutScroll.setMaxOffset(layout3tab.getMeasuredHeight() + DpAndPxUtils.dip2px(context, 10));
                layoutScroll.setToOpen();

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rlButton.getLayoutParams();
                layoutParams.bottomMargin = layout3tab.getMeasuredHeight() + DpAndPxUtils.dip2px(context, 10);
                rlButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initData() {
        Bundle bundle = BaiduNaviManagerFactory.getRouteResultManager().getRouteInfo();
        if (bundle == null) {
            return;
        }
        // 3Tab信息
        mRoutePlanItems = bundle.getParcelableArrayList(BNaviCommonParams.BNRouteInfoKey.INFO_TAB);
        // 每条路线的详细信息
        mRouteDetails = bundle.getBundle(BNaviCommonParams.BNRouteInfoKey.INFO_ROUTE_DETAIL);
        // 每条路线的限行信息
//        List<String> mLimitInfos = bundle.getStringArrayList(BNaviCommonParams.BNRouteInfoKey.TRAFFIC_LIMIT_INFO);
//        if (mLimitInfos != null) {
//            for (int i = 0; i < mLimitInfos.size(); i++) {
//                String[] arr = mLimitInfos.get(i).split(",");
//            }
//        }
        if (mRoutePlanItems != null) {
            if (mRoutePlanItems.size() > 0 && mRoutePlanItems.get(0) != null) {
                initTabView(route0, mRoutePlanItems.get(0));
            }

            if (mRoutePlanItems.size() > 1 && mRoutePlanItems.get(1) != null) {
                initTabView(route1, mRoutePlanItems.get(1));
            } else {
                route1.setVisibility(View.GONE);
            }

            if (mRoutePlanItems.size() > 2 && mRoutePlanItems.get(2) != null) {
                initTabView(route2, mRoutePlanItems.get(2));
            } else {
                route2.setVisibility(View.GONE);
            }
        }
        route0.setSelected(true);

        mRouteList.clear();
        mRouteList.addAll(mRouteDetails.<BNRouteDetail>getParcelableArrayList("0"));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvRoute.setLayoutManager(layoutManager);
        adapter = new RouteResultAdapter(mRouteList);
        rvRoute.setAdapter(adapter);
    }

    @OnClick({R.id.btn_fullView, R.id.btn_road, R.id.btn_startNavigation, R.id.route_0, R.id.route_1, R.id.route_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_fullView:
                BaiduNaviManagerFactory.getRouteResultManager().fullView();
                break;
            case R.id.btn_road:
                BaiduNaviManagerFactory.getRouteResultSettingManager().setRealRoadCondition(context);
                break;
            case R.id.btn_startNavigation:
                BaiduNaviManagerFactory.getRouteResultManager().startNavi();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction tx = fm.beginTransaction();
                NavigationStartFragment fragment = new NavigationStartFragment();
                tx.replace(R.id.frag_content, fragment, "DemoGuide");
                tx.addToBackStack(null);
                tx.commit();
                break;
            case R.id.route_0:
                route0.setSelected(true);
                route1.setSelected(false);
                route2.setSelected(false);
                BaiduNaviManagerFactory.getRouteGuideManager().selectRoute(0);
                mRouteList.clear();
                mRouteList.addAll(mRouteDetails.<BNRouteDetail>getParcelableArrayList("0"));
                adapter.notifyDataSetChanged();
                break;
            case R.id.route_1:
                route0.setSelected(false);
                route1.setSelected(true);
                route2.setSelected(false);
                BaiduNaviManagerFactory.getRouteGuideManager().selectRoute(1);
                mRouteList.clear();
                mRouteList.addAll(mRouteDetails.<BNRouteDetail>getParcelableArrayList("1"));
                adapter.notifyDataSetChanged();
                break;
            case R.id.route_2:
                if (mRoutePlanItems.size() < 3) {
                    return;
                }
                route0.setSelected(false);
                route1.setSelected(false);
                route2.setSelected(true);
                BaiduNaviManagerFactory.getRouteGuideManager().selectRoute(2);
                mRouteList.clear();
                mRouteList.addAll(mRouteDetails.<BNRouteDetail>getParcelableArrayList("2"));
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void initTabView(LinearLayout layoutTab, BNRoutePlanItem bnRoutePlanItem) {
        TextView prefer = layoutTab.findViewById(R.id.prefer);
        prefer.setText(bnRoutePlanItem.getPusLabelName());
        TextView time = layoutTab.findViewById(R.id.time);
        time.setText((int) bnRoutePlanItem.getPassTime() / 60 + "分钟");
        TextView distance = layoutTab.findViewById(R.id.distance);
        distance.setText((int) bnRoutePlanItem.getLength() / 1000 + "公里");
        TextView traffic_light = layoutTab.findViewById(R.id.traffic_light);
        traffic_light.setText(String.valueOf(bnRoutePlanItem.getLights()));
    }


    @Override
    public void onResume() {
        super.onResume();
        initView();
        BaiduNaviManagerFactory.getRouteResultManager().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        BaiduNaviManagerFactory.getRouteResultManager().onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaiduNaviManagerFactory.getRouteResultManager().onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
