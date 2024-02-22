package com.example.recipeapp.ui.saved;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SavedViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> nText;

    public SavedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is saved fragment");
        nText = new MutableLiveData<>();
        nText.setValue("This is a second textView");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getSubText() { return nText; }
}
