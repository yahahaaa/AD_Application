package com.yahaha.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.yahaha.ad.mysql.dto.MySqlRowData;
import com.yahaha.ad.sender.ISender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Auther LeeMZ
 * @Date 2021/2/19
 **/
@Component("kafkaSender")
@SuppressWarnings("all")
public class KafkaSender implements ISender {

    @Value("${adconf.kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public void sender(MySqlRowData rowData) {
        kafkaTemplate.send(topic, JSON.toJSONString(rowData));
    }

    @KafkaListener(topics = {"ad-search-mysql-data"},groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?,?> record){

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()){
            Object message = kafkaMessage.get();
            MySqlRowData rowData = JSON.parseObject(message.toString(), MySqlRowData.class);
            System.out.println(String.format("kafka processMysqlRowData: {}",JSON.toJSONString(rowData)));
        }
    }
}
