package com.dangdang.gx.ui.html.model;

import java.io.Serializable;

/**
 * Created by songxile on 18/11/1
 * <p>
 * h 5 ,定义右上角 button的icon 和按钮的响应事件 回调函数
 */

public class H5RightButtonBean implements Serializable {
    private String iconUrl;//btn 的icon
    private String action;//点击btn 回调js 函数
    private String text; //文字btn
    private float fontSize; //文字btn字体大小
    private String color; //文字btn字体颜色

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
