package org.xiao.cs.common.box.enumerate;

import java.util.Calendar;

public enum CalendarMapping {

    YEAR("YEAR", Calendar.YEAR),
    MONTH("MONTH", Calendar.MONTH),
    DAY("DAY", Calendar.DAY_OF_YEAR),
    HOUR("HOUR", Calendar.HOUR),
    MIN("MIN", Calendar.MINUTE),
    SECOND("SECOND", Calendar.SECOND),
    WEEK("WEEK", Calendar.WEEK_OF_YEAR);

    private final String code;
    private final Integer unit;

    CalendarMapping(String code, Integer unit) {
        this.code = code;
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public Integer getUnit() {
        return unit;
    }
}
