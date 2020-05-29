package com.example.recipeworld.ui.addRecipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddRecipeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddRecipeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
