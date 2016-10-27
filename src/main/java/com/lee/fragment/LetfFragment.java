package com.lee.fragment;

import android.app.AlertDialog;
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
import com.lee.adapter.AllAdapter;
import com.lee.assist.MyHelper;
import com.lee.assist.Position;
import com.lee.words.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class LetfFragment extends Fragment implements AdapterView.OnItemClickListener {
//    private Context context;
    private ListView listView;
//    List<String> list;
    List<Position> lists;
    Intent intent;
    MyHelper helper;
    private AllAdapter allAdapter;
    private String table;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment,container,false);
        listView= (ListView)view.findViewById(R.id.lv_study);
        helper=new MyHelper(getContext());
//        helper=new MyHelper(getActivity());
        lists=new ArrayList<>();
        intent = getActivity().getIntent();
        table = intent.getStringExtra("table");
        for (int i = 0; i < helper.getListNum(table); i++) {
            int index=i+1;
            lists.add(new Position(table,index+""));
        }
//        allAdapter = new AllAdapter(lists,getActivity(),AllAdapter.STUDY_TYPE);
        allAdapter = new AllAdapter(lists,getContext(),AllAdapter.STUDY_TYPE);
        listView.setAdapter(allAdapter);

        listView.setOnItemClickListener(this);
        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        //刷新适配器会重新刷新listview的所有视图，调用getview();
        allAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int list=position+1;
        showDialog(new Position(table, list + ""));

    }
    private void showDialog(Position position){
       final int list=Integer.parseInt(position.getList());
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(helper.isLearned(position) ? "提示" : "开始学习");
        builder.setMessage(helper.isLearned(position) ? "已学习过，是否重新学习？" +"\n\n\n": "LIST-" + position.getList()+"\n\n\n");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent.putExtra("list", list);
                intent.setClass(getActivity(), ContentActicity.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
        allAdapter.close();
        Log.e("lifecircle", "desleft");
        lists.clear();
    }
}
