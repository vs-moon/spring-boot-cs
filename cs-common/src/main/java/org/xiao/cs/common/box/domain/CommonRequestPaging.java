package org.xiao.cs.common.box.domain;

import org.xiao.cs.common.box.utils.SpringUtils;

public class CommonRequestPaging<T> extends CommonRequest<T> {

    private Paging paging;

    public CommonRequestPaging() {
        super();
    }

    public CommonRequestPaging(String sign, T args, Paging paging) {
        super(sign, args);
        this.paging = paging;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public static <T> CommonRequestPaging<T> builder(T args, Paging paging) {
        return new CommonRequestPaging<>(SpringUtils.getApplicationName(), args, paging);
    }

    public static class Paging {
        private int pageNum = 1;
        private int pageSize = 10;

        public Paging() {}
        public Paging(int pageNum, int pageSize) {
            this.pageNum = pageNum;
            this.pageSize = pageSize;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }
}
