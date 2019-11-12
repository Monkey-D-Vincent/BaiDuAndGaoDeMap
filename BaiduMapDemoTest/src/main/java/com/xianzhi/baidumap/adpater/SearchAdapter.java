package com.xianzhi.baidumap.adpater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiChildrenInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.xianzhi.baidumap.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LiMing
 * @Demo class SearchAdapter
 * @Description TODO
 * @date 2019-09-25 15:06
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<PoiInfo> data = new ArrayList<>();
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.tvDis.setText(data.get(i).getCity() + "-" + data.get(i).getAddress());
        String name = data.get(i).getName();
        if (data.get(i).getPoiDetailInfo() != null) {
            String str[] = data.get(i).getPoiDetailInfo().getTag().split(";");
            if (str.length > 0) {
                if ((str[str.length - 1].equals("公交车站") || str[str.length - 1].equals("地铁站")) && !name.contains("公交车站") && !name.contains("地铁站")) {
                    name = data.get(i).getName() + "-" + str[str.length - 1];
                } else {
                    viewHolder.tvTag.setText(str[str.length - 1] + "    ");
                }
            }
        }

        data.get(i).setName("");
        data.get(i).setName(name);
        viewHolder.tvKey.setText(data.get(i).getName());

        viewHolder.rvSearch.setVisibility(data.get(i).getPoiDetailInfo() == null || data.get(i).getPoiDetailInfo().getPoiChildrenInfoList() == null || data.get(i).getPoiDetailInfo().getPoiChildrenInfoList().isEmpty() ? View.GONE : View.VISIBLE);
        if (null != data.get(i).getPoiDetailInfo() && data.get(i).getPoiDetailInfo().getPoiChildrenInfoList() != null) {
            SearchDetailAdapter adapter = new SearchDetailAdapter(context, data.get(i).getPoiDetailInfo().getPoiChildrenInfoList(), new SearchDetailAdapter.OnSearchDetailClickListener() {
                @Override
                public void onSearchDetailClick(PoiChildrenInfo info) {
                    PoiInfo bean = new PoiInfo();
                    bean.setName(data.get(i).getName() + "-" + info.getShowName());
                    bean.setLocation(info.getLocation());
                    bean.setCity(data.get(i).getCity());
                    listener.onSearchClick(bean);
                }
            });
            viewHolder.rvSearch.setLayoutManager(new GridLayoutManager(context, 3));
            viewHolder.rvSearch.setAdapter(adapter);
        }
        viewHolder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSearchClick(data.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<PoiInfo> data) {
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
        void onSearchClick(PoiInfo bean);
    }
}
