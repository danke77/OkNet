package com.danke.http.sample

import com.danke.http.response.BaseResponse
import com.danke.http.transformer.BaseObservableTransformer
import retrofit2.Response

/**
 * @author danke
 * @date 2018/6/27
 */
class DefaultTransformer<T : Response<R>, R : BaseResponse<*>> : BaseObservableTransformer<T, R>() {

    override fun isTokenInvalid(r: R): Boolean = false

    override fun isResponseError(r: R): Boolean = r.code != null || r.msg != null
}