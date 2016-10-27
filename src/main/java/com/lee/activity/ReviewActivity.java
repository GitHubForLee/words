package com.lee.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lee.fragment.AllFragment;
import com.lee.fragment.PlanFragment;
import com.lee.fragment.ReviewFragment;
import com.lee.words.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/16.
 */
public class ReviewActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private FragmentManager manager;
    private List<Fragment> lists;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        manager=getSupportFragmentManager();
        initView();
        initData();
        viewPager.setAdapter(new Adapter(manager));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(0);
        initEvent();
    }

    private void initEvent() {
        radioGroup.setOnCheckedChangeListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    private void initData() {
        lists=new ArrayList<>();
        lists.add(new ReviewFragment());
        lists.add(new AllFragment());
        lists.add(new PlanFragment());
    }

    private void initView() {
        viewPager= (ViewPager) findViewById(R.id.fragment_container);
        radioGroup= (RadioGroup) findViewById(R.id.radio_group);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton button= (RadioButton) findViewById(checkedId);
        viewPager.setCurrentItem(group.indexOfChild(button));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((RadioButton)radioGroup.getChildAt(position)).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class Adapter extends FragmentPagerAdapter{
        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return lists.get(position);
        }

        @Override
        public int getCount() {
            return lists.size();
        }
    }
}
