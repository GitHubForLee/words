package com.lee.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.assist.MyHelper;
import com.lee.assist.Position;
import com.lee.words.R;
import com.lee.assist.Words;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/17.
 */
public class ExamActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private TextView tv_words;
    private RadioGroup radioGroup;
    private RadioButton rb_mean1, rb_mean2, rb_mean3, rb_mean4, rb_mean5;
    private Button bt_next, bt_addInto;
    private List<Words> list;
    private MyHelper helper;
    private int crtIndex = 0;
    private String table;
    private double score = 0;
    private int[] randomMean = new int[4];
    private Position position;
    private int random[];
    private int radioId = -1;
    private boolean flag = false;
    private List<Words> temp;

    public static void actionStart(Context context, Position position) {
        Intent intent = new Intent(context, ExamActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        initAttrs();
        initData();
        setContent();
        initEvent();
    }

    private void initEvent() {
        radioGroup.setOnCheckedChangeListener(this);
        bt_next.setOnClickListener(this);
        bt_addInto.setOnClickListener(this);
    }

    private void setContent() {
        Words words = list.get(crtIndex);
        getRandomNum();
        tv_words.setText(words.getSpelling() + " " + words.getFayin());
        rb_mean1.setText(list.get(randomMean[0]).getMeanning());
        rb_mean2.setText(list.get(randomMean[1]).getMeanning());
        rb_mean3.setText(list.get(randomMean[2]).getMeanning());
        rb_mean4.setText(list.get(randomMean[3]).getMeanning());

    }

    private void initData() {
        Intent intent = getIntent();
        position = (Position) intent.getSerializableExtra("position");
        table = position.getTable();
        String lists = position.getList();
        temp = helper.getWordsList(table, lists);
        random=getRandom();
        list=new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            list.add(temp.get(random[i]));
        }

    }

    private void initAttrs() {
        tv_words = (TextView) findViewById(R.id.tv_words);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        bt_addInto = (Button) findViewById(R.id.bt_addInto);
        bt_next = (Button) findViewById(R.id.bt_next);
        rb_mean1 = (RadioButton) findViewById(R.id.rb_mean1);
        rb_mean2 = (RadioButton) findViewById(R.id.rb_mean2);
        rb_mean3 = (RadioButton) findViewById(R.id.rb_mean3);
        rb_mean4 = (RadioButton) findViewById(R.id.rb_mean4);
        rb_mean5 = (RadioButton) findViewById(R.id.rb_mean5);
//        helper = new MyHelper(this);
        helper = new MyHelper(getApplicationContext());
    }

    private void getRandomNum() {
        int[] rans = new int[4];
        Random random = new Random();
        rans[0] = crtIndex;
        for (int i = 1; i < rans.length; i++) {
            rans[i] = random.nextInt(list.size());
            for (int j = 0; j < i; j++) {
                if (rans[i] == rans[j]) {
                    i--;
                    break;
                }
            }
        }
        setRandomMean(rans);
    }

    private void setRandomMean(int[] rans) {
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            randomMean[i] = random.nextInt(4);
            for (int j = 0; j < i; j++) {
                if (randomMean[i] == randomMean[j]) {
                    i--;
                    break;
                }

            }

        }
        for (int i = 0; i < 4; i++) {
            randomMean[i] = rans[randomMean[i]];
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton button;
        if (radioId == -1 || radioId == R.id.rb_mean5) {
            button = (RadioButton) findViewById(checkedId);
            radioId = checkedId;
        } else {
            Log.e("Exam", "radioId" + radioId);
            button = (RadioButton) findViewById(radioId);
            button.setChecked(true);
        }
        Log.e("Exam", "checkId" + checkedId);
//
//            button = (RadioButton) findViewById(checkedId);
        String answer = list.get(crtIndex).getMeanning();
        String yourAns = button.getText().toString();
        Log.e("Exam", "ans" + yourAns);
        if (radioId != R.id.rb_mean5) {
            showButton();
            while (!flag) {
                flag = true;
                if (answer.equals(yourAns)) {
                    score++;
                    Log.e("score", score + "");
                    Toast.makeText(ExamActivity.this, "答对了！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ExamActivity.this, "正确答案是：" + answer, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showButton() {
        bt_addInto.setVisibility(View.VISIBLE);
        bt_next.setVisibility(View.VISIBLE);
        if (crtIndex == list.size() - 1) {
            bt_next.setText("结束");
        }
    }

    private void hideButton() {
        bt_addInto.setVisibility(View.GONE);
        bt_next.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                radioId = -1;
                flag = false;
                nextWords();
                break;
            case R.id.bt_addInto:
                addIntoATTENTION();
                break;
        }
    }

    private void addIntoATTENTION() {
        if (helper.isExist(list.get(crtIndex).getSpelling())) {
            Toast.makeText(this, "单词已存在", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "加入成功", Toast.LENGTH_SHORT).show();
            helper.InsertIntoATTENTION(list.get(crtIndex));
        }
//        helper.InsertIntoATTENTION(list.get(crtIndex));
    }

    private void nextWords() {
        if (crtIndex == list.size() - 1) {
            DecimalFormat decimalFormat = new DecimalFormat("##");

//            NumberFormat format =   NumberFormat.getPercentInstance();
            double point = score / list.size();
            String persent = decimalFormat.format(point * 100);
//            String persent=format.format(point);
            String preBest = helper.getBestScore(position);
            if (preBest.isEmpty()) {
                helper.setBestScore(position, persent);
            } else {
                int preBestScore = Integer.parseInt(preBest);
                int crtBestScore = Integer.parseInt(persent);
                if (crtBestScore > preBestScore) {
                    helper.setBestScore(position, persent);
                }
            }


            showDialog(persent);
            return;
        }
        hideButton();
        rb_mean5.setChecked(true);
        crtIndex++;
        if (crtIndex < list.size()) {
            setContent();
        }
    }

    private void showDialog(String perdent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("测试结果");
        builder.setMessage("一共" + list.size() + "题,作对" + score + "题，正确率" + perdent + "%"+"\n\n\n");
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExamActivity.this.finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public int[] getRandom() {
        int random[] =new int[temp.size()];
        Random random1=new Random();
        for (int i = 0; i <temp.size() ; i++) {
            random[i]=random1.nextInt(temp.size());
            for (int j = 0; j < i; j++) {
                if(random[i]==random[j]){
                    i--;
                    break;
                }
            }
        }
        return random;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }
}
