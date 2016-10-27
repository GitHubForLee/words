package com.lee.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.assist.MyHelper;
import com.lee.assist.Position;
import com.lee.words.R;
import com.lee.assist.Words;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/9/17.
 */
public class ReviseActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {
    private TextView tv_id,tv_spelling,tv_fayin,tv_meanning;
    private Button bt_remember,bt_unremember,bt_next,bt_addInto;
    private List<Words> list;
    private MyHelper helper;
    private int crtIndex=0;
    private Position position;
    private Intent intent;
    private List<Words> worngList;
    private int i=0;
    private int count;
    private ImageView iv_voice;
    private TextToSpeech speech;
//    public static void actionStart(Context context,Position position,int requestCode){
//        Intent intent=new Intent(context,ReviseActivity.class);
//        intent.putExtra("position", position);
//        ((ReviewActivity)context).startActivityForResult(intent, requestCode);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise);
        initAttrs();
        initData();
        setContent();
        initEvent();
    }

    private void setContent() {
        Log.e("index",crtIndex+"   "+list.size());
        if(crtIndex<list.size()){

            Words crtWords=list.get(crtIndex);
            tv_id.setText(crtWords.getNumber());
            tv_spelling.setText(crtWords.getSpelling());
            tv_fayin.setText(crtWords.getFayin());
            tv_meanning.setText("请尽力回想中文意思！");
        }else{

        }
    }

    private void initData() {
        intent = getIntent();

        position = (Position) intent.getSerializableExtra("position");
        list=helper.getWordsList(position.getTable(), position.getList());
        count=list.size();
    }

    private void initEvent() {
        bt_unremember.setOnClickListener(this);
        bt_remember.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        bt_addInto.setOnClickListener(this);
        iv_voice.setOnClickListener(this);
    }

    private void initAttrs() {
        bt_addInto= (Button) findViewById(R.id.bt_addInto);
        bt_next= (Button) findViewById(R.id.bt_next);
        bt_remember= (Button) findViewById(R.id.bt_remember);
        bt_unremember= (Button) findViewById(R.id.bt_unremember);
        tv_id= (TextView) findViewById(R.id.tv_id);
        tv_spelling= (TextView) findViewById(R.id.tv_spelling);
        tv_meanning= (TextView) findViewById(R.id.tv_meanning);
        tv_fayin= (TextView) findViewById(R.id.tv_fayin);
        iv_voice= (ImageView) findViewById(R.id.iv_voice);
        speech=new TextToSpeech(this,this);
        list=new ArrayList<>();
        worngList=new ArrayList<>();
        helper=new MyHelper(getApplicationContext());
//        helper=new MyHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_unremember:
                unremember();
                break;
            case R.id.bt_remember:
                remember();
                break;
            case R.id.bt_next:
                crtIndex++;
                bt_unremember.setVisibility(View.VISIBLE);
                bt_remember.setVisibility(View.VISIBLE);
                bt_next.setVisibility(View.GONE);
                setContent();
                break;
            case R.id.bt_addInto:
                addIntoATTENTION();
                break;
            case R.id.iv_voice:
                if (speech!=null&&!speech.isSpeaking()) {
                    speech.speak(tv_spelling.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
                break;


        }

    }

    private void addIntoATTENTION() {
        if(helper.isExist(list.get(crtIndex).getSpelling())){
            Toast.makeText(ReviseActivity.this, "单词已存在", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ReviseActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
            helper.InsertIntoATTENTION(list.get(crtIndex));
        }
    }

    private void unremember() {
        worngList.add(list.get(crtIndex));

        if(bt_unremember.getText().toString().equals("不记得了")){
            tv_meanning.setText(list.get(crtIndex).getMeanning());
            bt_next.setVisibility(View.VISIBLE);
            bt_unremember.setVisibility(View.GONE);
            bt_remember.setVisibility(View.GONE);
            if(crtIndex==list.size()-1) {
                crtIndex=-1;
                list.clear();
                list.addAll(worngList);
                worngList.clear();
                Log.e("index","listsize"+list.size());
            }
        }else{
            if(crtIndex==list.size()-1) {
                crtIndex=-1;
                list.clear();
                list.addAll(worngList);
                worngList.clear();
            }
            crtIndex++;
            bt_remember.setText("我记得");
            bt_unremember.setText("不记得了");
            setContent();
        }

    }

    private void remember() {
        if(bt_remember.getText().toString().equals("我记得")) {

            tv_meanning.setText(list.get(crtIndex).getMeanning());
            bt_remember.setText("记对了");
            bt_unremember.setText("记错了");
        }else{
            i++;
            Log.e("iancount",i+"  "+count);
            if(i>=count) {
                showDialog();
//                reviewFinished();
                return;
            }
            if(crtIndex==list.size()-1) {
                crtIndex=-1;
                list.clear();
                list.addAll(worngList);
                worngList.clear();
            }
            crtIndex++;
            setContent();
            bt_remember.setText("我记得");
            bt_unremember.setText("不记得了");
        }
    }
    public void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("操作");
        builder.setMessage("复习完成"+"\n\n\n");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reviewFinished();
            }
        });
        builder.show();

    }
    public void reviewFinished(){
            helper.setReviewTime(position);
            intent.putExtra("review",true);
            setResult(1,intent);
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        intent.putExtra("review",true);
//        setResult(1,intent);
        helper.close();
    }

    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.SUCCESS){
            int result=speech.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
