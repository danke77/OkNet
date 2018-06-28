package com.danke.http.sample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.danke.http.OkNet;
import com.danke.http.exception.TokenInvalidException;
import com.danke.http.subscriber.BaseObserver;
import com.danke.http.subscriber.ToastObserver;

import org.jetbrains.annotations.NotNull;

/**
 * @author danke
 * @date 2018/6/27
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "OkNet";

    private BaseObserver<String> getObserver;
    private BaseObserver<String> postObserver;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = OkNet.create(ApiService.class);
        final TextView textResponse = findViewById(R.id.textResponse);

        findViewById(R.id.btGet).setOnClickListener(v -> {
            textResponse.setText("");
            getObserver = apiService
                    .getSubject(1764796)
                    .compose(new DefaultTransformer<>())
                    .map(movieResponse -> movieResponse.title)
                    .subscribeWith(new MovieObserver(MainActivity.this, textResponse));
        });

        findViewById(R.id.btPost).setOnClickListener(v -> {
            textResponse.setText("");
            postObserver = apiService
                    .getTop250(0, 10)
                    .compose(new DefaultTransformer<>())
                    .map(movieResponse -> movieResponse.title)
                    .subscribeWith(new MovieObserver(MainActivity.this, textResponse));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getObserver.unsubscribe();
        postObserver.unsubscribe();
    }

    private class MovieObserver extends ToastObserver<String> {

        private TextView textView;

        MovieObserver(@NonNull Context context, @NonNull TextView textView) {
            super(context);
            this.textView = textView;
        }

        @Override
        public void onTokenInvalid(@NotNull TokenInvalidException e) {

        }

        @Override
        public void onNext(String s) {
            Log.i(TAG, "onNext: " + s);
            textView.setText(s);
        }

        @Override
        protected void onStart() {
            super.onStart();
            Log.i(TAG, "onStart");
        }

        @Override
        public void onComplete() {
            super.onComplete();
            Log.i(TAG, "onComplete");
        }

        @Override
        public void onError(@NotNull Throwable t) {
            super.onError(t);
            Log.i(TAG, "onError: " + t);
        }
    }
}
