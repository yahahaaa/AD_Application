package com.yahaha.ad.mysql.dto;

import com.yahaha.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther LeeMZ
 * @Date 2021/2/15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MySqlRowData {

    private String tableName;
    private String level;
    private OpType opType;
    private List<Map<String,String>> fieldValueMap = new ArrayList<>();
}
