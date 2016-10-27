package com.lee.adapter;

import android.content.Context;
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
 * Created by Administrator on 2016/9/17.
 */
public class TestAdapter extends BaseAdapter {
    private List<Position> list;
    private Context context;
    private MyHelper helper;
    public TestAdapter(Context context,List<Position> list){
        this.list=list;
        helper=new MyHelper(context);
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
        TestHoder hoder;
        if (convertView == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.test_item,null);
            hoder=new TestHoder();
            hoder.ratingBar= (RatingBar) convertView.findViewById(R.id.ratingBar);
            hoder.tv_list= (TextView) convertView.findViewById(R.id.tv_list);
            hoder.tv_score= (TextView) convertView.findViewById(R.id.tv_score);
            convertView.setTag(hoder);
        }else{
            hoder= (TestHoder) convertView.getTag();
        }
        Position p=list.get(position);
        String bestScore=helper.getBestScore(p);
        hoder.ratingBar.setRating(bestScore.isEmpty()?0f:1.0f);
        hoder.tv_score.setText(bestScore.isEmpty()?"最高正确率：0%":"最高正确率："+bestScore+"%");
        hoder.tv_list.setText("List"+p.getList());
        return convertView;
    }
    public void close(){
        helper.close();
    }
    class TestHoder{
        RatingBar ratingBar;
        TextView tv_list,tv_score;
    }
}
