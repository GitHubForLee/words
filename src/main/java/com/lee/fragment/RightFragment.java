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
import com.lee.adapter.UnLearnAdapter;
import com.lee.assist.MyHelper;
import com.lee.words.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class RightFragment extends Fragment implements AdapterView.OnItemClickListener {
//    private Context context;
    private MyHelper helper;
    private ListView listView;
    private List<String> list;
    private Intent intent;
    private String[] unlearned;
//    private ArrayAdapter adapter;
    private UnLearnAdapter adapter;
    private String table;
    private int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper=new MyHelper(getContext());
//        helper=new MyHelper(getActivity());
        View view=inflater.inflate(R.layout.fragment,container,false);
        listView= (ListView)view.findViewById(R.id.lv_study);
        list=new ArrayList<>();
        intent = getActivity().getIntent();
        table = intent.getStringExtra("table");
        unlearned=helper.getUnLearned(table);
        for (int i = 0; i < unlearned.length; i++) {
            list.add("LIST-"+unlearned[i]);
        }
        adapter=new UnLearnAdapter(list,getContext());
//        adapter=new UnLearnAdapter(list,getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        index = position;
        showDialog(position);
//        int p=position;
//        int list = Integer.parseInt(unlearned[p]);
//        intent.putExtra("list", list);
//        intent.setClass(getActivity(), ContentActicity.class);
//        startActivityForResult(intent, 0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0&&resultCode==0){
            if( data!=null&&data.getBooleanExtra("learned",false)) {
                list.remove(index);
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void showDialog(int position){
        final int p=position;
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("开始学习");
        builder.setMessage("LIST-" + unlearned[position]+"\n\n\n");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int list = Integer.parseInt(unlearned[p]);
                intent.putExtra("list", list);
                intent.setClass(getActivity(), ContentActicity.class);
                startActivityForResult(intent, 0);
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("lifecircle", "desright");
        helper.close();
        list.clear();
    }
}
