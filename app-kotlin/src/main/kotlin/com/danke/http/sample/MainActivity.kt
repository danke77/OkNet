package com.danke.http.sample

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.danke.http.OkNet
import com.danke.http.exception.TokenInvalidException
import com.danke.http.subscriber.BaseObserver
import com.danke.http.subscriber.ToastObserver
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

/**
 * @author danke
 * @date 2018/6/27
 */
class MainActivity : AppCompatActivity() {

    private lateinit var observer: BaseObserver<String>
    private val apiService by lazy {
        OkNet.create(ApiService::class.java)
    }

    companion object {
        private const val TAG = "OkNet"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btGet.setOnClickListener {
            textResponse.text = ""
            observer = apiService
                    .getUser("Square")
                    .compose(DefaultTransformer<Response<UserResponse>, UserResponse>())
                    .map { it.name }
                    .subscribeWith(UserObserver(this@MainActivity, textResponse))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        observer.unsubscribe()
    }

    private class UserObserver constructor(context: Context, private val textView: TextView) : ToastObserver<String>(context) {
        override fun onTokenInvalid(e: TokenInvalidException) {
        }

        override fun onNext(t: String) {
            Log.i(TAG, "onNext: $t")
            textView.text = t
        }

        override fun onStart() {
            super.onStart()
            Log.i(TAG, "onStart")
        }

        override fun onComplete() {
            super.onComplete()
            Log.i(TAG, "onComplete")
        }

        override fun onError(t: Throwable) {
            super.onError(t)
            Log.i(TAG, "onError: $t")
        }
    }
}
