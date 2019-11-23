package com.example.recipeworld.ui.RecipeList;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.recipeworld.R;

public class RecipeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.onerecipe);
        if (fragment == null) {
            fragment = new InfoSingleRecipe();
            fm.beginTransaction()
                .add(R.id.onerecipe, fragment)
                .commit();
        }
    }
}
