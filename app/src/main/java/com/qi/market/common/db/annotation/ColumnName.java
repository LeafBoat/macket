package com.qi.market.common.db.annotation;

import android.support.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Author:liuqi
 * Date:2017/11/29 17:53
 * Detail:ColumnName的value对应数据库的列名；
 */
@Target(value = {ElementType.FIELD,ElementType.PARAMETER})
public @interface ColumnName {
    @NonNull
    String value();
}
