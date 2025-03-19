package com.chronometer.presentation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chronometer.databinding.ItemLapBinding;
import com.chronometer.domain.model.Lap;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class LapsAdapter extends ListAdapter<Lap, LapsAdapter.LapViewHolder> {

    // Constructor
    public LapsAdapter() {
        super(new LapDiffCallback());
    }

    @NonNull
    @Override
    public LapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use ViewBinding to inflate the layout
        ItemLapBinding binding = ItemLapBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new LapViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LapViewHolder holder, int position) {
        // Bind the lap data to the ViewHolder
        Lap lap = getItem(position);
        holder.bind(lap);
    }

    // ViewHolder class using ViewBinding
    public static class LapViewHolder extends RecyclerView.ViewHolder {
        private final ItemLapBinding binding;

        public LapViewHolder(@NonNull ItemLapBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // Bind lap data to the views
        public void bind(@NonNull Lap lap) {
            binding.tvLap.setText(lap.getLapName());
            binding.tvLapTime.setText(formatTime(lap.getPartialTime()));
            binding.tvTotalTime.setText(formatTime(lap.getTotalTime()));
        }

        private String formatTime(long timeInMillis) {
            long hours = timeInMillis / 3600000;
            long minutes = (timeInMillis % 3600000) / 60000;
            long seconds = (timeInMillis % 60000) / 1000;
            long milliseconds = (timeInMillis % 1000) / 10;

            return String.format(Locale.getDefault(), "%02d:%02d:%02d.%02d", hours, minutes,
                    seconds, milliseconds);
        }
    }

    // DiffUtil callback for efficient list updates
    static class LapDiffCallback extends DiffUtil.ItemCallback<Lap> {
        @Override
        public boolean areItemsTheSame(@NonNull Lap oldItem, @NonNull Lap newItem) {
            return oldItem.getLapName().equals(newItem.getLapName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Lap oldItem, @NonNull Lap newItem) {
            return oldItem.getPartialTime() == newItem.getPartialTime() &&
                    oldItem.getTotalTime() == newItem.getTotalTime();
        }
    }
}