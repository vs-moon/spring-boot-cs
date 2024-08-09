package org.xiao.cs.db.box.norm.manage;

import java.util.List;

public interface ManageServiceInsert<T> {
    default int insertOne(T record) {
        return 0;
    }

    default int insertMany(List<T> record) {
        return 0;
    }
}
