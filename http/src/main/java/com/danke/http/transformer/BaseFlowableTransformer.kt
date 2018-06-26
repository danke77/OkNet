package com.danke.http.transformer

import com.danke.http.response.BaseResponse
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import retrofit2.Response

/**
 * @author danke
 * @date 2018/6/26
 */
abstract class BaseFlowableTransformer<T : Response<R>, R : BaseResponse<*>> : FlowableTransformer<T, R>, IErrorChecker<R> {

    override fun apply(upstream: Flowable<T>): Publisher<R> =
            upstream.map(errorChecker(this))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}