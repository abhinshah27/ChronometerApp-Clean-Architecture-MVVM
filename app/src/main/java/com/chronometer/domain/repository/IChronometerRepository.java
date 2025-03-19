package com.chronometer.domain.repository;

import com.chronometer.domain.model.Lap;

import java.util.List;

public interface IChronometerRepository {
    void addLap(Lap lap);

    List<Lap> getLaps();

    void clearLaps();
}