package com.yahaha.ad.mysql.constant;

import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * @Auther LeeMZ
 * @Date 2021/2/13
 **/
public enum OpType {
    ADD,
    UPDATE,
    DELETE,
    OTHER;

    public static OpType to(EventType eventType){
        switch(eventType){
            case EXT_UPDATE_ROWS:
                return UPDATE;
            case EXT_WRITE_ROWS:
                return ADD;
            case EXT_DELETE_ROWS:
                return DELETE;
            default:
                return OTHER;
        }
    }
}