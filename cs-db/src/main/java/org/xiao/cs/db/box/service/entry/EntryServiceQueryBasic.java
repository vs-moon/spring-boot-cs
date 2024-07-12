package org.xiao.cs.db.box.service.entry;

import com.github.pagehelper.Page;
import org.xiao.cs.common.box.domain.CommonRequest;
import org.xiao.cs.common.box.domain.CommonRequestPaging;

import java.util.List;

public interface EntryServiceQueryBasic<T, By> {
    default T selectOne(CommonRequest<By> record) {
        return null;
    }

    default List<T> selectMany(CommonRequest<T> record) {
        return null;
    }

    default Page<T> selectPage(CommonRequestPaging<T> record) {
        return null;
    }
}
