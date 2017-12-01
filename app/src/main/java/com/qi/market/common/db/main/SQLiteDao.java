package com.qi.market.common.db.main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qi.market.common.db.CursorConverter;
import com.qi.market.common.db.Pair;
import com.qi.market.common.db.annotation.ColumnName;
import com.qi.market.common.db.annotation.Condition;
import com.qi.market.common.db.annotation.DELETE;
import com.qi.market.common.db.annotation.INSERT;
import com.qi.market.common.db.annotation.QUERY;
import com.qi.market.common.db.annotation.Record;
import com.qi.market.common.db.annotation.UPDATE;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Subscriber;

/**
 * Author:liuqi
 * Date:2017/11/29 14:49
 * Detail:
 */
public final class SQLiteDao {

    private final SQLiteOpenHelper sqLiteOpenHelper;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    CursorConverter cursorConverter = new CursorConverter();

    SQLiteDao(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    public static class Builder {
        private SQLiteOpenHelper sqliteOpenHelper = null;

        public Builder setSQLiteOpenHelper(SQLiteOpenHelper openHelper) {
            this.sqliteOpenHelper = openHelper;
            return this;
        }

        public SQLiteDao build() {
            return new SQLiteDao(sqliteOpenHelper);
        }
    }

    @SuppressWarnings("unchecked") // Single-interface proxy creation guarded by parameter safety.
    public <T> T create(Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Annotation[] annotations = method.getDeclaredAnnotations();
                if (annotations == null)
                    return null;
                for (Annotation annotation : annotations) {
                    if (annotation instanceof QUERY) {
                        return query(method, args);
                    } else if (annotation instanceof UPDATE)
                        update(method, args);
                    else if (annotation instanceof INSERT)
                        insert(method, args);
                    else if (annotation instanceof DELETE)
                        delete(method, args);
                }
                return null;
            }
        });
    }

    private void delete(Method method, Object[] args) {

    }

    private void insert(Method method, Object[] args) {

    }

    /**
     * 查询方法
     *
     * @param method
     * @param args
     * @param <T>
     * @return
     */
    private <T> Observable<List<T>> query(final Method method, final Object[] args) {
        QUERY query = method.getAnnotation(QUERY.class);
        final String dbName = query.value();
        final SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        Observable.OnSubscribe<List<T>> onSubscribe = new Observable.OnSubscribe<List<T>>() {

            @Override
            public void call(final Subscriber<? super List<T>> subscriber) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            database.beginTransaction();
                            List<Cursor> cursors = new ArrayList<>();
                            if (args == null || args.length == 0) {
                                String sql = "select * from " + dbName;
                                cursors.add(database.rawQuery(sql, null));
                            } else if (args.length == 1) {
                                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                                if (parameterAnnotations == null)
                                    throw new Exception("查询方法需要添加注解Condition");
                                if (parameterAnnotations.length > 1 || parameterAnnotations[0].length > 1)
                                    throw new Exception("查询参数注解的个数超过限定个数1");
                                Annotation annotation = parameterAnnotations[0][0];
                                Object arg = args[0];
                                if (annotation instanceof Condition) {
                                    String sql = "select * from " + dbName + " where " + ((Condition) annotation).value() + "=?";
                                    if (arg.getClass().isArray()) {
                                        for (Object object : (Object[]) arg) {
                                            cursors.add(database.rawQuery(sql, new String[]{String.valueOf(object)}));
                                        }
                                    } else {
                                        cursors.add(database.rawQuery(sql, new String[]{String.valueOf(arg)}));
                                    }
                                } else
                                    throw new Exception("查询参数的注解类型错误");
                            } else {
                                throw new Exception("参数个数错误");
                            }
                            //获取返回类型
                            Type returnType = method.getGenericReturnType();
                            //如果返回类型带有泛型，获取泛型类型
                            Type genericType = getGenericType(returnType);
                            //获取第二层泛型类型
                            Type finalType = getGenericType(genericType);
                            Class<T> clazz = (Class) finalType;
                            List<T> data = new ArrayList();
                            for (Cursor cursor : cursors) {
                                while (cursor.moveToNext()) {
                                    T obj = clazz.newInstance();
                                    obj = cursorConverter.convertToObj(cursor, obj);
                                    data.add(obj);
                                }
                                cursor.close();
                            }
                            subscriber.onNext(data);
                            database.setTransactionSuccessful();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        } finally {
                            database.endTransaction();
                            subscriber.onCompleted();
                        }
                    }
                });
            }
        };
        return Observable.create(onSubscribe);
    }

    private void update(final Method method, final Object[] args) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
                try {
                    UPDATE update = method.getAnnotation(UPDATE.class);
                    final String t_name = update.value();
                    if (args.length > 2 || args.length == 0) {
                        throw new Exception("更新操作需要一或两个参数");
                    }
                    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                    if (parameterAnnotations.length == 0 || parameterAnnotations.length > 2) {
                        throw new Exception("更新操作时，参数需要注解类：Condition，ColumnName，Record");
                    }
                    String condition = "";
                    Object conditionValue = null;
                    String updatedColumnName = null;
                    Object array = null;
                    for (int i = 0; i < 2; i++) {
                        Annotation[] annotations = parameterAnnotations[i];
                        if (annotations[0] instanceof Condition) {
                            condition = ((Condition) annotations[0]).value() + "=?";
                            conditionValue = args[i];
                        } else if (annotations[0] instanceof ColumnName) {
                            updatedColumnName = ((ColumnName) annotations[0]).value();
                            array = args[i];
                        } else if (annotations[0] instanceof Record) {
                            array = args[i];
                        } else {
                            throw new Exception("注解错误");
                        }
                    }
                    if (!array.getClass().isArray()) {
                        throw new Exception("需要改变的列或记录必须以数组类型传递");
                    }
                    database.beginTransaction();
                    Object[] objects = (Object[]) array;
                    if (updatedColumnName == null) {
                        for (Object obj : objects) {
                            Pair<String, Object[]> pair = cursorConverter.convertToString(obj, conditionValue);
                            database.execSQL("update " + t_name + " set " + pair.getKey() + " where " + condition,
                                    pair.getValue());
                        }
                    } else {
                        for (Object obj : objects) {
                            database.execSQL("update " + t_name + " set " + updatedColumnName + "=?" + " where " + condition,
                                    conditionValue == null ? new Object[]{obj, conditionValue} : new Object[]{obj});
                        }
                    }
                    database.setTransactionSuccessful();
                    subscriber.onNext(null);
                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    database.endTransaction();
                }
            }
        });

    }

    private Type getGenericType(Type type) {
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return null;
    }

}
