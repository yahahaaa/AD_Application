package com.yahaha.ad.sender;

import com.yahaha.ad.mysql.dto.MySqlRowData;

public interface ISender {

    void sender(MySqlRowData rowData);
}
