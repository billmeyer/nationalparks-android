package com.example.nationalparks.ui.nationalparks;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nationalparks.BuildConfig;
import com.example.nationalparks.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NationalParksViewModel extends ViewModel {
    private final MutableLiveData<List<JSONObject>> mNationalParks;

    public NationalParksViewModel() {
        mNationalParks = new MutableLiveData<>();

        Call.Factory httpClient = HttpUtil.buildOkHttpClient();

        // Retrieve a random number of National Parks from the DB. (Varies the duration of the web serivce call).
        int count = getRandomInt(5, 100);
        int start = getRandomInt(0, 359-count);

        final String url = String.format(Locale.US, "%s/api/v1/nationalparks?start=%d&count=%d", BuildConfig.BACKEND_URL, start, count);
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
                System.out.printf("response body: %s\n", resStr);
                parseJSONResponseRentBanner(resStr);
            }
        });
    }

    protected int getRandomInt(int min, int max) {
        min = (int)Math.ceil(min);
        max = (int)Math.floor(max);
        return (int)Math.floor(Math.random() * (max - min) + min); //The maximum is exclusive and the minimum is inclusive
    }

    public LiveData<List<JSONObject>> getTexts() {
        return mNationalParks;
    }

    private void parseJSONResponseRentBanner(String str) {
        List<JSONObject> names = new ArrayList<>();

        try {
            JSONArray jSONArray = new JSONArray(str);
            if (jSONArray.length() > 0) {
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    names.add(jSONObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mNationalParks.postValue(names);
    }
}
