package com.example.recipeworld;

import androidx.annotation.NonNull;
//import androidx.room.TypeConverters;

import java.util.List;
import java.util.UUID;

public class Recipe {
    private UUID mId;
    private String idDatabase;
    private String mRecipe;
    private String ingredients;

    /*public Recipe(@NonNull String recipe) {
        this.mRecipe = recipe;
        this.ingredients= ingredients;

    }*/
    public Recipe(String recipe, String ingredients) {
        mId = UUID.randomUUID();
        this.mRecipe = recipe;
        this.ingredients= ingredients;

    }
    public Recipe(String id, String recipe, String ingredients) {
        mId = UUID.randomUUID();
        this.idDatabase = id;
        this.mRecipe = recipe;
        this.ingredients= ingredients;

    }

    public Recipe(Recipe recipe) {
        this.mRecipe = recipe.getRecipe();
        this.ingredients= recipe.getIngredients();

    }

    public UUID getmId() {
        return mId;
    }
    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipe(){
        return this.mRecipe;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "mId=" + mId +
                ", idDatabase='" + idDatabase + '\'' +
                ", mRecipe='" + mRecipe + '\'' +
                ", ingredients='" + ingredients + '\'' +
                '}';
    }
}
