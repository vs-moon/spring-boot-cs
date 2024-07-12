package org.xiao.cs.db.box.service.business;

public interface BusinessService<T> extends
        BusinessServiceQuery<T>,
        BusinessServiceInsert<T>,
        BusinessServiceDelete,
        BusinessServiceUpdate<T>,
        BusinessServiceValid {}
