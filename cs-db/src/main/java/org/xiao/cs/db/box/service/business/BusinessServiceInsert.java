package org.xiao.cs.db.box.service.business;

import java.util.List;

public interface BusinessServiceInsert<T> {
    default int insertOne(T record) {
        return 0;
    }

    default int insertMany(List<T> record) {
        return 0;
    }
}
