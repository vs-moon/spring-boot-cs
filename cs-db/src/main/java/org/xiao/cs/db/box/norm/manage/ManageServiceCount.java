package org.xiao.cs.db.box.norm.manage;

public interface ManageServiceCount<T, By> {
    default long countByColumns(T record) {
        return 0;
    }

    default long countByKey(By record) {
        return 0;
    }
}
