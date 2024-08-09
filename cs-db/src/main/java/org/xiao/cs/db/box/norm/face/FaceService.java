package org.xiao.cs.db.box.norm.face;

public interface FaceService<T> extends
        FaceServiceQuery<T>,
        FaceServiceInsert<T>,
        FaceServiceDelete,
        FaceServiceUpdate<T>,
        FaceServiceValid {}
