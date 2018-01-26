package com.xmwang.cyh.common.event;

/**
 * Created by wangxiaoming on 2018/1/27.
 */

public class UserDriverInfoEvent {
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
    public UserDriverInfoEvent(int id, int tag){
        this.tag = tag;
        this.id = id;
    }
    public UserDriverInfoEvent(int id){
        this.id = id;
    }
    public UserDriverInfoEvent(){

    }
}
