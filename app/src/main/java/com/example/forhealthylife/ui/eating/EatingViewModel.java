package com.example.forhealthylife.ui.eating;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EatingViewModel extends ViewModel {
    private MutableLiveData<Integer> position;
    private MutableLiveData<Integer> count;
    private MutableLiveData<Integer> operation;

    public EatingViewModel() {
        position = new MutableLiveData<>();
        count = new MutableLiveData<>();
        operation = new MutableLiveData<>();
    }

    public LiveData<Integer> getInteger() {
        return position;
    }

    public void setInteger(Integer pos) {
        position.setValue(pos);
    }

    public LiveData<Integer> getCount() {
        return count;
    }

    public void setCount(Integer cnt) {
        count.setValue(cnt);
    }

    public LiveData<Integer> getOperation() {
        return operation;
    }

    public void setOperation(Integer opr) {
        operation.setValue(opr);
    }


}
