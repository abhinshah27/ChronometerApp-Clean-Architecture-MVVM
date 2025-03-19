package com.chronometer.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chronometer.domain.model.Lap;
import com.chronometer.domain.usecase.GetLapsUseCase;

import java.util.ArrayList;
import java.util.List;

public class ChronometerViewModel extends ViewModel {

    private final GetLapsUseCase getLapsUseCase;

    private final MutableLiveData<List<Lap>> lapsLiveData = new MutableLiveData<>();

    private long lastLapTime = 0;

    public ChronometerViewModel(GetLapsUseCase getLapsUseCase) {
        this.getLapsUseCase = getLapsUseCase;
        // Initialize the LiveData with an empty list
        lapsLiveData.setValue(new ArrayList<>());
    }

    public LiveData<List<Lap>> getLapsLiveData() {
        lapsLiveData.setValue(getLapsUseCase.execute());
        return lapsLiveData;
    }

    /**
     * Adds a new lap to the list.
     *
     * @param currentTime The current time in milliseconds when the lap is added.
     */
    public void addLap(long currentTime) {
        List<Lap> laps = lapsLiveData.getValue();
        if (laps != null) {
            long partialTime = laps.isEmpty() ? currentTime : currentTime - lastLapTime;
            String lapName = "Lap " + (laps.size() + 1);
            Lap lap = new Lap(lapName, partialTime, currentTime);
            getLapsUseCase.getRepository().addLap(lap);
            lastLapTime = currentTime;
            lapsLiveData.setValue(getLapsUseCase.execute());
        }
    }

    /**
     * Clears all laps from the list and resets the last lap time.
     */
    public void clearLaps() {
        getLapsUseCase.getRepository().clearLaps();
        lastLapTime = 0;
        lapsLiveData.setValue(getLapsUseCase.execute());
    }
}