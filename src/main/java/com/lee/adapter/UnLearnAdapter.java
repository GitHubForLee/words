package com.lee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lee.words.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class UnLearnAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    public UnLearnAdapter(List<String> list,Context context){
        this.list=list;
        this.context=context;
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
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.unlearn_item,null);
            holder=new ViewHolder();
            holder.textView= (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.textView.setText((String) getItem(position));
        return convertView;
    }
    class ViewHolder{
        TextView textView;
    }
}
