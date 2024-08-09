package org.xiao.cs.db.box.norm.among;

import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AmongServiceQueryBasic<T, By> {
    default T selectOne(By record) {
        return null;
    }

    default List<? extends T> selectMany(T record) {
        return null;
    }

    default List<? extends T> selectLazy(T record) {
        return null;
    }

    default PageInfo<? extends T> selectPage(T record, int pageNum, int pageSize) {
        return null;
    }
}
