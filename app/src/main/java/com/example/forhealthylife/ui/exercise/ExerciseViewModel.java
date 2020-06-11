package com.example.forhealthylife.ui.exercise;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExerciseViewModel extends ViewModel {
    private MutableLiveData<Integer> position;

    public ExerciseViewModel() {
        position = new MutableLiveData<>();

    }

    public LiveData<Integer> getInteger() {
        return position;
    }

    public void setInteger(Integer pos) {
        position.setValue(pos);
    }
}
