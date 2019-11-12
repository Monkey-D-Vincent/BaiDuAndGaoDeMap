package com.xianzhi.baidumap.adpater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.navisdk.adapter.struct.BNRouteDetail;
import com.xianzhi.baidumap.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author liMing
 * @Demo class RouteResultAdapter
 * @Description TODO
 * @date 2019-10-14 17:31
 */
public class RouteResultAdapter extends RecyclerView.Adapter<RouteResultAdapter.ViewHolder> {

    private List<BNRouteDetail> mRouteDetails;

    public RouteResultAdapter(List<BNRouteDetail> routeList) {
        mRouteDetails = routeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_route_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivIcon.setImageResource(mRouteDetails.get(position).getIconId());
        holder.tvTitle.setText(mRouteDetails.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mRouteDetails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
