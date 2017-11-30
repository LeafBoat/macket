package com.qi.market.common.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by Qi on 2017/11/30.
 */
@Target(ElementType.PARAMETER)
public @interface Condition {
    String value();
}
