package com.example.forhealthylife.ui.eating;

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

public class EatingFragment extends Fragment {

    private EatingViewModel eatingViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        eatingViewModel =
                ViewModelProviders.of(this).get(EatingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_eating, container, false);


        return root;





    }


}
