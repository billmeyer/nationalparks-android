package com.example.nationalparks;

import android.app.Application;
import android.util.Log;

import com.splunk.rum.Config;
import com.splunk.rum.SplunkRum;

public class NationalParksApplication extends Application {
    private static final String LOG_TAG = "NationalParksApp";

    @Override
    public void onCreate() {
        super.onCreate();

        Config config = Config.builder()
                .applicationName("National Parks")
                .rumAccessToken(BuildConfig.RUM_ACCESS_TOKEN)
                .realm(BuildConfig.RUM_REALM)
                .deploymentEnvironment("development")
                .debugEnabled(true)
                .build();
        SplunkRum.initialize(config, this);

        SplunkRum sr = SplunkRum.getInstance();
        Log.d(LOG_TAG, String.format("Rum Session ID: %s", sr.getRumSessionId()));
    }
}
