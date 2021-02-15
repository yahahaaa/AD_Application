package com.yahaha.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Auther LeeMZ
 * @Date 2021/2/14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinlogRowData {

    private TableTemplate table;

    private EventType eventType;

    private List<Map<String,String>> after;

    private List<Map<String,String>> before;
}
