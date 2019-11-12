package com.xianzhi.map.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.SubPoiItem;
import com.xianzhi.map.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LiMing
 * @Demo class SearchAdapter
 * @Description TODO
 * @date 2019-10-29 11:04
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<PoiItem> data = new ArrayList<>();
    private OnSearchClickListener listener;

    public SearchAdapter(Context context, OnSearchClickListener listener) {
        this.context = context;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.content_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tvKey.setText(data.get(i).getTitle());
        String name = data.get(i).getTypeDes();
        String str[] = name.split(";");
        if (str.length > 2) {
            viewHolder.tvTag.setText(str[str.length - 2] + "    ");
        } else if (str.length > 0) {
            viewHolder.tvTag.setText(str[str.length - 1] + "    ");
        }
        viewHolder.tvDis.setText(!data.get(i).getTitle().contains("公交站") ? data.get(i).getProvinceName() + "-" + data.get(i).getCityName() + "-" + data.get(i).getSnippet() : data.get(i).getSnippet());

        viewHolder.rvSearch.setLayoutManager(new GridLayoutManager(context, 3));
        viewHolder.rvSearch.setAdapter(new SearchDetailAdapter(context, data.get(i).getSubPois(), new SearchDetailAdapter.OnSearchDetailClickListener() {
            @Override
            public void onSearchDetailClick(SubPoiItem info) {
                PoiItem poiItem = new PoiItem(info.getPoiId(), info.getLatLonPoint(), info.getTitle(), info.getSnippet());
                listener.onSearchClick(poiItem);
            }
        }));

        viewHolder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PoiItem poiItem = new PoiItem(data.get(i).getPoiId(), data.get(i).getLatLonPoint(), data.get(i).getTitle(), data.get(i).getSnippet());
                listener.onSearchClick(poiItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<PoiItem> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_key)
        TextView tvKey;
        @BindView(R.id.tv_tag)
        TextView tvTag;
        @BindView(R.id.tv_dis)
        TextView tvDis;
        @BindView(R.id.rv_search)
        RecyclerView rvSearch;
        @BindView(R.id.ll_content)
        LinearLayout llContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnSearchClickListener {
        void onSearchClick(PoiItem bean);
    }
}
