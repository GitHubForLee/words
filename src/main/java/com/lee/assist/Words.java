package com.lee.assist;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/14.
 */
public class Words implements Serializable{
    private String number;
    private String spelling;
    private String meanning;
    private String fayin;
    private String list;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSpelling() {
        return spelling;
    }

    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }

    public String getMeanning() {
        return meanning;
    }

    public void setMeanning(String meanning) {
        this.meanning = meanning;
    }

    public String getFayin() {
        return fayin;
    }

    public void setFayin(String fayin) {
        this.fayin = fayin;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public Words(String number, String spelling, String meanning, String fayin) {

        this.number = number;
        this.spelling = spelling;
        this.meanning = meanning;
        this.fayin = fayin;
        this.list="ATTENTION";
    }

    public Words(String number,String spelling, String meanning) {
        this.spelling = spelling;
        this.meanning = meanning;
        this.number=number;
    }
}
