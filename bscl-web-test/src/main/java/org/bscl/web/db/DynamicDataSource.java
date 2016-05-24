package org.bscl.web.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * User: shijingui
 * Date: 2016/5/23
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHolder.getDataSouce();
    }
}
