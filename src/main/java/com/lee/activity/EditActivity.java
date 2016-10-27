package com.lee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lee.assist.MyHelper;
import com.lee.words.R;
import com.lee.assist.Words;

/**
 * Created by Administrator on 2016/9/16.
 */
public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_spelling,et_mean;
    private Button bt_sure,bt_cancle;
    private MyHelper helper;
    private Words words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
//        helper=new MyHelper(this);
        helper=new MyHelper(getApplicationContext());
        initContent();
    }

    private void initContent() {
        Intent intent=getIntent();
        words = (Words) intent.getSerializableExtra("words");
        et_spelling.setText(words.getSpelling());
        et_spelling.setSelection(words.getSpelling().length());
        et_mean.setText(words.getMeanning());

    }

    private void initView() {
        et_mean= (EditText) findViewById(R.id.et_mean);
        et_spelling= (EditText) findViewById(R.id.et_spelling);
        bt_cancle= (Button) findViewById(R.id.bt_cancle);
        bt_sure= (Button) findViewById(R.id.bt_sure);
        bt_cancle.setOnClickListener(this);
        bt_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_cancle:
                finish();
                break;
            case R.id.bt_sure:
                updataWords();
                Intent intent=getIntent();
                intent.putExtra("spelling",et_spelling.getText().toString());
                intent.putExtra("mean",et_mean.getText().toString());
                setResult(1,intent);
                finish();
                break;
        }

    }
    private void updataWords(){
        words.setSpelling(et_spelling.getText().toString());
        words.setMeanning(et_mean.getText().toString());
        helper.updataWords(words);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }
}
