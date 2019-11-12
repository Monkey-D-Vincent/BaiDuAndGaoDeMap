package com.xianzhi.baidumap.adpater;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.tools.NumberUtils;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LiMing
 * @Demo class RoutePlanningAdapter
 * @Description TODO
 * @date 2019-10-09 14:04
 */
public class RoutePlanningAdapter extends BaseAdapter {

    private Context context;
    private List<DrivingRouteLine> data;
    private LayoutInflater inflater;

    public RoutePlanningAdapter(Context context, List<DrivingRouteLine> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.content_route_planning, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvLight.setText(data.get(position).getLightNum() + "");
        int time = data.get(position).getDuration();
        viewHolder.tvTime.setText(time / 3600 == 0 ? time / 60 + "分钟" : time / 3600 + "小时" + (time % 3600) / 60 + "分钟");
        viewHolder.tvDrivingDis.setText(getDistance(data.get(position).getDistance()));
        viewHolder.tvType.setText("方案" + NumberUtils.cvt(position + 1));
//        viewHolder.tvCongestionDis.setText(getDistance(data.get(position).getCongestionDistance()));

        viewHolder.tvType.setTextColor(position == 0 ? context.getResources().getColor(R.color.text_blue) : Color.BLACK);
        viewHolder.tvDrivingDis.setTextColor(position == 0 ? context.getResources().getColor(R.color.text_blue) : Color.BLACK);
        viewHolder.tvTime.setTextColor(position == 0 ? context.getResources().getColor(R.color.text_blue) : Color.BLACK);
        viewHolder.tvLight.setTextColor(position == 0 ? context.getResources().getColor(R.color.text_blue) : Color.BLACK);

        return convertView;
    }

    /**
     * 格式化距离
     * @param distance
     * @return
     */
    private String getDistance(int distance) {
        if(distance >= 1000 && distance < 10000) {
            return (new DecimalFormat("#.00").format((double) distance / 1000)) + "公里";
        } else if(distance >= 10000) {
            return distance / 1000 + "公里";
        }
        return distance + "米";
    }

    static class ViewHolder {
        @BindView(R.id.tv_light)
        TextView tvLight;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_drivingDis)
        TextView tvDrivingDis;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
