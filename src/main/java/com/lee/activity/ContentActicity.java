package com.lee.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.assist.MyHelper;
import com.lee.assist.Words;
import com.lee.words.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/9/13.
 */
public class ContentActicity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {
    private TextView tv_spelling,tv_fayin,tv_mean,tv_number;
    private Button bt_back,bt_next,bt_addInto;
    private Intent intent;
    private List<Words> list;
    private MyHelper helper;
    private String table;
    private ImageView iv_voice;
    private TextToSpeech speech;
    private int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
//        helper = new MyHelper(this);
        helper = new MyHelper(getApplicationContext());
        intent=getIntent();
        initView();
        table = intent.getStringExtra("table");
        list=helper.getWordsList(table, getListName());
        setContent();
        speech=new TextToSpeech(this,this);
    }

    private void initView() {
        tv_number= (TextView) findViewById(R.id.tv_number);
        tv_fayin= (TextView) findViewById(R.id.tv_fayin);
        tv_mean= (TextView) findViewById(R.id.tv_mean);
        tv_spelling= (TextView) findViewById(R.id.tv_spelling);
        bt_back= (Button) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(this);
        bt_next= (Button) findViewById(R.id.bt_next);
        bt_next.setOnClickListener(this);
        bt_addInto= (Button) findViewById(R.id.bt_addInto);
        bt_addInto.setOnClickListener(this);
        iv_voice= (ImageView) findViewById(R.id.iv_voice);
        iv_voice.setOnClickListener(this);


    }

    private String getListName(){
        return intent.getIntExtra("list",-1)+"";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:
                if(index<list.size()-1){
                    index++;
                    setContent();
                }else{
                    showDialog();
                }
                break;
            case R.id.bt_back:
                if(index>0){
                    index--;
                    setContent();
                }else {
                    Toast.makeText(ContentActicity.this, "前面没有了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_addInto:
                if(helper.isExist(tv_spelling.getText().toString())){
                    Toast.makeText(ContentActicity.this, "单词已存在", Toast.LENGTH_SHORT).show();
                }else{
                    Words words=new Words(tv_number.getText().toString(),tv_spelling.getText().toString(),
                            tv_mean.getText().toString(),tv_fayin.getText().toString());
                    Toast.makeText(ContentActicity.this, "加入成功", Toast.LENGTH_SHORT).show();
                    helper.InsertIntoATTENTION(words);
                }
                break;
            case R.id.iv_voice:
                if (speech!=null&&!speech.isSpeaking()) {
                    speech.speak(tv_spelling.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                }
                break;
        }
    }
    private void setContent(){
        Words crtWords=list.get(index);
        tv_number.setText(crtWords.getNumber());
        tv_spelling.setText(crtWords.getSpelling());
        tv_fayin.setText(crtWords.getFayin());
        tv_mean.setText(crtWords.getMeanning());
    }

    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.SUCCESS){
            Locale locale=Locale.UK;
            int result=speech.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("学习完成"+"\n\n\n");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.learned(table, getListName());
                intent.putExtra("learned",true);
                setResult(0, intent);
                finish();
            }
        });
        builder.show();
    }
}
