package com.lee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lee.adapter.TestAdapter;
import com.lee.assist.MyHelper;
import com.lee.assist.Position;
import com.lee.words.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class TestActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private List<Position> list;
    private MyHelper helper;
    private TestAdapter adapter;
    public static void actionStart(Context context,String table){
        Intent intent=new Intent(context,TestActivity.class);
        intent.putExtra("table",table);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initAttrs();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initData();
        adapter.notifyDataSetChanged();

    }

    private void initEvent() {
        listView.setOnItemClickListener(this);
    }

    private void initData() {
        Intent intent=getIntent();
        String table=intent.getStringExtra("table");
        for (int i = 0; i < helper.getListNum(table); i++) {
            int index=i+1;
            list.add(new Position(table,index+""));
        }
        adapter=new TestAdapter(getApplicationContext(),list);
//        adapter=new TestAdapter(this,list);
        listView.setAdapter(adapter);
    }

    private void initAttrs() {
        list=new ArrayList<>();
        helper=new MyHelper(getApplicationContext());
        listView= (ListView) findViewById(R.id.listView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExamActivity.actionStart(this,list.get(position));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
        adapter.close();
    }
}
