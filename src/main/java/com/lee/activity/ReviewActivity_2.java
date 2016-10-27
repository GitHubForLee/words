package com.lee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.lee.fragment.AllFragment;
import com.lee.fragment.PlanFragment;
import com.lee.fragment.ReviewFragment;
import com.lee.words.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/9/16.
 */
public class ReviewActivity_2 extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
//    private ViewPager viewPager;
    private FragmentManager manager;
    private List<Fragment> lists;
    private RadioGroup radioGroup;
    private Calendar calendar;

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public static void actionStart(Context context,String table){
        Intent intent=new Intent(context,ReviewActivity_2.class);
        intent.putExtra("table", table);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_2);
        manager=getSupportFragmentManager();
        initView();
        initData();
        changeFragment(lists.get(0));
        initEvent();
    }

    private void initEvent() {
        radioGroup.setOnCheckedChangeListener(this);
//        viewPager.addOnPageChangeListener(this);
    }

    private void initData() {
        lists=new ArrayList<>();
        lists.add(new ReviewFragment());
        lists.add(new AllFragment());
        lists.add(new PlanFragment());
        calendar=Calendar.getInstance();
    }

    private void initView() {
//        viewPager= (ViewPager) findViewById(R.id.fragment_container);
        radioGroup= (RadioGroup) findViewById(R.id.radio_group);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_review:
                changeFragment(lists.get(0));
                break;
            case R.id.rb_all:
                changeFragment(lists.get(1));
                break;
            case R.id.rb_plan:
                ((PlanFragment)lists.get(2)).setCalendar(calendar);
                changeFragment(lists.get(2));
                break;
        }
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }
}
