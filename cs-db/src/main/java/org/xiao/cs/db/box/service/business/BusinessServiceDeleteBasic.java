package org.xiao.cs.db.box.service.business;

import java.util.List;

public interface BusinessServiceDeleteBasic<By> {
    default int deleteOne(By record) {
        return 0;
    }

    default int deleteMany(List<By> record) {
        return 0;
    }
}
