package com.dangdang.gx.ui.http;

/**
 * Created by wanghaiming on 2016/1/15.
 */
public class DangError extends RuntimeException {

    private int mCode;
    private String mReason;

    public DangError(int code, String reason) {
        super(null, null);
        mCode = code;
        mReason = reason;
    }

    public int getmCode() {
        return mCode;
    }

    public String getmReason() {
        return mReason;
    }

    public String toString() {
        return "DangError code:" + mCode + " message:" + mReason;
    }

}
