package com.xianzhi.map.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.poisearch.SubPoiItem;
import com.xianzhi.map.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LiMing
 * @Demo class SearchDetailAdapter
 * @Description TODO
 * @date 2019-09-25 17:08
 */
public class SearchDetailAdapter extends RecyclerView.Adapter<SearchDetailAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<SubPoiItem> data;
    private OnSearchDetailClickListener listener;

    public SearchDetailAdapter(Context context, List<SubPoiItem> data, OnSearchDetailClickListener listener) {
        this.data = data;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View convertView = inflater.inflate(R.layout.content_search_detail, null);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.tvPlace.setText(data.get(i).getSubName());
        viewHolder.tvPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSearchDetailClick(data.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_place)
        TextView tvPlace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnSearchDetailClickListener {
        void onSearchDetailClick(SubPoiItem info);
    }
}
