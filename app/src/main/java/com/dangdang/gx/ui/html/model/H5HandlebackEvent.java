package com.dangdang.gx.ui.html.model;

/**
 */

public class H5HandlebackEvent {

    public final static int H5_BACKSTATE_OK = 1;

    public int h5BackState;

    public boolean handleBackState(){
        return h5BackState == H5_BACKSTATE_OK;
    }

}
