package com.yahaha.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.yahaha.ad.mysql.TemplateHolder;
import com.yahaha.ad.mysql.dto.BinlogRowData;
import com.yahaha.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 聚合监听器，当 第三方 组件监听到mysql binlog后会调用该监听器的onEvent方法，
 * 由该方法选择此时应当激活的监听器
 * @Auther LeeMZ
 * @Date 2021/2/15
 **/
@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    private String dbName;
    private String tableName;

    private Map<String,Ilistener> listenerMap = new HashMap<>();

    @Autowired
    private TemplateHolder templateHolder;

    private String genKey(String dbName, String tableName){
        return dbName + ":" + tableName;
    }

    /**
     * 用于将增量监听器注册到该融合监听器中
     * @param _dbName
     * @param _tableName
     * @param listener
     */
    public void register(String _dbName, String _tableName, Ilistener listener){
        log.info("register: {}-{}",_dbName,_tableName);
        this.listenerMap.put(genKey(_dbName,_tableName),listener);
    }

    @Override
    public void onEvent(Event event) {

        EventType type = event.getHeader().getEventType();
        log.debug("event type: {}", type);

        // 在增删改操作前，会先有TableMap用来记录当前表和数据库的名字
        if (type == EventType.TABLE_MAP){
            TableMapEventData data = event.getData();
            this.tableName = data.getTable();
            this.dbName = data.getDatabase();
            return;
        }

        // 除了增删改操作，其他的binlog都不需要关心
        if (type != EventType.EXT_UPDATE_ROWS &&
            type != EventType.EXT_WRITE_ROWS &&
            type != EventType.EXT_DELETE_ROWS){
            return;
        }

        // 表名和库名是否已经完成填充
        if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)){
            log.error("no meta data event");
            return;
        }

        // 找出对应表有兴趣的监听器
        String key = genKey(this.dbName,this.tableName);
        Ilistener ilistener = this.listenerMap.get(key);
        // 如果监听器为空，说明是我们不关心的数据库表,直接结束方法
        if (null == ilistener){
            log.debug("skip {}",key);
            return;
        }

        log.info("trigger event: {}",type.name());

        try{
            BinlogRowData rowData = buildRowData(event.getData());
            if (rowData == null){
                return;
            }

            rowData.setEventType(type);
            ilistener.onEvent(rowData);
        }catch (Exception ex){
            ex.printStackTrace();
            log.error(ex.getMessage());
        }finally {
            tableName = "";
            dbName = "";
        }
    }

    /**
     * 增加和删除数据时返回的 eventData中rows数据格式是相同的，但是update是不太一样，需要做统一格式处理
     * @param eventData
     * @return
     */
    private List<Serializable[]> getAfterValues(EventData eventData){

        if (eventData instanceof WriteRowsEventData){
            return ((WriteRowsEventData) eventData).getRows();
        }

        if (eventData instanceof DeleteRowsEventData){
            return ((DeleteRowsEventData) eventData).getRows();
        }

        if (eventData instanceof UpdateRowsEventData){
            return ((UpdateRowsEventData) eventData).getRows().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * 通过eventData构建BinlogRowData
     * @param eventData
     * @return
     */
    private BinlogRowData buildRowData(EventData eventData){

        TableTemplate table = templateHolder.getTable(tableName);
        if (table == null){
            log.warn("table {} not found",tableName);
            return null;
        }

        List<Map<String,String>> afterMapList = new ArrayList<>();
        for (Serializable[] after : getAfterValues(eventData)) {
            //<字段名，字段值>
            Map<String,String> afterMap = new HashMap<>();

            int colLen = after.length;

            for (int ix = 0; ix < colLen; ix++){

                // 取出当前位置对应的列名
                String colName = table.getPosMap().get(ix);

                // 如果没有则说明不关心这个列
                if (null == colName){
                    log.debug("ignore position: {}",ix);
                }

                String colValue = after[ix].toString();
                afterMap.put(colName,colValue);
            }

            afterMapList.add(afterMap);
        }

        BinlogRowData  rowData = new BinlogRowData();
        rowData.setAfter(afterMapList);
        rowData.setTable(table);
        return rowData;
    }
}
