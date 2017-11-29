package com.qi.market.common.db;

import android.database.Cursor;

import com.qi.market.common.db.annotation.ColumnName;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * Author:liuqi
 * Date:2017/11/29 17:48
 * Detail:
 */
public class CursorConverter<T> {

    public T cursor(Cursor cursor, int index, T obj) throws IllegalAccessException {
        String columnName = cursor.getColumnName(index);
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ColumnName annotation = field.getAnnotation(ColumnName.class);
            if (annotation != null && annotation.value().equals(columnName)) {
                if (field.getType().getName().equals(String.class.getName())) {
                    field.set(obj, cursor.getString(index));
                } else if (field.getType().getName().equals(Double.class.getName())) {
                    field.set(obj, cursor.getDouble(index));
                } else if (field.getType().getName().equals(Float.class.getName())) {
                    field.set(obj, cursor.getFloat(index));
                } else if (field.getType().getName().equals(Long.class.getName())) {
                    field.set(obj, cursor.getLong(index));
                } else if (field.getType().getName().equals(String.class.getName())) {
                } else if (field.getType().getName().equals(String.class.getName())) {
                } else if (field.getType().getName().equals(String.class.getName())) {
                } else if (field.getType().getName().equals(String.class.getName())) {
                } else if (field.getType().getName().equals(String.class.getName())) {
                }
            }
        }
        return null;
    }
}
