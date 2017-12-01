package com.qi.market.common.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by Qi on 2017/11/30.
 * 查询或更新等操作时，Condition的value对应数据库列名。不过该列名是作为限定条件用的。例如：where id=1234，这时方法参数应该要加上Condition，Observable<List<Object>> query(@Condition("id") int id)
 */
@Target(ElementType.PARAMETER)
public @interface Condition {
    String value();
}
