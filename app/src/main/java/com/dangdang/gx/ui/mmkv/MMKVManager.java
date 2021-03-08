package com.dangdang.gx.ui.mmkv;

import com.tencent.mmkv.MMKV;

public class MMKVManager {


    static  void setData(Object key, Object value){
        MMKV kv = MMKV.defaultMMKV();

        kv.encode("bool", true);
        boolean bValue = kv.decodeBool("bool");

        kv.encode("int", Integer.MIN_VALUE);
        int iValue = kv.decodeInt("int");

        kv.encode("string", "Hello from mmkv");
        String str = kv.decodeString("string");
    }
    static void getData(){
        MMKV kv = MMKV.defaultMMKV();




        String str = kv.decodeString("string");

    }
}
