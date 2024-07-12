package org.xiao.cs.db.box.service.entry;

public interface EntryService<T> extends
        EntryServiceQuery<T>,
        EntryServiceInsert<T>,
        EntryServiceDelete,
        EntryServiceUpdate<T>,
        EntryServiceValid {}
