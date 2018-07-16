# OkNet

[ ![Kotlin](https://img.shields.io/badge/Kotlin-1.2.41-blue.svg) ](http://kotlinlang.org)
[ ![Retrofit](https://img.shields.io/badge/Retrofit-2.4.0-blue.svg) ](https://github.com/square/retrofit)
[ ![OkHttp](https://img.shields.io/badge/OkHttp-3.11.0-blue.svg) ](https://github.com/square/okhttp)
[ ![RxJava](https://img.shields.io/badge/RxJava-2.1.16-blue.svg) ](https://github.com/ReactiveX/RxJava)
[ ![jcenter](https://api.bintray.com/packages/danke/maven/OkNet/images/download.svg) ](https://bintray.com/danke/maven/OkNet/_latestVersion)
[ ![Build Status](https://travis-ci.org/danke77/OkNet.svg?branch=master) ](https://travis-ci.org/danke77/OkNet)

OkNet is a wrapper **HTTP** networking library by **Retrofit** + **OkHttp** + **RxJava** for Android. Works on API level 14 or later.

## Features

* Features of [Retrofit](https://github.com/square/retrofit) and [OkHttp](https://github.com/square/okhttp)
* Method Chaining by [RxJava](https://github.com/ReactiveX/RxJava)
* Support calling in both Kotlin and Java
* Automatically invoke response callback on Android Main Thread while HTTP networking on IO Thread
* Support custom OkHttp Client and Retrofit
* Support maintaining multiple Retrofit object
* High-efficiency GsonConverterFactory and RxJava2CallAdapterFactory
* **More coming soon!**

## Dependencies

``` groovy
repositories {
    jcenter()
}

implementation 'com.danke.android:oknet:x.y.z'
```

## Usage

* There are two samples, one is in [Kotlin](app-kotlin) and another in [Java](app-java).

### Quick Glance Usage

* Kotlin

``` kotlin
// get
apiService.get()
        .compose(MyTransformer<Response<MyResponse>, MyResponse>())
        .map { it.data }
        .subscribeWith(object : ToastObserver<String>(context){
            override fun onNext(t: String) {
                // do something when it is successful
            }

            override fun onStart() {
                super.onStart()
                // do something when it is started
            }

            override fun onError(t: Throwable) {
                super.onError(t)
                // do something when it is failed
            }

            override fun onComplete() {
                super.onComplete()
                // do something when it is completed
            }

            override fun onTokenInvalid(e: TokenInvalidException) {
                // do something when token is invalid
            }
        })
```

* Java

``` java
// get
apiService.get()
        .compose(new MyTransformer <>())
        .map(response -> response.data)
        .subscribeWith(new ToastObserver<String>(context) {
            @Override
            public void onNext(String s) {
                // do something when it is successful
            }

            @Override
            protected void onStart() {
                super.onStart();
                // do something when it is started
            }

            @Override
            public void onError(@NotNull Throwable t) {
                super.onError(t);
                // do something when it is failed
            }

            @Override
            public void onComplete() {
                super.onComplete();
                // do something when it is completed
            }

            @Override
            public void onTokenInvalid(@NotNull TokenInvalidException e) {
                // do something when token is invalid
            }
        });
```

### Configuration

You can make common configuration in Application or MainActivity.

* Kotlin

Base url is necessary

``` kotlin
OkNet.apply {
    baseUrl = BASE_URL
}
```

Or custom RetrofitBuilder

``` kotlin
OkNet.apply {
    retrofitBuilder = RetrofitBuilder(baseUrl = BASE_URL)
            .isLoggingEnable(BuildConfig.DEBUG)
            .monitor(MyMonitor)
            .clientBuilder(MyOkHttpClientBuilder)
            .interceptors(MyInterceptor)
            .callAdapterFactories(MyCallAdapterFactory)
            .converterFactories(MyConverterFactory)
            .timeout(timeout)
}
```

* Java

Base url is necessary

``` java
OkNet.setBaseUrl(BASE_URL);
```

Or custom RetrofitBuilder

``` java
OkNet.setRetrofitBuilder(new RetrofitBuilder(BASE_URL)
        .isLoggingEnable(BuildConfig.DEBUG)
        .monitor(MyMonitor)
        .clientBuilder(MyOkHttpClientBuilder)
        .interceptors(MyInterceptor)
        .callAdapterFactories(MyCallAdapterFactory)
        .converterFactories(MyConverterFactory)
        .timeout(timeout);
```

### Response

Custom response must extends `BaseResponse`

* Kotlin

``` kotlin
@Keep
data class MyResponse(
        @SerializedName("others") val others: String?) : BaseResponse<MyResponseData>()
```

* Java

``` java
@Keep
public class MyResponse extends BaseResponse<MyResponseData> {
    @SerializedName("others")
    public String others;
}
```

### Service

Service must be an interface, and you should define your apis in service.

* Kotlin

``` kotlin
interface ApiService {
    @GET("list")
    fun getList(@Query("start") start: Int = 0,
                @Query("count") count: Int = 10): Observable<Response<MyResponse>>

    @FormUrlEncoded
    @POST("list")
    fun postList(@Field("start") start: Int = 0,
                 @Field("count") count: Int = 10): Observable<Response<MyResponse>>

    @GET("subject/{id}")
    fun getSubject(@Path("id") id: Long): Observable<Response<MyResponse>>
}
```

* Java

``` java
public interface ApiService {
    @GET("list")
    Observable<Response<MyResponse>> getList(@Query("start") int start,
                                             @Query("count") int count);

    @FormUrlEncoded
    @POST("list")
    Observable<Response<MyResponse>> postList(@Field("start") int start,
                                              @Field("count") int count);

    @GET("subject/{id}")
    Observable<Response<MyResponse>> getSubject(@Path("id") long id);
}
```

### Transformer

If there are some common business errors to handle, you can do it in your own Transformer witch extends one of `BaseObservableTransformer`, `BaseFlowableTransformer`, `BaseMaybeTransformer`, or `BaseSingleTransformer`.

* Kotlin

``` kotlin
class MyTransformer<T : Response<R>, R : BaseResponse<*>> : BaseObservableTransformer<T, R>() {
    override fun isTokenInvalid(r: R): Boolean = false

    override fun isResponseError(r: R): Boolean = r.code != null || r.msg != null
}
```

* Java

``` java
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
```

### Subscriber

You can define your own Subscriber or Observer to handle reponse, witch extends 
`BaseSubscriber` or `BaseObserver`. Also you can extends `ToastSubscriber` or `ToastObserver` witch toast error message, so you can only care for your successful response.

## Proguard

A ProGuard configuration is provided as part of the library, so you don’t need to add any specific rules to your ProGuard configuration.

## Contribution

* Issues are welcome. Please add a screenshot of bug and code snippet.
* Pull requests are welcome. If you want to change API or making something big better to create issue and discuss it first.

## LICENSE

This library is licensed under the [Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

See [`LICENSE`](LICENSE) for full of the license text.

    Copyright (C) 2018 danke77.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.