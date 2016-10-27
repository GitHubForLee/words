package com.lee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.lee.fragment.LetfFragment;
import com.lee.fragment.RightFragment;
import com.lee.words.R;

/**
 * Created by Administrator on 2016/9/13.
 */
public class StudyActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment leftFrament,rightFrament;

    public static void actionStart(Context context,String table){
        Intent intent=new Intent(context,StudyActivity.class);
        intent.putExtra("table",table);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        manager=getSupportFragmentManager();
//        listView= (ListView) findViewById(R.id.lv_study);
//        list=new ArrayList<>();
//        intent = getIntent();
//        String table=intent.getStringExtra("table");
//        for (int i = 0; i < new MyHelper(this).getListNum(table); i++) {
//            int index=i+1;
//            list.add("LIST-"+index);
//        }
//        listView.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,list));
//        listView.setOnItemClickListener(this);
        leftFrament=new LetfFragment();
        rightFrament=new RightFragment();
        initView();
        changeFragment(rightFrament);
    }

    private void initView() {
        radioGroup= (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);
    }

    private void changeFragment(Fragment fragment){
        transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_all:
                changeFragment(leftFrament);
                break;
            case R.id.rb_unlearn:
                changeFragment(rightFrament);
                break;
        }
    }
}
