package com.example.recipeworld.ui.RecipeList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeworld.AllRecipes;

public class RecipeListViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private AllRecipes mRecipes;

    public RecipeListViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is gallery fragment");
    }

    public AllRecipes getmRecipes() {
        return mRecipes;
    }

    public void setmRecipes(AllRecipes mRecipes) {
        this.mRecipes = mRecipes;
    }

    public LiveData<String> getText() {
        return mText;
    }
}
