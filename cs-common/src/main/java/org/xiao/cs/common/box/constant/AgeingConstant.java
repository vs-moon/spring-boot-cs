package org.xiao.cs.common.box.constant;

public class AgeingConstant {
    public static final Integer UNIT_BASE = 1000;
    public static final Integer UNIT_TIMES_60 = 60;
    public static final Integer UNIT_TIMES_24 = 24;
    public static final Integer UNIT_TIMES_7 = 7;
    public static final Integer UNIT_MIN = UNIT_BASE * UNIT_TIMES_60;
    public static final Integer UNIT_HOUR = UNIT_MIN * UNIT_TIMES_60;
    public static final Integer UNIT_DAY = UNIT_HOUR * UNIT_TIMES_24;
    public static final Integer UNIT_WEEK = UNIT_DAY * UNIT_TIMES_7;
}
