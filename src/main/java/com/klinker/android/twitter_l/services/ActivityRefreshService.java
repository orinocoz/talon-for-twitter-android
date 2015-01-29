package com.klinker.android.twitter_l.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import com.klinker.android.twitter_l.settings.AppSettings;
import com.klinker.android.twitter_l.utils.ActivityUtils;

public class ActivityRefreshService extends IntentService {

    SharedPreferences sharedPrefs;

    public ActivityRefreshService() {
        super("ActivityRefreshService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        AppSettings settings = AppSettings.getInstance(this);
        ActivityUtils utils = new ActivityUtils(this);

        boolean newActivity = utils.refreshActivity();

        if (settings.notifications && settings.activityNot && newActivity) {
            utils.postNotification();
        }
    }
}
