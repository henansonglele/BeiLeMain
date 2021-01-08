package com.dangdang.gx.ui.http;

import java.io.Serializable;

/**
 * Created by luchenghao on 2016/1/14.
 */
public class RequestResult<T> implements Serializable {
    public static class Status implements Serializable {
        private Status(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public Status() {
        }

        public int code;
        public String message;
    }

    public T data;
    public Status status = new RequestResult.Status(0,"");
    public long systemDate;
}
