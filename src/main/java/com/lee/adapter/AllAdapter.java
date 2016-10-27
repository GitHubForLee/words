package com.lee.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lee.assist.MyHelper;
import com.lee.assist.Position;
import com.lee.words.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class AllAdapter extends BaseAdapter {
    public static final String STUDY_TYPE="0";
    public static final String REVIEW_TYPE="1";
    private List<Position> list;
    private Context context;
    private MyHelper helper;
    private String type;
    public AllAdapter(List<Position> list,Context context,String type){
        this.list=list;
        this.type=type;
        this.context=context;
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
        Log.e("reflush","1");
        MyHolder holder;
        if (convertView == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.all_item,null);
            holder=new MyHolder();
            holder.ratingBar= (RatingBar) convertView.findViewById(R.id.ratingBar);
            holder.textView= (TextView) convertView.findViewById(R.id.textView);
            holder.tv_learned= (TextView) convertView.findViewById(R.id.tv_learned);
            convertView.setTag(holder);
        }else{
            holder= (MyHolder) convertView.getTag();
        }
        Position p=list.get(position);
        holder.textView.setText("LIST-" + p.getList());
        boolean isLearned=helper.isLearned(p);
        if(type==STUDY_TYPE){
            holder.ratingBar.setRating(isLearned? 1.0f : 0f);
            holder.tv_learned.setText(isLearned?"已学完":"未学完");
        }else{
            holder.ratingBar.setRating(p.isNeedReviewed()? 1.0f : 0f);
            if(p.isFinishReview())
                holder.tv_learned.setText("复习完成");
            else if(!p.islearned())
                holder.tv_learned.setText("未学完");
            else if(p.isNeedReviewed()){
                holder.tv_learned.setText("该复习了");
            }else{
                holder.tv_learned.setText("暂不需要复习");
            }
        }
        return convertView;
    }
    public void close(){
        helper.close();
    }
    class MyHolder{
        RatingBar ratingBar;
        TextView textView;
        TextView tv_learned;
    }
}
