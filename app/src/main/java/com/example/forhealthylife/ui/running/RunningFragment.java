package com.example.forhealthylife.ui.running;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.forhealthylife.R;


public class RunningFragment extends Fragment {

    private RunningViewModel runningViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        runningViewModel =
                ViewModelProviders.of(this).get(RunningViewModel.class);
        View root = inflater.inflate(R.layout.fragment_running, container, false);
        final TextView textView = root.findViewById(R.id.text_community);

        return root;
    }

}
