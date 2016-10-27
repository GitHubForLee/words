package com.lee.assist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lee.words.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class MyHelper {
    public static final String BOOK_1="book1";
    public static final String BOOK_2="book2";
    public static final String BOOK_3="book3";
    private final String ATTENTION="ATTENTION";
    private final String PLAN="PLAN";
    private final String BOOKS="BOOKS";
    private SQLiteDatabase db;
    private OpenHelper helper;
    private Context context;
    private InputStream inputStream;
    private SimpleDateFormat dateFormat;
    public MyHelper(Context context){
        this.context=context;
        copy();
        helper=new OpenHelper(context,"mydatabase.db",null,2);
        db=helper.getReadableDatabase();
    }


//    public Cursor getCursor(String table,String list){
//        return db.query(table,new String[]{"ID","SPELLING","MEANNING","PHONETIC_ALPHABET","LIST"},"LIST=?",new String[]{list},null,null,null);
//    }
    public List<Words> getWordsList(String table,String list){
        List<Words> words=new ArrayList<>();
        Cursor cursor=db.query(table, new String[]{"ID", "SPELLING", "MEANNING", "PHONETIC_ALPHABET", "LIST"}, "LIST=?", new String[]{list}, null, null, null);
        while (cursor.moveToNext()){
            String number=cursor.getString(cursor.getColumnIndex("ID"));
            String spelling=cursor.getString(cursor.getColumnIndex("SPELLING"));
            String meanning=cursor.getString(cursor.getColumnIndex("MEANNING"));
            String fayin=cursor.getString(cursor.getColumnIndex("PHONETIC_ALPHABET"));
            words.add(new Words(number,spelling,meanning,fayin));
        }
        cursor.close();
        return words;
    }
    public void close(){
        db.close();
        helper.close();

    }

    public int getListNum(String table){
        Cursor cursor=db.query(PLAN,null,"BOOKID=?",new String[]{table},null,null,null);
        int count=cursor.getCount();
        cursor.close();
        return count;
//        return  db.rawQuery("select count(1) from "+table+" group by List", null).getCount();
    }
    public int getCount(String table){
        Cursor cursor=db.query(table, null, null, null, null, null, null);
        int count=cursor.getCount();
        cursor.close();
        return count;
    }
    public String getTime(String table){
        Cursor cursor=db.query(BOOKS, null, "ID=?", new String[]{table}, null, null, null);
        cursor.moveToNext();
        String creatTime= cursor.getString(cursor.getColumnIndex("GENERATE_TIME"));
        cursor.close();
        return creatTime;
    }
    public String[] getUnLearned(String table){
        int i=0;
        Cursor cursor=db.query(PLAN,new String[]{"LIST"},"BOOKID=? and LEARNED=0",new String[]{table},null,null,null);
        String[] unLearned=new String[cursor.getCount()];
        while (cursor.moveToNext()){
            unLearned[i++]=cursor.getString(cursor.getColumnIndex("LIST"));
        }
        cursor.close();
        return unLearned;
    }
    public int getNotFinishReview(String table){
        Cursor cursor=db.query(PLAN,null,"BOOKID =? and REVIEW_TIMES < ?",new String[]{table,"5"},null,null,null);
        int count=cursor.getCount();
        cursor.close();
        return count;
    }
    public void learned(String table,String list){
        dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        ContentValues values=new ContentValues();
        values.put("LEARNED","1");
        values.put("LEARN_TIME",dateFormat.format(new Date()));
        db.update(PLAN,values,"BOOKID=? and LIST=?",new String[]{table,list});
    }
    public String getStudyProgress(String table){
       int a=getListNum(table)-getUnLearned(table).length;
        return "学习进度："+a+"/"+getListNum(table);
    }
    public boolean isExist(String word){
        Cursor cursor=db.query(ATTENTION, null, "SPELLING=?", new String[]{word}, null, null, null);
        int count=cursor.getCount();
        return count==1?true:false;
    }
    public boolean isLearned(Position position){
        String table=position.getTable();
        String list=position.getList();
        Cursor cursor=db.query(PLAN, null, "BOOKID=? and LIST=? and LEARNED=?", new String[]{table, list, 1 + ""}, null, null, null);
        int count=cursor.getCount();
        cursor.close();
        return count==1?true:false;
    }
    public void InsertIntoATTENTION(Words words){
        String number=words.getNumber();
        String spelling=words.getSpelling();
        String fayin=words.getFayin();
        String meanning=words.getMeanning();
        String list=words.getList();
        ContentValues values=new ContentValues();
        values.put("ID",number);
        values.put("SPELLING",spelling);
        values.put("MEANNING",meanning);
        values.put("PHONETIC_ALPHABET",fayin);
        values.put("LIST", list);
        db.insert(ATTENTION, null, values);
    }
    public List<Words> getAtnWords(){
        List<Words> list=new ArrayList<>();
        Cursor cursor=db.query(ATTENTION, null, null, null, null, null, null);
        Log.e("mosheng", cursor.getCount() + "");
        while (cursor.moveToNext()){
            String id=cursor.getString(cursor.getColumnIndex("ID"));
            String spelling = cursor.getString(cursor.getColumnIndex("SPELLING"));
            String meanning=cursor.getString(cursor.getColumnIndex("MEANNING"));
            list.add(new Words(id,spelling,meanning));
        }
        cursor.close();
        return list;
    }
    public void deleteWords(Words words){
        db.delete(ATTENTION, "SPELLING=?", new String[]{words.getSpelling()});
    }
    public void updataWords(Words words){
       ContentValues values=new ContentValues();
        values.put("SPELLING",words.getSpelling());
        values.put("MEANNING", words.getMeanning());
        db.update(ATTENTION, values, "ID=?", new String[]{words.getNumber()});
    }
    public List<String> getLearnedList(String table){
        List<String> lists=new ArrayList<>();
        Cursor cursor=db.query(PLAN, new String[]{"LIST"}, "BOOKID=? and LEARNED=1", new String[]{table}, null, null, null);
        while (cursor.moveToNext()){
            String list=cursor.getString(cursor.getColumnIndex("LIST"));
            lists.add(list);
        }
        return lists;

    }
    public String getBestScore(Position position){
        String table=position.getTable();
        String list=position.getList();
        Cursor cursor=db.query(PLAN, new String[]{"BESTSCORE"}, "BOOKID=? and LIST=?", new String[]{table, list}, null, null, null);
        cursor.moveToNext();
        String bestScore=cursor.getString(cursor.getColumnIndex("BESTSCORE"));
        cursor.close();
        return bestScore;
    }
    public void setBestScore(Position position,String persent){
        String table=position.getTable();
        String list=position.getList();
        ContentValues values=new ContentValues();
        values.put("BESTSCORE", persent);
        db.update(PLAN,values,"BOOKID = ? and LIST= ?",new String[]{table,list});
    }
    public void setReviewTime(Position position){
        dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        String table=position.getTable();
        String list=position.getList();
        Cursor cursor=db.query(PLAN, new String[]{"REVIEW_TIMES"}, "BOOKID=? and LIST=?", new String[]{table, list}, null, null, null);
        cursor.moveToNext();
        int times=Integer.parseInt(cursor.getString(cursor.getColumnIndex("REVIEW_TIMES")));
        ContentValues values=new ContentValues();
        values.put("REVIEW_TIMES",(times+1)+"");
        values.put("REVIEWTIME",dateFormat.format(new Date()));
        values.put("SHOULDREVIEW",0);
        cursor.close();
        db.update(PLAN,values,"BOOKID=? and LIST=?", new String[]{table, list});
    }
    public void shouldReview(){
        dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Log.e("HELPER", "???");
        Date date=new Date();
        String today=dateFormat.format(date);
        Calendar todayCalendar=Calendar.getInstance();
        try {
            todayCalendar.setTime(dateFormat.parse(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Cursor cursor=db.query(PLAN, new String[]{"LEARN_TIME", "REVIEWTIME","REVIEW_TIMES"},"LEARN_TIME != ? and REVIEWTIME != ?", new String[]{"",today}, null, null, null);
        while (cursor.moveToNext()){
            String learnTime=cursor.getString(cursor.getColumnIndex("LEARN_TIME"));
            String reviewTime=cursor.getString(cursor.getColumnIndex("REVIEWTIME"));
            String reviewTimes=cursor.getString(cursor.getColumnIndex("REVIEW_TIMES"));
            int times=Integer.parseInt(reviewTimes);
                Date learnDate=null;
                Date reviewDate=null;
                try {
                    learnDate=dateFormat.parse(learnTime);
                    if(!reviewTime.isEmpty())
                        reviewDate=dateFormat.parse(reviewTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar learnCalendar=Calendar.getInstance();
                Calendar reviewCalendar=null;
            if (!reviewTime.isEmpty()) {
                reviewCalendar=Calendar.getInstance();
                reviewCalendar.setTime(reviewDate);
            }
            learnCalendar.setTime(learnDate);
                ContentValues values=new ContentValues();
                if(needReview(todayCalendar,learnCalendar,reviewCalendar,times)){
                    Log.e("1or0", "wozhixc");
                    values.put("SHOULDREVIEW", 1);
                    Log.e("1or0", learnTime+reviewTime);
                    db.update(PLAN, values, "LEARN_TIME=? and REVIEWTIME=?", new String[]{learnTime,reviewTime});
                }else {
                    values.put("SHOULDREVIEW",0);
                    db.update(PLAN,values,"LEARN_TIME=? and REVIEWTIME=?",new String[]{learnTime,reviewTime});
                }
        }
        cursor.close();
    }
    private boolean needReview(Calendar todayCalendar, Calendar learnCalendar,Calendar reviewTime,int reviewTimes) {
        dateFormat=new SimpleDateFormat("yyyy/MM/dd");
//        double dayCount=(todayCalendar.getTimeInMillis()-learnCalendar.getTimeInMillis())/(24*60*60*1000);
//        if(dayCount==1.0||dayCount==2.0||dayCount==4.0||dayCount==7.0||dayCount==15.0){
////            return true;
//        }
//        Calendar calendar=Calendar.getInstance();
//        learnCalendar.add(Calendar.DAY_OF_MONTH,1);
//        if(!calendar.before(learnCalendar)) {
//            Calendar first = calendar;
//            double dayCount1 = (todayCalendar.getTimeInMillis() - first.getTimeInMillis()) / (24 * 60 * 60 * 1000);
//            if (dayCount1 == 0.0 || dayCount1 == 1.0 || dayCount1 == 3.0 || dayCount1 == 4.0 || dayCount1 == 14.0) {
//                return true;
//            }
//        }
        /**复习0次
         * 1.学习时间加1天；
         * 2.今天》=1
         * 3.第一个复习天为今天
         * 4.该复习的天数为今天，+1，+3，+6，+14
         *
         * 复习1次了
         * 1.复习时间加一
         * 2.今天》=1
         * 3.第二个复习天为今天
         * 4.该复习的天数为今天，+2，+5，+13
         *
         * 复习2次了
         * 1.复习时间加2天
         * 2.今天》=1
         * 3.第三个复习天为今天
         * 4.该复习的天数为今天，+3，+11；
         *
         * 复习3次了
         * 1.复习时间加+3天
         * 2.今天》=1
         * 3.第四个复习天为今天
         * 4.复习时间为今天，+8；
         *
         * 复习四次了
         * 1.复习时间加8天；
         * 2.今天》=1
         * 3第五个复习天为今天
         * 4.该复习的为今天
         *
         *
         * 思路：判断复习次数，获得今天的日期new date，比较；
         *
         */
        Date date=new Date();
        String crt=dateFormat.format(date);
        Calendar crtCalendar=Calendar.getInstance();
        try {
            crtCalendar.setTime(dateFormat.parse(crt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (reviewTimes){
            case 0:
                Calendar first;
                learnCalendar.add(Calendar.DAY_OF_MONTH,1);
                if(!crtCalendar.before(learnCalendar)){
                    first=crtCalendar;
                }else {
                    first=learnCalendar;
                }
                double dayCount1 = (todayCalendar.getTimeInMillis() - first.getTimeInMillis()) / (24 * 60 * 60 * 1000);
                if (dayCount1 == 0.0 || dayCount1 == 1.0 || dayCount1 == 3.0 || dayCount1 == 6.0 || dayCount1 == 14.0) {
                    return true;
                }
                break;
            case 1:
                Calendar second;
                reviewTime.add(Calendar.DAY_OF_MONTH,1);
                if(!crtCalendar.before(reviewTime)){
                    second=crtCalendar;
                }else {
                    second=reviewTime;
                }
                double dayCount2 = (todayCalendar.getTimeInMillis() - second.getTimeInMillis()) / (24 * 60 * 60 * 1000);
                if (dayCount2 == 0.0 || dayCount2 == 2.0 || dayCount2 == 5.0 || dayCount2 == 13.0 ) {
                    return true;
                }
                break;
            case 2:
                Calendar third;
                reviewTime.add(Calendar.DAY_OF_MONTH,2);
                if(!crtCalendar.before(reviewTime)){
                    third=crtCalendar;
                }else {
                    third=reviewTime;
                }
                double dayCount3 = (todayCalendar.getTimeInMillis() - third.getTimeInMillis()) / (24 * 60 * 60 * 1000);
                if (dayCount3 == 0.0 || dayCount3 == 3.0 || dayCount3 == 11.0) {
                    return true;
                }
                break;
            case 3:
                Calendar forth;
                reviewTime.add(Calendar.DAY_OF_MONTH,3);
                if(!crtCalendar.before(reviewTime)){
                    forth=crtCalendar;
                }else {
                    forth=reviewTime;
                }
                double dayCount4 = (todayCalendar.getTimeInMillis() - forth.getTimeInMillis()) / (24 * 60 * 60 * 1000);
                if (dayCount4 == 0.0 || dayCount4 == 8.0) {
                    return true;
                }
                break;
            case 4:
                Calendar fifth;
                reviewTime.add(Calendar.DAY_OF_MONTH,8);
                if(!crtCalendar.before(reviewTime)){
                    fifth=crtCalendar;
                }else {
                    fifth=reviewTime;
                }
                double dayCount5 = (todayCalendar.getTimeInMillis() - fifth.getTimeInMillis()) / (24 * 60 * 60 * 1000);
                if (dayCount5 == 0.0) {
                    return true;
                }
                break;
        }
        return false;
    }
    public List<Position> getReviewList(String table){
        dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        List<Position> list=new ArrayList<>();
        Cursor cursor=db.query(PLAN,new String[]{"LIST","REVIEW_TIMES","REVIEWTIME"},"BOOKID=? and SHOULDREVIEW=1",new String[]{table},null
        ,null,null);
        while (cursor.moveToNext()){
            String lists=cursor.getString(cursor.getColumnIndex("LIST"));
            String review_times=cursor.getString(cursor.getColumnIndex("REVIEW_TIMES"));
            String reviewTime=cursor.getString(cursor.getColumnIndex("REVIEWTIME"));
            Position position=new Position(table,lists);
            position.setReview_Times(review_times);
            position.setReviewTime(reviewTime);
            list.add(position);
        }
        cursor.close();
        return list;
    }
    public String getReviewString(String table,String time){
        dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        String reviewString="";
        Date date= null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar todayCalendar=Calendar.getInstance();
            todayCalendar.setTime(date);
            Cursor cursor=db.query(PLAN,new String[]{"LIST","LEARN_TIME","REVIEWTIME","REVIEW_TIMES"},
                    "BOOKID = ? and LEARN_TIME != ? and REVIEWTIME != ?",
                    new String[]{table,"", time},null,null,null);
            while (cursor.moveToNext()){
                String reviewTime=cursor.getString(cursor.getColumnIndex("REVIEWTIME"));
                String learnTime=cursor.getString(cursor.getColumnIndex("LEARN_TIME"));
                String reviewTimes=cursor.getString(cursor.getColumnIndex("REVIEW_TIMES"));
                Date learnDate= null;
                Date reviewDate=null;
                try {
                    learnDate = dateFormat.parse(learnTime);
                    if(!reviewTime.isEmpty())
                        reviewDate=dateFormat.parse(reviewTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int times=Integer.parseInt(reviewTimes);
                Calendar learnCalendar=Calendar.getInstance();
                Calendar reviewCalendar=null;
                if(!reviewTime.isEmpty()){
                    reviewCalendar=Calendar.getInstance();
                    reviewCalendar.setTime(reviewDate);
                }
                learnCalendar.setTime(learnDate);
                if(needReview(todayCalendar,learnCalendar,reviewCalendar,times)){
                    reviewString+="LIST-"+cursor.getString(cursor.getColumnIndex("LIST"))+" ";
                }
            }
        cursor.close();
       return reviewString;
    }
    public boolean finishReview(Position position){
        String table=position.getTable();
        String list=position.getList();
        Cursor cursor=db.query(PLAN,null,"BOOKID = ? and LIST = ? and REVIEW_TIMES >= ?",new String[]{table,list,"5"},null,null,null);
       int count= cursor.getCount();
        cursor.close();

        return  count==0?false:true;
    }
    public boolean isNeedReview(Position position){
        String table=position.getTable();
        String list=position.getList();
        Cursor cursor=db.query(PLAN,null,"BOOKID = ? and LIST = ? and SHOULDREVIEW = 1",new String[]{table,list},null,null,null);
        int count= cursor.getCount();
        cursor.close();

        return  count==0?false:true;
    }
    public void copy() {
        FileOutputStream outputStream=null;
        try {
            File file=new File("/data/data/com.lee.words/databases/mydatabase.db");
            File dir=new File("data/data/com.lee.words/databases");
            if (!dir.exists()){
                dir.mkdir();
                file.createNewFile();
            }else{
                return;
            }
            inputStream=context.getResources().openRawResource(R.raw.wordorid);
            outputStream=new FileOutputStream(file);
            byte[] bytes=new byte[1024];
            int num;
            while ((num=inputStream.read(bytes))!=-1){
                outputStream.write(bytes, 0, num);
                outputStream.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(outputStream!=null)
                    outputStream.close();
                if(inputStream!=null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

