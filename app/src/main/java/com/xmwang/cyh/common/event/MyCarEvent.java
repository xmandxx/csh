package com.xmwang.cyh.common.event;

/**
 * Created by xmwang on 2018/1/20.
 */

public class MyCarEvent {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    private int tag;
    public MyCarEvent(int id,int tag){
        this.tag = tag;
        this.id = id;
    }
}
