package org.xiao.cs.common.box.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionUtils {

    public static <R, T> R reBuild(T args, Function<T, R> function, Consumer<R> consumer) {
        R apply = function.apply(args);
        consumer.accept(apply);
        return apply;
    }

    public static <R, T> List<R> reBuild(List<T> args, Function<T, R> function) {
        List<R> list = new ArrayList<>(args.size());
        for (T arg : args) {
            list.add(function.apply(arg));
        }
        return list;
    }
}
