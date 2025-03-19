package com.chronometer.domain.model;

import androidx.annotation.NonNull;

public class Lap {
    private final String lapName;
    private final long partialTime;
    private final long totalTime;

    public Lap(String lapName, long partialTime, long totalTime) {
        this.lapName = lapName;
        this.partialTime = partialTime;
        this.totalTime = totalTime;
    }

    public String getLapName() {
        return lapName;
    }

    public long getPartialTime() {
        return partialTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "Lap: " + lapName + ", Partial: " + partialTime + " ms, Total: " + totalTime + " ms";
    }
}