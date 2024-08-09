package org.xiao.cs.db.box.norm.manage;

import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ManageServiceQueryBasic<T, By> {
    default T selectOne(By record) {
        return null;
    }

    default List<T> selectMany(T record) {
        return null;
    }

    default List<T> selectLazy(T record) {
        return null;
    }

    default PageInfo<T> selectPage(T record, int pageNum, int pageSize) {
        return null;
    }
}
