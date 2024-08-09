package org.xiao.cs.db.box.norm.manage;

public interface ManageService<T> extends
        ManageServiceQuery<T>,
        ManageServiceInsert<T>,
        ManageServiceDelete,
        ManageServiceUpdate<T>,
        ManageServiceValid {}
