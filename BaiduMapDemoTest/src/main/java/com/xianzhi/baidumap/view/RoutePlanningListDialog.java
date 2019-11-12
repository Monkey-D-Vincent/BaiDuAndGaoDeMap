package com.xianzhi.baidumap.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.adpater.RoutePlanningAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LiMing
 * @Demo class RoutePlanningListDialog
 * @Description TODO
 * @date 2019-10-10 16:10
 */
public class RoutePlanningListDialog extends Dialog {

    @BindView(R.id.gv_route)
    GridView gvRoute;

    private List<DrivingRouteLine> data;
    private Context context;
    private OnRouteClickListener listener;

    public RoutePlanningListDialog(@NonNull Context context) {
        super(context);
    }

    public RoutePlanningListDialog(@NonNull Context context, int themeResId, List<DrivingRouteLine> data, OnRouteClickListener listener) {
        super(context, themeResId);
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    protected RoutePlanningListDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_routeplanning_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        RoutePlanningAdapter adapter = new RoutePlanningAdapter(context, data);
        gvRoute.setAdapter(adapter);
        gvRoute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                listener.onRouteClick(data.get(position));
            }
        });
    }

    public interface OnRouteClickListener {
        void onRouteClick(DrivingRouteLine bean);
    }
}
