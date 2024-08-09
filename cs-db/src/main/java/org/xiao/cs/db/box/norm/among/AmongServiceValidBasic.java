package org.xiao.cs.db.box.norm.among;

import org.xiao.cs.common.box.domain.ArgsState;

public interface AmongServiceValidBasic<By, To> {
    default int validOne(ArgsState<By, To> record) {
        return 0;
    }

    default int validMany(ArgsState.Many<By, To> record) {
        return 0;
    }
}
