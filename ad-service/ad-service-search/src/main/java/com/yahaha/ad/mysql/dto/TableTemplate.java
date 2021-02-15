package com.yahaha.ad.mysql.dto;

import com.yahaha.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther LeeMZ
 * @Date 2021/2/14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableTemplate {

    private String tableName;
    private String level;

    /** <操作类型 如 ADD , column> */
    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();

    /** <Binlog中字段名用 Integer 数字表示，字段对应的中文名> */
    private Map<Integer, String> posMap = new HashMap<>();

}
