package com.lee.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lee.activity.ContentActicity;
import com.lee.activity.ReviseActivity;
import com.lee.adapter.AllAdapter;
import com.lee.assist.MyHelper;
import com.lee.assist.Position;
import com.lee.words.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class AllFragment extends Fragment implements AdapterView.OnItemClickListener,DialogInterface.OnClickListener {
    private ListView listView;
    List<Position> lists;
    Intent intent;
    private MyHelper helper;
    private int curPositon;
    private String table;
    private AllAdapter allAdapter;
    private static final String TAG = "AllFragment";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "attach");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG,"creatview");

        helper=new MyHelper(getContext());
//        helper=new MyHelper(getActivity());
        View view=inflater.inflate(R.layout.fragment,container,false);
        listView= (ListView)view.findViewById(R.id.lv_study);
        intent = getActivity().getIntent();
        table = intent.getStringExtra("table");
        lists=new ArrayList<>();
        for (int i = 0; i <helper.getListNum(table); i++) {
            int index=i+1;
            Position position=new Position(table,index+"");
            position.setIslearned(helper.isLearned(position));
            position.setNeedReviewed(helper.isNeedReview(position));
            position.setFinishReview(helper.finishReview(position));
            lists.add(position);
        }
//        allAdapter = new AllAdapter(lists,getActivity(),AllAdapter.REVIEW_TYPE);
        allAdapter = new AllAdapter(lists,getContext(),AllAdapter.REVIEW_TYPE);
        listView.setAdapter(allAdapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "destroy");
        helper.close();
        allAdapter.close();

    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        curPositon=position;

       showDialog(lists.get(position));

    }
    private void showDialog(Position position){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("操作");
        if(position.isFinishReview()){
            builder.setMessage("已完成复习。是否继续复习？"+"\n\n\n");
        }else if (!position.islearned()){
            builder.setMessage("未学完，是否开始学习？"+"\n\n\n");
        }else if(position.isNeedReviewed()){
            builder.setMessage("开始复习?"+"\n\n\n");

        }else {
            builder.setMessage("暂不需要学习，是否现在复习？"+"\n\n\n");
        }
        builder.setPositiveButton("确定",this);
//        builder.setCancelable(false);
        builder.show();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case Dialog.BUTTON_POSITIVE:

                if(!lists.get(curPositon).islearned()){
                    intent.putExtra("list", curPositon+1);
                    intent.setClass(getActivity(), ContentActicity.class);

                }else {
                    intent.putExtra("position",lists.get(curPositon));
                    intent.setClass(getActivity(), ReviseActivity.class);
                }
                startActivityForResult(intent, 1);
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lists.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //结果码默认为0:如果结果码是0，要判断data是否为空。
        if(data==null){
            return;
        }
        if(requestCode!=1){
            return;
        }
        Position position=lists.get(curPositon);
        switch (resultCode){
            case 0:
                if(data.getBooleanExtra("learned",false)){
                    position.setIslearned(true);
                }
                allAdapter.notifyDataSetChanged();
                break;
            case 1:
                if(data.getBooleanExtra("review", false)){
                    if(helper.finishReview(position)){
                        position.setFinishReview(true);
                    }
                    position.setNeedReviewed(false);
                }
                allAdapter.notifyDataSetChanged();
                break;
        }
    }
}
