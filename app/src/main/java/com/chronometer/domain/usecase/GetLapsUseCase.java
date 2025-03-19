package com.chronometer.domain.usecase;

import com.chronometer.domain.model.Lap;
import com.chronometer.domain.repository.IChronometerRepository;

import java.util.List;

public class GetLapsUseCase {
    private final IChronometerRepository repository;

    public GetLapsUseCase(IChronometerRepository repository) {
        this.repository = repository;
    }

    public List<Lap> execute() {
        return repository.getLaps();
    }

    public IChronometerRepository getRepository() {
        return repository;
    }
}