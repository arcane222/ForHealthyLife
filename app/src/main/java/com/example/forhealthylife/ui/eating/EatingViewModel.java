package com.example.forhealthylife.ui.eating;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EatingViewModel extends ViewModel {
    private MutableLiveData<Integer> position;

    public EatingViewModel() {
        position = new MutableLiveData<>();
    }

    public LiveData<Integer> getInteger() {
        return position;
    }

    public void setInteger(Integer pos) {
        position.setValue(pos);
    }
}
