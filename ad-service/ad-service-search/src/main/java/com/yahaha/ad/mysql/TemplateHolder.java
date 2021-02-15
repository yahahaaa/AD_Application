package com.yahaha.ad.mysql;

import com.alibaba.fastjson.JSON;
import com.yahaha.ad.mysql.constant.OpType;
import com.yahaha.ad.mysql.dto.ParseTemplate;
import com.yahaha.ad.mysql.dto.TableTemplate;
import com.yahaha.ad.mysql.dto.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Auther LeeMZ
 * @Date 2021/2/14
 **/
@Component
@Slf4j
public class TemplateHolder {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ParseTemplate parseTemplate;

    /** 通过自定义 SQL 语句查询数据库名、表明、字段名与字段名对应的索引 */
    private String SQL_SCHEMA = "select table_schema, table_name, " +
            "column_name, ordinal_position from information_schema.columns " +
            "where table_schema = ? and table_name = ?";


    @PostConstruct
    private void init(){
        loadJson("template.json");
    }

    /**
     * 对外提供接口获取 ParseTemplate
     * @param tableName
     * @return
     */
    public TableTemplate getTable(String tableName){
        return parseTemplate.getTableTemplateMap().get(tableName);
    }

    /**
     * 加载resource目录下 template.json 文件映射为一个Template对象
     * Template对象映射为 ParseTemplate 对象
     * @param path
     */
    private void loadJson(String path){

        //使用当前线程的classLoad加载资源文件可以防止不同环境下由于累加器不同导致的错误
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream inStream = cl.getResourceAsStream(path);

        try {
            // 将 Template.json 解析为 Template 对象
            Template template = JSON.parseObject(
                    inStream,
                    Charset.defaultCharset(),
                    Template.class
            );
            // 将 Template对象 解析为 ParseTemplate 对象
            this.parseTemplate = ParseTemplate.parse(template);
            // 填充 TableTemplate 中字段名与字段索引的键值对集合
            loadMeta();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("fail to parse json file");
        }
    }

    /**
     * 通过 SQL 语句获取不同数据表对应的 TableTemplate 中 字段与字段索引 集合信息
     */
    private void loadMeta(){

        //TableTemplateMap() ---> 键值对为： <TableName, TableTemplate>
        for (Map.Entry<String, TableTemplate> entry : parseTemplate.getTableTemplateMap().entrySet()) {

            TableTemplate table = entry.getValue();

            // OpTypeFieldSetMap() ---> 键值对： <OpType（操作类型）, column（字段名）>
            List<String> insertFields = table.getOpTypeFieldSetMap().get(OpType.ADD);
            List<String> updateFields = table.getOpTypeFieldSetMap().get(OpType.UPDATE);
            List<String> deleteFields = table.getOpTypeFieldSetMap().get(OpType.DELETE);

            // 第三个参数用于返回自定义对象类型，但是我们不需要返回任何类型，
            // 只需要从ResultSet结合中以流的形式获取字段名和索引然后赋值给table即可
            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    parseTemplate.getDatabase(),table.getTableName()
            }, (rs, i) -> {

                int pos = rs.getInt("ORDINAL_POSITION");
                String colName = rs.getString("COLUMN_NAME");

                // 判断是否为我们关心的字段，不关心的直接省略
                if ((null != updateFields && updateFields.contains(colName))
                    || (null != insertFields && insertFields.contains(colName))
                    || (null != deleteFields && deleteFields.contains(colName))){
                    table.getPosMap().put(pos - 1, colName);
                }
                return null;
            });

            // 非流的方式处理
//            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
//                    parseTemplate.getDatabase(), table.getTableName()
//            }, new RowMapper<Object>() {
//                @Override
//                public Object mapRow(ResultSet resultSet, int i) throws SQLException {
//
//                    int pos = resultSet.getInt("ORDINAL_POSITION");
//                    String colName = resultSet.getString("COLUMN_NAME");
//
//                    // 判断是否为我们关心的字段，不关心的直接省略
//                    if ((null != updateFields && updateFields.contains(colName))
//                            || (null != insertFields && insertFields.contains(colName))
//                            || (null != deleteFields && deleteFields.contains(colName))){
//                        table.getPosMap().put(pos - 1, colName);
//                    }
//                    return null;
//                }
//            });


        }
    }
}
