package com.qi.market.common.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Author:liuqi
 * Date:2017/11/29 14:43
 * Detail:在方法上注解，表示为更新方法
 */
@Target(ElementType.METHOD)
public @interface UPDATE {
    String value();
}
