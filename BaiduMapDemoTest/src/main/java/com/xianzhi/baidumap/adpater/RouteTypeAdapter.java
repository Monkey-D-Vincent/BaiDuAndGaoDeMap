package com.xianzhi.baidumap.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xianzhi.baidumap.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LiMing
 * @Demo class RouteTypeAdapter
 * @Description TODO
 * @date 2019-10-08 11:24
 */
public class RouteTypeAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;
    private OnRouteTypeClickListener listener;
    private LayoutInflater inflater;

    public RouteTypeAdapter(Context context, List<String> data, OnRouteTypeClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.content_route_type, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvType.setText(data.get(position));
        viewHolder.tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRouteTypeClick(position, data.get(position));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_type)
        TextView tvType;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnRouteTypeClickListener {
        void onRouteTypeClick(int index, String name);
    }
}
