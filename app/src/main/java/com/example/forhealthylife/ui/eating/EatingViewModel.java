package com.example.forhealthylife.ui.eating;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EatingViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public EatingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

}
