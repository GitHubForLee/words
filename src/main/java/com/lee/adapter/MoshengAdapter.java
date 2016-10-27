package com.lee.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lee.words.R;
import com.lee.assist.Words;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class MoshengAdapter extends BaseAdapter {
    private List<Words> list;
    private Context context;

    public MoshengAdapter(List<Words> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("getview",position+"");
        Holder holder;
        if (convertView == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.mosheng_item,null);
            holder=new Holder();
            holder.tv_mean= (TextView) convertView.findViewById(R.id.tv_mean);
            holder.tv_spelling= (TextView) convertView.findViewById(R.id.tv_spelling);
            holder.tv_number= (TextView) convertView.findViewById(R.id.tv_number);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.tv_number.setText((position+1)+"");
        holder.tv_spelling.setText(list.get(position).getSpelling());
        holder.tv_mean.setText(list.get(position).getMeanning());
        return convertView;
    }
    class Holder{
        private TextView tv_number;
        private TextView tv_spelling;
        private TextView tv_mean;
    }
}
