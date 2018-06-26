package com.danke.http.adapter.rxjava2;

import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author danke
 * @date 2018/6/21
 */
public final class RxJava2ErrorHandlingCallAdapterFactory extends CallAdapter.Factory {

    private final RxJava2CallAdapterFactory original;

    private RxJava2ErrorHandlingCallAdapterFactory() {
        original = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxJava2ErrorHandlingCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(@NonNull final Type returnType,
                                 @NonNull final Annotation[] annotations,
                                 @NonNull final Retrofit retrofit) {
        CallAdapter callAdapter = original.get(returnType, annotations, retrofit);
        if (callAdapter != null) {
            return new RxJava2CallAdapterWrapper(retrofit, callAdapter);
        }

        return null;
    }

}
