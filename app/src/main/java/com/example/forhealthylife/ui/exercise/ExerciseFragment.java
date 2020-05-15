package com.example.forhealthylife.ui.exercise;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.forhealthylife.R;
import com.example.forhealthylife.ui.running.RunningViewModel;

public class ExerciseFragment extends Fragment {

    private ExerciseViewModel exerciseViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        exerciseViewModel =
                ViewModelProviders.of(this).get(ExerciseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);

        return root;
    }

}
