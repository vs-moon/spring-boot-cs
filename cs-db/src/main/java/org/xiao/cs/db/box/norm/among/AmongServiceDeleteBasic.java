package org.xiao.cs.db.box.norm.among;

import java.util.List;

public interface AmongServiceDeleteBasic<By> {
    default int deleteOne(By record) {
        return 0;
    }

    default int deleteMany(List<By> record) {
        return 0;
    }
}
