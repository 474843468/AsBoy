package com.tmall.myredboy.bean;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class FeedBack {
    /**
     * message : 信息填写不完整
     * status : 204
     */

    public String message;
    public int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
