package com.lee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lee.assist.MyHelper;
import com.lee.assist.Position;
import com.lee.words.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/16.
 */
public class ReviewAdapter extends BaseAdapter {
    MyHelper helper;
    List<Position> list;
    Context context;
    public ReviewAdapter(Context context,List<Position> list){
        this.context=context;
        this.list=list;
        helper=new MyHelper(context);
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
        ReviewHolder holder;
        if (convertView == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.review_item,null);
            holder=new ReviewHolder();
            holder.tv_cishu= (TextView) convertView.findViewById(R.id.tv_cishu);
            holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_list= (TextView) convertView.findViewById(R.id.tv_list);
            convertView.setTag(holder);
        }else{
            holder= (ReviewHolder) convertView.getTag();
        }
        holder.tv_list.setText("LIST-"+list.get(position).getList());
        holder.tv_time.setText("上次复习时间："+list.get(position).getReviewTime());
        holder.tv_cishu.setText("复习次数："+list.get(position).getReview_Times());
        return convertView;
    }
    public void close(){
        helper.close();
    }
    class ReviewHolder{
        TextView tv_list,tv_time,tv_cishu;
    }
}
