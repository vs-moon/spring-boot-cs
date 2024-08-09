package org.xiao.cs.db.box.norm.among;

import java.util.List;

public interface AmongServiceUpdate<T> {
    default int updateOne(T record) {
        return 0;
    }
    default int updateMany(List<T> record) {
        return 0;
    }
}
