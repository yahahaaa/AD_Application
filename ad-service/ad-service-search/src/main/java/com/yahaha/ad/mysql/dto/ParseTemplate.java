package com.yahaha.ad.mysql.dto;

import com.yahaha.ad.mysql.constant.OpType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @Auther LeeMZ
 * @Date 2021/2/14
 **/
@Data
public class ParseTemplate {

    private String database;

    /** <tableName, TableTemplate> */
    private Map<String, TableTemplate> tableTemplateMap = new HashMap<>();

    /**
     * <p> template.json 文件中存储了我们需要关心的数据库表名以及字段
     * template.json 对应了 Template.class, 项目启动时 TemplateHolder 会将 template.json 内容映射为一个Template对象
     *
     * <p> parse方法将 Template 对象映射为多个 TableTemplate 存储在ParseTemplate的Map集合中
     *
     * @param _template 模板内容映射对象
     * @return
     */
    public static ParseTemplate parse(Template _template){

        ParseTemplate parseTemplate = new ParseTemplate();
        parseTemplate.setDatabase(_template.getDatabase());

        // 一个Template对应一个数据库，一个JsonTable对应一个表
        for (JsonTable table : _template.getJsonTable()) {
            String name = table.getTableName();
            Integer level = table.getLevel();

            // 填充tableTemplate类中属性（一个表对应一个tableTemplate）
            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTableName(name);
            tableTemplate.setLevel(level.toString());
            Map<OpType, List<String>> opTypeFieldSetMap = tableTemplate.getOpTypeFieldSetMap(); //获取一个空集合，然后再向集合中添加内容
            // 向tableTemplate.opTypeFieldSetMap属性集合中添加键值对 -- <操作类型，List<column>>
            for (JsonTable.Column column : table.getInsert()) {
                getAndCreateIfNeed(
                        OpType.ADD,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
            for (JsonTable.Column column : table.getUpdate()) {
                getAndCreateIfNeed(
                        OpType.UPDATE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
            for (JsonTable.Column column : table.getDelete()) {
                getAndCreateIfNeed(
                        OpType.DELETE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }

            parseTemplate.tableTemplateMap.put(name,tableTemplate);
        }

        return null;
    }

    private static <T,R> R getAndCreateIfNeed(T key, Map<T,R> map, Supplier<R> factory){
        return map.computeIfAbsent(key, k->factory.get());
    }
}
