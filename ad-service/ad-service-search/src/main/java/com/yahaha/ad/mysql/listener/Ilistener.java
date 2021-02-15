package com.yahaha.ad.mysql.listener;

import com.yahaha.ad.mysql.dto.BinlogRowData;

@SuppressWarnings("all")
/**
 * 我们可能不光有用于向消息中间件投递消息触发增量索引的监听器
 * 还可能有其他用途的监听器需要注册到融合监听器中
 */
public interface Ilistener {

    void register();

    void onEvent(BinlogRowData eventData);
}
