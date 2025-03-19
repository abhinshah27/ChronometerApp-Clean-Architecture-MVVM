package com.chronometer.viewmodel;

import com.chronometer.domain.model.Lap;
import com.chronometer.domain.repository.IChronometerRepository;
import com.chronometer.domain.usecase.GetLapsUseCase;
import com.chronometer.presentation.viewmodel.ChronometerViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChronometerViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
            // For LiveData testing

    @Mock
    private GetLapsUseCase getLapsUseCase;
    @Mock
    private IChronometerRepository repository;
    private ChronometerViewModel viewModel;

    @Before
    public void setUp() {
        when(getLapsUseCase.getRepository()).thenReturn(repository);
        viewModel = new ChronometerViewModel(getLapsUseCase);
    }

    @Test
    public void testAddLap() {
        List<Lap> mockLaps = Collections.singletonList(
                new Lap("Lap 1", 1000, 1000)
        );
        when(getLapsUseCase.execute()).thenReturn(mockLaps);
        viewModel.addLap(1000);

        // Assert
        verify(repository).addLap(any(Lap.class));
        assertEquals(mockLaps, viewModel.getLapsLiveData().getValue());
    }

    @Test
    public void testClearLaps() {
        when(getLapsUseCase.execute()).thenReturn(new ArrayList<>());
        viewModel.clearLaps();

        // Assert
        verify(repository).clearLaps();
        assertEquals(0, Objects.requireNonNull(viewModel.getLapsLiveData().getValue()).size());
    }

    @Test
    public void testAddLapWithMultipleLaps() {
        List<Lap> mockLaps = Arrays.asList(
                new Lap("Lap 1", 1000, 1000),
                new Lap("Lap 2", 2000, 3000)
        );
        when(getLapsUseCase.execute()).thenReturn(mockLaps);

        viewModel.addLap(1000);
        viewModel.addLap(3000);

        verify(repository, times(2)).addLap(any(Lap.class));
        assertEquals(mockLaps, viewModel.getLapsLiveData().getValue());
    }
}