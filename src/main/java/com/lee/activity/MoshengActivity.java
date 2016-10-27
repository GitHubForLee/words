package com.lee.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lee.adapter.MoshengAdapter;
import com.lee.assist.MyHelper;
import com.lee.words.R;
import com.lee.assist.Words;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class MoshengActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,DialogInterface.OnClickListener {
    private ListView listView;
    private List<Words> list;
    private MyHelper helper;
    private MoshengAdapter adapter;
    private String[] operator;
    private AlertDialog dialog;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("mo","create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosheng);
        listView= (ListView) findViewById(R.id.listView);
//        helper=new MyHelper(this);
        helper=new MyHelper(getApplicationContext());
        list=new ArrayList<>();
        list=helper.getAtnWords();
//        adapter=new MoshengAdapter(list,this);
        adapter=new MoshengAdapter(list,getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


        operator=getResources().getStringArray(R.array.operator);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("操作");
        builder.setItems(operator, this);
        builder.setNegativeButton("取消", this);
        builder.setCancelable(false);

        dialog=builder.create();
        dialog.show();
        index=position;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case AlertDialog.BUTTON_NEGATIVE:
                dialog.cancel();
                break;
            case 0:
                Intent intent=new Intent(this,EditActivity.class);
                intent.putExtra("words",list.get(index));
                startActivityForResult(intent, 0);
                break;
            case 1:
                helper.deleteWords(list.get(index));
                list.remove(list.get(index));
//                list=helper.getAtnWords();
//                adapter=new MoshengAdapter(list,this);
//                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onResume() {
        Log.e("mo", "asdfasd");
        super.onResume();
        adapter.notifyDataSetChanged();
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("mo","asdfasd");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0&&resultCode==1){
            String spelling = data.getStringExtra("spelling");
            String mean=data.getStringExtra("mean");
            list.get(index).setSpelling(spelling);
            list.get(index).setMeanning(mean);
        }
    }
}
