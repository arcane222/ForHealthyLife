package com.team_comfortable.forhealthylife.ui.notice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NoticeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NoticeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Notice fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}