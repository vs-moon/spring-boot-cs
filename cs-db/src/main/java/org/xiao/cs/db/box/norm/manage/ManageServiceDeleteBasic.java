package org.xiao.cs.db.box.norm.manage;

import java.util.List;

public interface ManageServiceDeleteBasic<By> {
    default int deleteOne(By record) {
        return 0;
    }

    default int deleteMany(List<By> record) {
        return 0;
    }
}
