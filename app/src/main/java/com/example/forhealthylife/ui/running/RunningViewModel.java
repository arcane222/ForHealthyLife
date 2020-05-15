package com.example.forhealthylife.ui.running;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RunningViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RunningViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }


}
