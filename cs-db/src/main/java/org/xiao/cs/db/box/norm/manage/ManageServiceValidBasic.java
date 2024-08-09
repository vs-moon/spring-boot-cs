package org.xiao.cs.db.box.norm.manage;

import org.xiao.cs.common.box.domain.ArgsState;

public interface ManageServiceValidBasic<By, To> {
    default int validOne(ArgsState<By, To> record) {
        return 0;
    }

    default int validMany(ArgsState.Many<By, To> record) {
        return 0;
    }
}
