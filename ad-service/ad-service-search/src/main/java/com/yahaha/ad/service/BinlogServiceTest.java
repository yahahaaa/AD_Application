package com.yahaha.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

import java.io.IOException;

/**
 * @Auther LeeMZ
 * @Date 2021/2/14
 **/
public class BinlogServiceTest {

    public static void main(String[] args) throws IOException {

        BinaryLogClient client = new BinaryLogClient(
                "192.168.0.103",
                3306,
                "root",
                "123456"
        );

        //client.setBinlogFilename(); 设置监听哪一个binlog文件
        //client.setBinlogPosition(); 设置监听从哪个位置开始监听

        client.registerEventListener(event -> {
            EventData data = event.getData();
            //如果是更新数据行的EventData
            if (data instanceof UpdateRowsEventData){
                System.out.println("Update ------------");
                System.out.println(data.toString());
            } else if (data instanceof WriteRowsEventData){
                System.out.println("Write -------------");
                System.out.println(data.toString());
            } else if (data instanceof DeleteRowsEventData){
                System.out.println("Delete -------------");
                System.out.println(data.toString());
            }
        });

        client.connect();//连接
    }
}
