package org.xiao.cs.db.box.norm.manage;

import java.util.List;

public interface ManageServiceUpdate<T> {
    default int updateOne(T record) {
        return 0;
    }
    default int updateMany(List<T> record) {
        return 0;
    }
}
