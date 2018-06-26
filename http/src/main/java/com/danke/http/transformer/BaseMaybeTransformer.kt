package com.danke.http.transformer

import com.danke.http.response.BaseResponse
import io.reactivex.Maybe
import io.reactivex.MaybeSource
import io.reactivex.MaybeTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * @author danke
 * @date 2018/6/26
 */
abstract class BaseMaybeTransformer<T : Response<R>, R : BaseResponse<*>> : MaybeTransformer<T, R>, IErrorChecker<R> {

    override fun apply(upstream: Maybe<T>): MaybeSource<R> =
            upstream.map(errorChecker(this))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}