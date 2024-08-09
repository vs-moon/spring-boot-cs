package org.xiao.cs.db.box.norm.face;

import org.xiao.cs.common.box.domain.CommonRequest;

import java.util.List;

public interface FaceServiceDeleteBasic<By> {
    default int deleteOne(CommonRequest<By> record) {
        return 0;
    }

    default int deleteMany(CommonRequest<List<By>> record) {
        return 0;
    }
}
