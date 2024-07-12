package org.xiao.cs.db.box.service.entry;

import org.xiao.cs.common.box.domain.CommonRequest;

import java.util.List;

public interface EntryServiceInsert<T> {
    default int insertOne(CommonRequest<T> record) {
        return 0;
    }

    default int insertMany(CommonRequest<List<T>> record) {
        return 0;
    }
}
