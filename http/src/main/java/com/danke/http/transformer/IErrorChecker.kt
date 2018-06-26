package com.danke.http.transformer

/**
 * @author danke
 * @date 2018/6/26
 */
interface IErrorChecker<in T> {

    fun isTokenInvalid(t: T): Boolean

    fun isResponseError(t: T): Boolean
}