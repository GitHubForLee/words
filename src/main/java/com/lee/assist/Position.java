package com.lee.assist;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/14.
 */
public class Position implements Serializable{
    private String table;
    private String list;
    private String reviewTime;
    private boolean needReviewed;
    private boolean finishReview;
    private boolean islearned;

    public boolean isFinishReview() {
        return finishReview;
    }

    public void setFinishReview(boolean finishReview) {
        this.finishReview = finishReview;
    }

    public boolean isNeedReviewed() {
        return needReviewed;
    }

    public void setNeedReviewed(boolean needReviewed) {
        this.needReviewed = needReviewed;
    }

    public boolean islearned() {
        return islearned;
    }

    public void setIslearned(boolean islearned) {
        this.islearned = islearned;
    }

    public String getReview_Times() {
        return review_Times;
    }

    public void setReview_Times(String review_Times) {
        this.review_Times = review_Times;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    private String review_Times;
    public Position(String table, String list) {
        this.table = table;
        this.list = list;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }
}
