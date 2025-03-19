package com.chronometer.utils;

public class Constant {
    public static final String PREF_NAME = "FirebaseAnalyticsPrefs";
    public static final String KEY_IS_COHORT_LOGGED = "is_cohort_logged";
    public static final String KEY_USAGE_COUNT = "usage_count";
    public static final String EVENT_BUTTON_TAP = "event_name";
    public static final String PARAM_LABEL = "tap_with_label";
    public static final String USER_PROPERTY_COHORT = "Cohort";
    public static final String USER_PROPERTY_USAGE = "Usage";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

    private Constant() {
        throw new UnsupportedOperationException("Cannot instantiate Constant class");
    }
}
