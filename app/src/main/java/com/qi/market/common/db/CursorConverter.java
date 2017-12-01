package com.qi.market.common.db;

import android.database.Cursor;

import com.qi.market.common.db.annotation.ColumnName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * Author:liuqi
 * Date:2017/11/29 17:48
 * Detail:
 */
public class CursorConverter {

    public <T> T convertToObj(Cursor cursor, T obj) throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int index = 0; index < cursor.getColumnCount(); index++) {
            String columnName = cursor.getColumnName(index);
            for (Field field : fields) {
                field.setAccessible(true);
                ColumnName annotation = field.getAnnotation(ColumnName.class);
                if ((annotation != null && annotation.value().equals(columnName))
                        || (field.getName().equals(columnName))) {
                    setValue(obj, field, cursor, index);
                    break;
                }
            }
        }

        return obj;
    }


    private <T> void setValue(T obj, Field field, Cursor cursor, int index) throws Exception {
        if (field.getType().getName().equals(String.class.getName())) {
            field.set(obj, cursor.getString(index));
        } else if (field.getType().getName().equals(Double.class.getName())
                || field.getType().getName().equals(double.class.getName())) {
            field.set(obj, cursor.getDouble(index));
        } else if (field.getType().getName().equals(Float.class.getName())
                || field.getType().getName().equals(float.class.getName())) {
            field.set(obj, cursor.getFloat(index));
        } else if (field.getType().getName().equals(Long.class.getName())
                || field.getType().getName().equals(long.class.getName())) {
            field.set(obj, cursor.getLong(index));
        } else if (field.getType().getName().equals(Integer.class.getName())
                || field.getType().getName().equals(int.class.getName())) {
            field.set(obj, cursor.getInt(index));
        } else if (field.getType().getName().equals(Short.class.getName())
                || field.getType().getName().equals(short.class.getName())) {
            field.set(obj, cursor.getShort(index));
        } else {
            field.set(obj, cursor.getShort(index));
        }
    }

    public Pair<String, Object[]> convertToString(Object obj, Object conditionValue) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        String columns = "";
        Object[] args = (conditionValue == null) ? new Object[fields.length] : new Object[fields.length + 1];
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            args[i] =  field.get(obj);
            ColumnName annotation = field.getAnnotation(ColumnName.class);
            if (annotation != null) {
                columns += annotation.value() + "=?,";
            } else {
                columns += field.getName() + "=?,";
            }
        }
        columns.substring(0, columns.length() - 1);
        if (conditionValue != null) {
            args[args.length - 1] = conditionValue;
        }
        return new Pair<>(columns, args);
    }
}
