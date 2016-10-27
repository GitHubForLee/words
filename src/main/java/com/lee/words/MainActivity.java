package com.lee.words;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.activity.MoshengActivity;
import com.lee.activity.ReviewActivity_2;
import com.lee.activity.StudyActivity;
import com.lee.activity.TestActivity;
import com.lee.assist.MyHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView select_ciku, ciku_name, danci_num, chuanjian_time, study_progress, review_progress;
    private Button bt_study, bt_repeat, bt_test, bt_shengci;
    private ProgressBar bar, review_bar;
    private SharedPreferences preferences;
    private MyHelper helper;
    private boolean flag = false;
    private double preTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new MyHelper(MainActivity.this);

        helper.shouldReview();
        flag = true;
        setContentView(R.layout.sliding_main);
        initView();
        initEvent();
        setContent();
    }

    private void initView() {
        study_progress = (TextView) findViewById(R.id.study_progress);
        review_progress = (TextView) findViewById(R.id.review_progress);
        select_ciku = (TextView) findViewById(R.id.select_ciku);
        ciku_name = (TextView) findViewById(R.id.ciku_name);
        danci_num = (TextView) findViewById(R.id.danci_num);
        chuanjian_time = (TextView) findViewById(R.id.chuanjian_time);
        bt_repeat = (Button) findViewById(R.id.bt_repeat);
        bt_shengci = (Button) findViewById(R.id.bt_mosheng);
        bt_study = (Button) findViewById(R.id.bt_study);
        bt_test = (Button) findViewById(R.id.bt_test);
        preferences = getSharedPreferences("ciku", MODE_PRIVATE);
        select_ciku.setText(preferences.getString("ciku", "单词GRE词库一"));
        bar = (ProgressBar) findViewById(R.id.progressBar);
        review_bar = (ProgressBar) findViewById(R.id.review_bar);
    }

    private void initEvent() {
        select_ciku.setOnClickListener(this);
        bt_study.setOnClickListener(this);
        bt_shengci.setOnClickListener(this);
        bt_repeat.setOnClickListener(this);
        bt_test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_ciku:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setSingleChoiceItems(R.array.ciku, getInt(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        select_ciku.setText(getResources().getStringArray(R.array.ciku)[which]);
                        dialog.cancel();
                        setContent();
                    }
                });
//                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.bt_mosheng:
                startActivity(new Intent(this, MoshengActivity.class));
                break;
            case R.id.bt_repeat:
                ReviewActivity_2.actionStart(this, getTableName());
                break;
            case R.id.bt_study:
                StudyActivity.actionStart(this, getTableName());
//                Intent intent=new Intent(this,StudyActivity.class);
//                intent.putExtra("table",getTableName());
//                startActivity(intent);
                break;
            case R.id.bt_test:
                TestActivity.actionStart(this, getTableName());
                break;

        }
    }

    private int getInt() {
        String[] strings = getResources().getStringArray(R.array.ciku);
        for (int i = 0; i < strings.length; i++) {
            if (select_ciku.getText().toString().equals(strings[i])) {
                return i;
            }
        }
        return -1;
    }

    private void setContent() {
        String table = getTableName();
        int listCount = helper.getListNum(table);
        ciku_name.setText(select_ciku.getText().toString());
        danci_num.setText(helper.getCount(table) + "");
        chuanjian_time.setText(helper.getTime(table));
        study_progress.setText(helper.getStudyProgress(table));
        bar.setMax(listCount);
        review_bar.setMax(listCount);
        bar.setProgress(listCount - helper.getUnLearned(table).length);
        review_bar.setProgress(listCount - helper.getNotFinishReview(table));
        review_progress.setText("复习进度：" + (listCount - helper.getNotFinishReview(table)) + "/" + listCount);
    }

    private String getTableName() {
        String table = null;
        switch (getInt()) {
            case 0:
                table = MyHelper.BOOK_1;
                break;
            case 1:
                table = MyHelper.BOOK_2;
                break;
            case 2:
                table = MyHelper.BOOK_3;
                break;
        }
        return table;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag) {
            setContent();
//            study_progress.setText(helper.getStudyProgress(getTableName()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ciku", select_ciku.getText().toString());
            editor.commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                double crtTime = System.currentTimeMillis();
                if (crtTime - preTime > 2000) {
                    preTime = crtTime;
                    Toast.makeText(MainActivity.this, "请再按一次退出程序", Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);

    }
}
