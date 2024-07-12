package org.xiao.cs.db.box.service.business;

import com.github.pagehelper.Page;

import java.util.List;

public interface BusinessServiceQueryBasic<T, By> {
    default T selectOne(By record) {
        return null;
    }

    default List<T> selectMany(T record) {
        return null;
    }

    default Page<T> selectPage(T record, int pageNum, int pageSize) {
        return null;
    }
}
