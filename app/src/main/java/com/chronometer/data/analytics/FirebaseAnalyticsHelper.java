package com.chronometer.data.analytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.chronometer.utils.Constant.DATE_FORMAT_YYYYMMDD;
import static com.chronometer.utils.Constant.EVENT_BUTTON_TAP;
import static com.chronometer.utils.Constant.KEY_IS_COHORT_LOGGED;
import static com.chronometer.utils.Constant.KEY_USAGE_COUNT;
import static com.chronometer.utils.Constant.PARAM_LABEL;
import static com.chronometer.utils.Constant.PREF_NAME;
import static com.chronometer.utils.Constant.USER_PROPERTY_COHORT;
import static com.chronometer.utils.Constant.USER_PROPERTY_USAGE;

public class FirebaseAnalyticsHelper {

    private static FirebaseAnalytics firebaseAnalytics;

    private FirebaseAnalyticsHelper() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    private static void initializeFirebaseAnalytics(Context context) {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
    }

    public static void logCohortProperty(Context context) {
        initializeFirebaseAnalytics(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);

        if (!sharedPreferences.getBoolean(KEY_IS_COHORT_LOGGED, false)) {
            String cohortDate = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD,
                    Locale.getDefault()).format(new Date());

            firebaseAnalytics.setUserProperty(USER_PROPERTY_COHORT, cohortDate);
            sharedPreferences.edit().putBoolean(KEY_IS_COHORT_LOGGED, true).apply();
        }
    }

    public static void incrementUsageProperty(Context context) {
        initializeFirebaseAnalytics(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);

        int usageCount = sharedPreferences.getInt(KEY_USAGE_COUNT, 0) + 1;

        firebaseAnalytics.setUserProperty(USER_PROPERTY_USAGE, String.valueOf(usageCount));
        sharedPreferences.edit().putInt(KEY_USAGE_COUNT, usageCount).apply();
    }

    public static void logButtonTapEvent(Context context, String buttonLabel) {
        initializeFirebaseAnalytics(context);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_LABEL, buttonLabel);
        firebaseAnalytics.logEvent(EVENT_BUTTON_TAP, bundle);
    }
}
