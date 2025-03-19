package com.chronometer.presentation;

import static com.chronometer.utils.Constant.BTN_LAP;
import static com.chronometer.utils.Constant.BTN_RESET;
import static com.chronometer.utils.Constant.BTN_START;
import static com.chronometer.utils.Constant.ERROR_LAP;
import static com.chronometer.utils.Constant.ERROR_LAP_LIST;
import static com.chronometer.utils.Constant.PACKAGE_NAME;
import static com.chronometer.utils.Constant.RV_LAP;
import static com.chronometer.utils.Constant.TIMEOUT;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import com.chronometer.presentation.view.ChronometerActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChronometerE2ETest {

    @Rule
    public ActivityScenarioRule<ChronometerActivity> activityRule =
            new ActivityScenarioRule<>(ChronometerActivity.class);
    private UiDevice device;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation());

        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        device.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), TIMEOUT);
    }

    @After
    public void tearDown() {
        activityRule.getScenario().close();
    }

    @Test
    public void testStartLapStopChronometer() {
        // Step 1: Click Start Button
        UiObject2 startButton = waitForElement(BTN_START);
        startButton.click();

        // Step 2: Click Lap Button
        UiObject2 lapButton = waitForElement(BTN_LAP);
        lapButton.click();

        // Step 3: Check if RecyclerView (Lap List) is Displayed
        UiObject2 lapList = waitForElement(RV_LAP);
        assertNotNull(ERROR_LAP, lapList);

        // Step 4: Stop the Chronometer
        UiObject2 stopButton = waitForElement(BTN_START);
        stopButton.click();

        // Step 5: Reset the Chronometer
        UiObject2 resetButton = waitForElement(BTN_RESET);
        resetButton.click();

        // Step 6: Verify Laps are Cleared
        UiObject2 updatedLapList = device.findObject(By.res(PACKAGE_NAME, RV_LAP));
        assertNotNull(ERROR_LAP_LIST, updatedLapList);
    }

    /**
     * Helper method to wait for a UI element to appear.
     */
    private UiObject2 waitForElement(String resourceId) {
        device.wait(Until.hasObject(By.res(PACKAGE_NAME, resourceId)), TIMEOUT);
        UiObject2 element = device.findObject(By.res(PACKAGE_NAME, resourceId));
        assertNotNull(resourceId + " not found", element);
        return element;
    }
}
