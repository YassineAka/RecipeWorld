package com.example.recipeworld.ui.RecipeList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipeworld.AllRecipes;
import com.example.recipeworld.MainActivity;
import com.example.recipeworld.R;
import com.example.recipeworld.Recipe;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

public class InfoSingleRecipe extends Fragment {
    private Recipe mRecipe;
    private ImageView mImage;
    private TextView mRecipeName;
    private TextView mRecipeIngredients;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID recipe_id = (UUID) getActivity().getIntent().getSerializableExtra("recipe_id");
        this.mRecipe = AllRecipes.get(getActivity()).getRecipe(recipe_id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////////Recyclerview linking
        View v = inflater.inflate(R.layout.fragment_onerecipe,container,false);

////////objectsview linking
        this.mImage = v.findViewById(R.id.recipeImage);
        this.mImage.setImageResource(R.drawable.meat);
        this.mRecipeName = v.findViewById(R.id.recipeName);
        this.mRecipeIngredients = v.findViewById(R.id.recipeIngredients);
        this.mRecipeName.setText(this.mRecipe.getRecipe());
        this.mRecipeIngredients.setText(this.mRecipe.getIngredients());

        return v;
    }

}
