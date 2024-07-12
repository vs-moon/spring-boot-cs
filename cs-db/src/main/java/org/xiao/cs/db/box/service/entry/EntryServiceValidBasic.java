package org.xiao.cs.db.box.service.entry;

import org.xiao.cs.common.box.domain.ArgsState;
import org.xiao.cs.common.box.domain.CommonRequest;

public interface EntryServiceValidBasic<By, To> {
    default int validOne(CommonRequest<ArgsState<By, To>> record) {
        return 0;
    }

    default int validMany(CommonRequest<ArgsState.Many<By, To>> record) {
        return 0;
    }
}
