package com.lee.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lee.activity.ReviseActivity;
import com.lee.adapter.ReviewAdapter;
import com.lee.assist.MyHelper;
import com.lee.assist.Position;
import com.lee.words.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/16.
 */
public class ReviewFragment extends Fragment implements AdapterView.OnItemClickListener {
    private TextView textView;
    private ListView listView;
    private View view;
    private List<Position> list;
    private MyHelper helper;
    private String table;
    private int index=-1;
    private ReviewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment,container,false);
        helper=new MyHelper(getContext());
//        helper=new MyHelper(getActivity());
        initView();
        initData();
        initEvent();
//        adapter = new ReviewAdapter(getActivity(),list);
        adapter = new ReviewAdapter(getContext(),list);
        listView.setAdapter(adapter);
        return view;

    }

    private void initEvent() {
        listView.setOnItemClickListener(this);
    }

    private void initData() {
        Intent intent = getActivity().getIntent();
        table = intent.getStringExtra("table");
        list=helper.getReviewList(table);
    }

    private void initView() {
        textView= (TextView) view.findViewById(R.id.textView);
        listView= (ListView) view.findViewById(R.id.lv_study);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ReviseActivity.actionStart(getActivity(),list.get(position),1);
//        Intent intent=new Intent(getActivity(),ReviseActivity.class);
//        intent.putExtra("position", list.get(position));
//        startActivityForResult(intent, 1);
        index=position;
        showDialog(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        if(requestCode==1&&resultCode==1){
            list.remove(index);
            adapter.notifyDataSetChanged();
        }
    }
    private void showDialog(int position){
        final int p=position;
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("开始复习 LIST-" + list.get(position).getList()+"\n\n\n");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(getActivity(),ReviseActivity.class);
                intent.putExtra("position", list.get(p));
                startActivityForResult(intent, 1);
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
        adapter.close();
        list.clear();
    }
}
