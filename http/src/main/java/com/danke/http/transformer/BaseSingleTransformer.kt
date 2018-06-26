package com.danke.http.transformer

import com.danke.http.response.BaseResponse
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * @author danke
 * @date 2018/6/26
 */
abstract class BaseSingleTransformer<T : Response<R>, R : BaseResponse<*>> : SingleTransformer<T, R>, IErrorChecker<R> {

    override fun apply(upstream: Single<T>): SingleSource<R> =
            upstream.map(errorChecker(this))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}