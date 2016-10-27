package com.lee.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.lee.activity.ReviewActivity_2;
import com.lee.adapter.PlanAdapter;
import com.lee.assist.Review;
import com.lee.words.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/16.
 */
public class PlanFragment extends Fragment implements View.OnClickListener {
    private Button bt_back,bt_next;
    private ListView listView;
    private View view;
    private Calendar calendar;
    private SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
    private DecimalFormat decimalFormat=new DecimalFormat("00");
    private List<Review> list;
    private PlanAdapter adapter;
    private boolean isFirst;
    private String table;

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        table=getActivity().getIntent().getStringExtra("table");
        isFirst=true;
        view = inflater.inflate(R.layout.fragment,container,false);
        initView();
        list=new ArrayList<>();
        initData();
//        new SimpleAdapter()
//        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
//        adapter=new PlanAdapter(list,getActivity());
        adapter=new PlanAdapter(list,getContext());
        listView.setAdapter(adapter);
        initEvent();
        return view;

    }

    private void initEvent() {
        bt_next.setOnClickListener(this);
        bt_back.setOnClickListener(this);
    }

    private void initData() {
        list.clear();
        for (int i = 0; i < 7; i++) {
            if (isFirst){
                isFirst=false;
            }else{
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
            }
//            int month=calendar.get(Calendar.MONTH)+1;
//            calendar.
//            String date=calendar.get(Calendar.YEAR)+"/"+decimalFormat.format(month)+"/"+calendar.get(Calendar.DAY_OF_MONTH);
            list.add(new Review(table,format.format(calendar.getTime())));
        }
        if(!list.get(0).getDate().equals(format.format(new Date()))){
            bt_back.setEnabled(true);
            return;
        }
    }

    private void initView() {
        bt_back= (Button) view.findViewById(R.id.bt_back);
        bt_next= (Button) view.findViewById(R.id.bt_next);
        bt_back.setVisibility(View.VISIBLE);
        bt_next.setVisibility(View.VISIBLE);
        listView= (ListView) view.findViewById(R.id.lv_study);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:
                bt_back.setEnabled(true);
                initData();
                adapter.notifyDataSetChanged();
                break;
            case R.id.bt_back:
                lastWeek();
                adapter.notifyDataSetChanged();
                break;
        }

    }

    private void lastWeek() {
        Date date=null;
        try {
            date = format.parse(list.get(0).getDate());
//            if(format.format(date).equals(format.format(new Date()))){
//                bt_back.setEnabled(false);
//                return;
//            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        list.clear();
        for (int i = 0; i <7 ; i++) {
            if(i==0){
                calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-7);
            }else {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
            }
//            int month=calendar.get(Calendar.MONTH)+1;
//
//            String dates=calendar.get(Calendar.YEAR)+"/"+decimalFormat.format(month)+"/"+calendar.get(Calendar.DAY_OF_MONTH);
            list.add(new Review(table,format.format(calendar.getTime())));
        }
        Log.e("date",list.get(0).getDate());
        Log.e("date",format.format(new Date()));
        if(list.get(0).getDate().equals(format.format(new Date()))){
            bt_back.setEnabled(false);
        }
    }

//    @Override
//    public void onPause() {
//        Log.e("ALL","planpause");
//        super.onPause();
//        try {
//            calendar.setTime(format.parse(list.get(0).getDate()));
//            ((ReviewActivity_2)getActivity()).setCalendar(calendar);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.close();
        try {
            calendar.setTime(format.parse(list.get(0).getDate()));
            ((ReviewActivity_2)getActivity()).setCalendar(calendar);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        list.clear();
    }
}
