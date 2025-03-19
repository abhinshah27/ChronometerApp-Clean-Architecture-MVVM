package com.chronometer.presentation.view;

import android.os.Bundle;
import android.view.View;

import com.chronometer.R;
import com.chronometer.data.analytics.FirebaseAnalyticsHelper;
import com.chronometer.data.repository.ChronometerRepository;
import com.chronometer.databinding.ActivityChronometerBinding;
import com.chronometer.domain.model.Lap;
import com.chronometer.domain.repository.IChronometerRepository;
import com.chronometer.domain.usecase.GetLapsUseCase;
import com.chronometer.presentation.adapter.LapsAdapter;
import com.chronometer.presentation.viewmodel.ChronometerViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChronometerActivity extends AppCompatActivity {

    private ActivityChronometerBinding binding;
    private ChronometerViewModel chronometerViewModel;
    private boolean isRunning = false;
    private boolean wasRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChronometerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    /**
     * Initializes the ChronometerActivity by setting up UI components, ViewModel, and event listeners.
     * - Configures Firebase Analytics for tracking cohort properties.
     * - Initializes the ChronometerViewModel with the necessary use case.
     * - Sets up the RecyclerView for displaying lap times with a reverse order.
     * - Observes lap data changes and updates the UI accordingly.
     * - Registers event listeners for buttons (Start, Reset, Lap, and Bottom Start).
     */
    private void init() {
        FirebaseAnalyticsHelper.logCohortProperty(this);
        IChronometerRepository repository = new ChronometerRepository();
        GetLapsUseCase getLapsUseCase = new GetLapsUseCase(repository);

        chronometerViewModel = new ChronometerViewModel(getLapsUseCase);
        RecyclerView lapsRecyclerView = binding.rvLap;
        lapsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LapsAdapter lapsAdapter = new LapsAdapter();
        lapsRecyclerView.setAdapter(lapsAdapter);

        chronometerViewModel.getLapsLiveData().observe(this, state -> {
            List<Lap> reversedList = new ArrayList<>(state);
            Collections.reverse(reversedList);
            lapsAdapter.submitList(reversedList);
        });

        lapsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                if (positionStart == 0) {
                    binding.rvLap.setItemAnimator(null);
                    binding.rvLap.post(() -> binding.rvLap.smoothScrollToPosition(0));
                }
            }
        });
        binding.btnStart.setOnClickListener(v -> performStart(lapsAdapter));
        binding.btnReset.setOnClickListener(v -> performReset());
        binding.btnLap.setOnClickListener(v -> handleLap());
        binding.btnBottomStart.setOnClickListener(v -> handleBottomStart(lapsAdapter));
    }

    /**
     * Resets the chronometer and clears the lap list.
     */
    private void performReset() {
        binding.customChronometer.reset();
        chronometerViewModel.clearLaps();
        binding.btnBottomStart.setText(R.string.label_start);
        wasRunning = false;
    }

    /**
     * Starts or stops the chronometer and updates the UI accordingly.
     *
     * @param lapsAdapter The adapter for the lap RecyclerView.
     */
    private void performStart(LapsAdapter lapsAdapter) {
        if (isRunning) {
            binding.customChronometer.stop();
            FirebaseAnalyticsHelper.logButtonTapEvent(this, getString(R.string.label_stop));

            binding.btnBottomStart.setText(
                    lapsAdapter.getItemCount() > 0 ? R.string.label_reset : R.string.label_start);
            binding.btnStart.setText(R.string.label_start);
            binding.btnLap.setVisibility(View.GONE);
            binding.btnReset.setVisibility(View.VISIBLE);
            wasRunning = true;
        } else {
            if (wasRunning) {
                FirebaseAnalyticsHelper.logButtonTapEvent(this, getString(R.string.label_resume));
            } else {
                FirebaseAnalyticsHelper.logButtonTapEvent(this, getString(R.string.label_start));
                FirebaseAnalyticsHelper.incrementUsageProperty(this);
            }

            // Start the chronometer
            binding.customChronometer.start();
            binding.btnStart.setText(R.string.label_stop);
            binding.btnBottomStart.setText(R.string.label_running);
            binding.btnLap.setVisibility(View.VISIBLE);
            binding.btnReset.setVisibility(View.GONE);
            wasRunning = false;
        }
        isRunning = !isRunning;
    }

    /**
     * Handles the Lap button click event, adding a new lap time.
     */
    private void handleLap() {
        long elapsedTime = binding.customChronometer.getTimeElapsed();

        FirebaseAnalyticsHelper.logButtonTapEvent(this, getString(R.string.label_lap));
        chronometerViewModel.addLap(elapsedTime);
    }

    /**
     * Handles the bottom Start/Reset button click event.
     *
     * @param lapsAdapter The adapter for the lap RecyclerView.
     */
    private void handleBottomStart(LapsAdapter lapsAdapter) {
        String buttonText = binding.btnBottomStart.getText().toString();
        if (buttonText.equals(getString(R.string.label_start))) {
            performStart(lapsAdapter);
        } else if (buttonText.equals(getString(R.string.label_reset))) {
            performReset();
        }
    }
}