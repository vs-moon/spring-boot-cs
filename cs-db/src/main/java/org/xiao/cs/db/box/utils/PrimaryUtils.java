package org.xiao.cs.db.box.utils;

import cn.hutool.core.util.IdUtil;
import org.xiao.cs.common.box.utils.SpringUtils;
import org.xiao.cs.db.box.properties.DBProperties;

public class PrimaryUtils {

    public static final DBProperties DB_PROPERTIES = SpringUtils.getBean(DBProperties.class);

    public static long snow() {
        return IdUtil.getSnowflakeNextId();
    }

    public static long snowDcs() {
        DBProperties.DSCProperties dcs = DB_PROPERTIES.getDcs();
        return dcs.anyNull() ?
                snow() : IdUtil.getSnowflake(dcs.getWorkerId(), dcs.getDatacenterId()).nextId();
    }
    public static String uuid() {
        return IdUtil.fastSimpleUUID();
    }
}
