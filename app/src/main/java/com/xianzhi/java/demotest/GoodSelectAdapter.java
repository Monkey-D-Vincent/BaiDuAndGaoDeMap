package com.xianzhi.java.demotest;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LiMing
 * @Demo class GoodSelectAdapter
 * @Description TODO
 * @date 2019/9/17 上午10:39
 */

public class GoodSelectAdapter extends BaseAdapter {

    private Context context;
    private List<DailyWorkValuesBean.QuestionListBean> data;
    private int index = 1;
    private OnTextClickListener listener;
    private Map<String, String> itemData = new HashMap<>();

    public GoodSelectAdapter(Context context, List<DailyWorkValuesBean.QuestionListBean> data, OnTextClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
        itemData.put("你今天是否在用“诚信问题零容忍”来要求自己？", "A.完全达到。");
        itemData.put("你今天工作任务的完成，是否做到“追求极致”？", "E.部分达到。");
        itemData.put("你今天接到领导交办的工作任务，是否做到“使命必达”？", "J.没有达到。");
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
        index++;
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.content_good, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (data != null) {
            viewHolder.tvTitle.setText(data.get(position).getDesc());

            if (viewHolder.vgPropContents.getChildCount() == 0) {
                RadioButton[] textViews = new RadioButton[data.get(position).getAnswerList().size()];
                for (int i = 0; i < data.get(position).getAnswerList().size(); i++) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(5, 5, 5, 5);
                    RadioButton textView = new RadioButton(context);
                    textView.setGravity(Gravity.CENTER_VERTICAL);
                    textView.setPadding(5, 5, 5, 5);
                    textView.setLayoutParams(params);
                    textViews[i] = textView;
                    textViews[i].setButtonDrawable(R.drawable.checkbox_check_off_anothor);
                    textViews[i].setBackgroundResource(R.drawable.translute);
                    textViews[i].setTextColor(Color.parseColor("#666666"));
                    textViews[i].setText(data.get(position).getAnswerList().get(i).getDesc());
                    textViews[i].setTag(i);
                    viewHolder.vgPropContents.addView(textViews[i]);
                }
                for (int j = 0; j < textViews.length; j++) {
                    textViews[j].setTag(textViews);
                    textViews[j].setOnClickListener(new LableClickListener(data.get(position)));
                }
                if(itemData.get(data.get(position).getDesc()) != null) {
                    /**判断之前是否已选中标签*/
                    for (int h = 0; h < viewHolder.vgPropContents.getChildCount(); h++) {
                        RadioButton v = (RadioButton) viewHolder.vgPropContents.getChildAt(h);
                        if (itemData.get(data.get(position).getDesc()).equals(v.getText().toString())) {
                            v.setBackgroundResource(R.drawable.pink_check);
                            v.setTextColor(Color.parseColor("#a92c2b"));
                            v.setButtonDrawable(R.drawable.checkbox_check_on_anothor);
                            itemData.put(data.get(position).getDesc(), v.getText().toString());
                        }
                    }
                }
            }
        }

        return convertView;
    }

    class LableClickListener implements View.OnClickListener {

        private DailyWorkValuesBean.QuestionListBean bean;

        public LableClickListener(DailyWorkValuesBean.QuestionListBean bean) {
            this.bean = bean;
        }

        @Override
        public void onClick(View v) {
            RadioButton[] textViews = (RadioButton[]) v.getTag();
            RadioButton tv = (RadioButton) v;
            for (int i = 0; i < textViews.length; i++) {
                //让点击的标签背景变成橙色，字体颜色变为白色
                if (tv.equals(textViews[i])) {
                    textViews[i].setBackgroundResource(R.drawable.pink_check);
                    textViews[i].setTextColor(Color.parseColor("#a92c2b"));
                    itemData.put(bean.getDesc(), textViews[i].getText().toString());
                    listener.onClick(bean.getDesc(), textViews[i].getText().toString());
                    textViews[i].setButtonDrawable(R.drawable.checkbox_check_on_anothor);
                } else {
                    //其他标签背景变成白色，字体颜色为黑色
                    textViews[i].setBackgroundResource(R.drawable.translute);
                    textViews[i].setTextColor(Color.parseColor("#666666"));
                    textViews[i].setButtonDrawable(R.drawable.checkbox_check_off_anothor);
                }
            }
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        TextView tvTitle;
        MyViewGroup vgPropContents;

        public ViewHolder(View view) {
            tvTitle = view.findViewById(R.id.tv_title);
            vgPropContents = view.findViewById(R.id.vgPropContents);
        }
    }

    public interface OnTextClickListener {
        void onClick(String title, String text);
    }
}
