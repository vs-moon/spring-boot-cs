package org.xiao.cs.db.box.context;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.xiao.cs.db.box.utils.DynamicUtils;

public class DynamicRouting extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey () {
        return DynamicUtils.get();
    }
}
