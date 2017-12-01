package com.qi.market.common.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by Qi on 2017/12/1.
 * 标记参数类型是记录类型
 * 例如：Observable <T> update(@Record T[] obj)表示更新数据库多条记录的所有列
 *      Observable <T> update(@ColumnName T[] obj)表示更新数据库多条记录的某个列
 */
@Target(ElementType.PARAMETER)
public @interface Record {
}
