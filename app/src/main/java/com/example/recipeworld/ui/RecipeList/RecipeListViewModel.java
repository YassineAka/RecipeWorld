package com.example.recipeworld.ui.RecipeList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecipeListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecipeListViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
