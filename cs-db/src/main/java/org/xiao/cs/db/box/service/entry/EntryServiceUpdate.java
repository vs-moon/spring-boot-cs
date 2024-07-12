package org.xiao.cs.db.box.service.entry;

import org.xiao.cs.common.box.domain.CommonRequest;

import java.util.List;

public interface EntryServiceUpdate<T> {
    default int updateOne(CommonRequest<T> record) {
        return 0;
    }
    default int updateMany(CommonRequest<List<T>> record) {
        return 0;
    }
}
