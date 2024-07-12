package org.xiao.cs.db.box.service.business;

import java.util.List;

public interface BusinessServiceUpdate<T> {
    default int updateOne(T record) {
        return 0;
    }
    default int updateMany(List<T> record) {
        return 0;
    }
}
