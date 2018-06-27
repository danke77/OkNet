package com.danke.http.sample;

import com.danke.http.response.BaseResponse;
import com.danke.http.transformer.BaseObservableTransformer;

import retrofit2.Response;

/**
 * @author danke
 * @date 2018/6/27
 */
public class DefaultTransformer<T extends Response<R>, R extends BaseResponse<?>> extends BaseObservableTransformer<T, R> {
    @Override
    public boolean isTokenInvalid(R r) {
        return false;
    }

    @Override
    public boolean isResponseError(R r) {
        return r.getCode() != null && r.getCode() != -1 || r.getMsg() != null;
    }
}