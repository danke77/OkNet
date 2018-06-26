package com.danke.http.transformer

import com.danke.http.response.BaseResponse
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * @author danke
 * @date 2018/6/26
 */
abstract class BaseObservableTransformer<T : Response<R>, R : BaseResponse<*>> : ObservableTransformer<T, R>, IErrorChecker<R> {

    override fun apply(upstream: Observable<T>): ObservableSource<R> =
            upstream.map(errorChecker(this))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}