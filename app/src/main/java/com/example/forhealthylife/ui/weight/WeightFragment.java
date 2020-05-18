package com.example.forhealthylife.ui.weight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.forhealthylife.R;

public class WeightFragment extends Fragment {

    private WeightViewModel weightViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        weightViewModel =
                ViewModelProviders.of(this).get(WeightViewModel.class);
        View root = inflater.inflate(R.layout.fragment_weight, container, false);
        final TextView textView = root.findViewById(R.id.text_weight);

        weightViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {
                textView.setText(s);
            }
        });
        return root;
    }
}
