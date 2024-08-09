package org.xiao.cs.db.box.norm.among;

public interface AmongService<T> extends
        AmongServiceQuery<T>,
        AmongServiceInsert<T>,
        AmongServiceDelete,
        AmongServiceUpdate<T>,
        AmongServiceValid {}
