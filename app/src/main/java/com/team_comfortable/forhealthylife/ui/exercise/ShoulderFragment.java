package com.team_comfortable.forhealthylife.ui.exercise;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.team_comfortable.forhealthylife.R;


public class ShoulderFragment extends Fragment {
    private ExerciseViewModel exerciseViewModel;

    public static ShoulderFragment newInstance() {
        return new ShoulderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shoulder, container, false);
        ListView listview = root.findViewById(R.id.shoulderName);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, DataExercise.shoulderName);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exerciseViewModel.setInteger(position+12);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        exerciseViewModel = ViewModelProviders.of(getActivity()).get(ExerciseViewModel.class);
    }
}

