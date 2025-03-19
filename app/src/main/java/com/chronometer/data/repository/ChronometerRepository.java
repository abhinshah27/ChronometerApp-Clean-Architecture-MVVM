package com.chronometer.data.repository;

import com.chronometer.domain.model.Lap;
import com.chronometer.domain.repository.IChronometerRepository;

import java.util.ArrayList;
import java.util.List;

public class ChronometerRepository implements IChronometerRepository {
    private final List<Lap> laps = new ArrayList<>();

    @Override
    public void addLap(Lap lap) {
        laps.add(lap);
    }

    @Override
    public List<Lap> getLaps() {
        return laps;
    }

    @Override
    public void clearLaps() {
        laps.clear();
    }
}