package org.xiao.cs.db.box.norm.face;

import com.github.pagehelper.PageInfo;
import org.xiao.cs.common.box.domain.CommonRequest;
import org.xiao.cs.common.box.domain.CommonRequestPaging;

import java.util.List;

public interface FaceServiceQueryBasic<T, By> {
    default T selectOne(CommonRequest<By> record) {
        return null;
    }

    default List<? extends T> selectMany(CommonRequest<T> record) {
        return null;
    }

    default List<? extends T> selectLazy(CommonRequest<T> record) {
        return null;
    }

    default PageInfo<? extends T> selectPage(CommonRequestPaging<T> record) {
        return null;
    }
}
