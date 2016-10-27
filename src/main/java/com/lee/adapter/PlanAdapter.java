package com.lee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lee.assist.MyHelper;
import com.lee.words.R;
import com.lee.assist.Review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class PlanAdapter extends BaseAdapter {
    private List<Review> list;
    private Context context;
    private MyHelper helper;
    public PlanAdapter(List<Review> list, Context context) {
        this.list = list;
        this.context = context;
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
        PlanHolder holder;
        if (convertView == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.plan_item,null);
            holder=new PlanHolder();
            holder.tv_content= (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_week= (TextView) convertView.findViewById(R.id.tv_week);
            convertView.setTag(holder);
        }else{
            holder= (PlanHolder) convertView.getTag();
        }
        String date=list.get(position).getDate();
        holder.tv_time.setText(date);
        holder.tv_week.setText(getWeek(date));
        String content=helper.getReviewString(list.get(position).getTable(),date);
        holder.tv_content.setText("复习内容: "+content);
        return convertView;
    }
    private String getWeek(String date){
        String week="";
        SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
        Date date1=null;
        try {
            date1=format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date1);
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:
                week="星期日";
                break;
            case 2:
                week="星期一";
                break;
            case 3:
                week="星期二";
                break;
            case 4:
                week="星期三";
                break;
            case 5:
                week="星期四";
                break;
            case 6:
                week="星期五";
                break;
            case 7:
                week="星期六";
                break;
        }
        return week;
    }
    public void close(){
        helper.close();
    }
    class PlanHolder{
        TextView tv_time;
        TextView tv_content;
        TextView tv_week;
    }
}
