package com.team_comfortable.forhealthylife.ui.exercise;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.team_comfortable.forhealthylife.R;
import com.team_comfortable.forhealthylife.ui.exercise.ExerciseViewModel;

public class ChestFragment extends Fragment {

    private ExerciseViewModel exerciseViewModel;

    public static ChestFragment newInstance() {
        return new ChestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chest, container, false);
        ListView listview = root.findViewById(R.id.chestName);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, com.team_comfortable.forhealthylife.ui.exercise.DataExercise.chestName);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exerciseViewModel.setInteger(position);
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
