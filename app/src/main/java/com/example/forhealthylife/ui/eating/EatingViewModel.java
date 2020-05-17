package com.example.forhealthylife.ui.eating;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EatingViewModel extends ViewModel {
    private MutableLiveData<Integer> position;
    private MutableLiveData<Integer> count;
    private MutableLiveData<Integer> operation;
    private MutableLiveData<Integer> version;

    public EatingViewModel() {
        position = new MutableLiveData<>();
        count = new MutableLiveData<>();
        operation = new MutableLiveData<>();
        version = new MutableLiveData<>();
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

    public LiveData<Integer> getVersion() {
        return version;
    }

    public void setVersion(Integer ver) {
        version.setValue(ver);
    }

}
