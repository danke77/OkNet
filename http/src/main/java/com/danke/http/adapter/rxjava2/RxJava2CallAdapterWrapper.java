package com.danke.http.adapter.rxjava2;

import android.support.annotation.NonNull;

import com.danke.http.converter.gson.JsonParseException;

import org.reactivestreams.Publisher;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Retrofit;

/**
 * https://stackoverflow.com/questions/43225556/how-to-make-rxerrorhandlingcalladapterfactory
 *
 * @author danke
 * @date 2018/6/21
 */
final class RxJava2CallAdapterWrapper<R> implements CallAdapter<R, Object> {

    private final Retrofit retrofit;
    private final CallAdapter<R, Object> wrapped;
    private Call originCall;

    public RxJava2CallAdapterWrapper(Retrofit retrofit, CallAdapter<R, Object> wrapped) {
        this.retrofit = retrofit;
        this.wrapped = wrapped;
    }

    @Override
    public Type responseType() {
        return wrapped.responseType();
    }

    @Override
    public Object adapt(@NonNull Call<R> call) {
        originCall = call;
        Object result = wrapped.adapt(call);

        if (result instanceof Single) {
            return ((Single) result).onErrorResumeNext(new Function<Throwable, SingleSource>() {
                @Override
                public SingleSource apply(@NonNull Throwable throwable) throws Exception {
                    return Single.error(asRetrofitException(throwable));
                }
            });
        }

        if (result instanceof Observable) {
            return ((Observable) result).onErrorResumeNext(new Function<Throwable, ObservableSource>() {
                @Override
                public ObservableSource apply(@NonNull Throwable throwable) throws Exception {
                    return Observable.error(asRetrofitException(throwable));
                }
            });
        }

        if (result instanceof Flowable) {
            return ((Flowable) result).onErrorResumeNext(new Function<Throwable, Publisher>() {
                @Override
                public Publisher apply(Throwable throwable) throws Exception {
                    return Flowable.error(asRetrofitException(throwable));
                }
            });
        }

        if (result instanceof Maybe) {
            return ((Maybe) result).onErrorResumeNext(new Function<Throwable, MaybeSource>() {
                @Override
                public MaybeSource apply(Throwable throwable) throws Exception {
                    return Maybe.error(asRetrofitException(throwable));
                }
            });
        }

        if (result instanceof Completable) {
            return ((Completable) result).onErrorResumeNext(new Function<Throwable, CompletableSource>() {
                @Override
                public CompletableSource apply(@NonNull Throwable throwable) throws Exception {
                    return Completable.error(asRetrofitException(throwable));
                }
            });
        }

        return result;
    }

    private RetrofitException asRetrofitException(Throwable throwable) {
        // We had non-200 http error
        if (throwable instanceof HttpException) {
            return RetrofitException.httpError(retrofit, originCall.request(), ((HttpException) throwable).response());
        }

        if (throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException) {
            return RetrofitException.timeOutError(retrofit, originCall.request(), throwable);
        }

        if (throwable instanceof JsonParseException) {
            return RetrofitException.jsonParseError(retrofit, originCall.request(), (JsonParseException) throwable);
        }

        // A network error happened
        if (throwable instanceof IOException) {
            return RetrofitException.networkError(retrofit, originCall.request(), (IOException) throwable);
        }

        // We don't know what happened. We need to simply convert to an unknown error
        return RetrofitException.unexpectedError(retrofit, originCall.request(), throwable);
    }
}
