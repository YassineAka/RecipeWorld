package com.example.recipeworld;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AllRecipes {
    private static AllRecipes sRecipeLab;
    private List<Recipe> mRecipes;

    public static AllRecipes get(Context context) {
        if(sRecipeLab == null) {
            sRecipeLab = new AllRecipes(context);
        }
        return sRecipeLab;
    }
    private AllRecipes(Context context) {
        mRecipes = new ArrayList<>();
        mRecipes.add(new Recipe("lasagne","ingédients,ingédients,ingédients,ingédients"));
        mRecipes.add(new Recipe("pâte","ingédients,ingédients,ingédients,ingédients"));
        mRecipes.add(new Recipe("cake","ingédients,ingédients,ingédients,ingédients"));
        mRecipes.add(new Recipe("poisson","ingédients,ingédients,ingédients,ingédients"));


    }
    public Recipe getRecipe(UUID id) {
        for (Recipe Recipe : mRecipes) {
            if(Recipe.getmId().equals(id))
                return Recipe;
        }
        return null;
    }
    public List<Recipe> getRecipes() {
        return mRecipes;
    }
}

