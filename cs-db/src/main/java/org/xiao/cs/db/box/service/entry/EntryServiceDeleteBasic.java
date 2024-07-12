package org.xiao.cs.db.box.service.entry;

import org.xiao.cs.common.box.domain.CommonRequest;

import java.util.List;

public interface EntryServiceDeleteBasic<By> {
    default int deleteOne(CommonRequest<By> record) {
        return 0;
    }

    default int deleteMany(CommonRequest<List<By>> record) {
        return 0;
    }
}
