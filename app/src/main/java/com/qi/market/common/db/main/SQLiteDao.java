package com.qi.market.common.db.main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.qi.market.common.db.annotation.DELETE;
import com.qi.market.common.db.annotation.INSERT;
import com.qi.market.common.db.annotation.QUERY;
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

    SQLiteDao(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    public static class Builder {
        private SQLiteOpenHelper sqliteOpenHelper = null;

        public void setSQLiteOpenHelper(SQLiteOpenHelper openHelper) {
            this.sqliteOpenHelper = openHelper;
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
                        query(method, args);
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

    private <T> Observable<List<T>> query(Method method, final Object[] args) {
        QUERY query = method.getAnnotation(QUERY.class);
        final String dbName = query.value();
        final SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        Observable.OnSubscribe<T> onSubscribe = new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        database.beginTransaction();
                        List<T> data = new ArrayList<>();
                        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
                        Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
                        try {
                            if (args == null || args.length == 0) {
                                String sql = "select * from " + dbName;
                                Cursor cursor = database.rawQuery(sql, null);
                                while (cursor.moveToNext()) {
                                    T obj = clazz.newInstance();
                                    int columnCount = cursor.getColumnCount();
                                    for (int i = 0; i < columnCount; i++) {
                                        cursor
                                    }
                                }
                            } else {
                                String sql = "select * from " + dbName + "where";
                            }
                            database.setTransactionSuccessful();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            database.endTransaction();
                        }
                    }
                });
            }
        };
        return Observable.create(onSubscribe);
    }

    private void update(Method method, Object[] args) {

    }

}
