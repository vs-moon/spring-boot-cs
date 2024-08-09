package org.xiao.cs.db.box.norm.among;

import java.util.List;

public interface AmongServiceInsert<T> {
    default int insertOne(T record) {
        return 0;
    }

    default int insertMany(List<T> record) {
        return 0;
    }
}
