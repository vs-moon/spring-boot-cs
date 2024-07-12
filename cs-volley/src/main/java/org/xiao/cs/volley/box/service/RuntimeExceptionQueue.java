package org.xiao.cs.volley.box.service;

public interface RuntimeExceptionQueue {
    void capture (RuntimeException runtimeException) throws RuntimeException;
}
