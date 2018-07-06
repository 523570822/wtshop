package com.wtshop.api.common.result;

import java.util.Date;

/**
 * Created by sq on 2017/7/27.
 */
public class TrackResult {

    private String content;

    private String date;

    public TrackResult(String content, String date) {
        this.content = content;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
