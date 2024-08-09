package org.xiao.cs.db.box.norm.face;

import org.xiao.cs.common.box.domain.CommonRequest;

import java.util.List;

public interface FaceServiceInsert<T> {
    default int insertOne(CommonRequest<T> record) {
        return 0;
    }

    default int insertMany(CommonRequest<List<T>> record) {
        return 0;
    }
}
