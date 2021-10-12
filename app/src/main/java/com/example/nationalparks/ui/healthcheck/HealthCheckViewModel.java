package com.example.nationalparks.ui.healthcheck;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nationalparks.BuildConfig;
import com.example.nationalparks.HttpUtil;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HealthCheckViewModel extends ViewModel {
    private static final String LOG_TAG = "HealthCheckViewModel";
    private MutableLiveData<String> mText;

    public HealthCheckViewModel() {
        mText = new MutableLiveData<>();

        Call.Factory httpClient = HttpUtil.buildOkHttpClient();

        final String url = String.format(Locale.US, "%s/api/v1/health-check", BuildConfig.BACKEND_URL);
        Call call = httpClient.newCall(new Request.Builder().url(url).get().build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String resStr = responseBody.string();
                Log.d(LOG_TAG, String.format("Response Body: %s", resStr));
                mText.postValue(resStr);
            }
        });
    }

    public LiveData<String> getText() {
        return mText;
    }
}