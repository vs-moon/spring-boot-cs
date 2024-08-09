package org.xiao.cs.db.box.norm.face;

import org.xiao.cs.common.box.domain.ArgsState;
import org.xiao.cs.common.box.domain.CommonRequest;

public interface FaceServiceValidBasic<By, To> {
    default int validOne(CommonRequest<ArgsState<By, To>> record) {
        return 0;
    }

    default int validMany(CommonRequest<ArgsState.Many<By, To>> record) {
        return 0;
    }
}
