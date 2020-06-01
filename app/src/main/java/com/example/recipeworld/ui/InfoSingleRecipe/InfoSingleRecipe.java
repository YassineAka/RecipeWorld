package com.example.recipeworld.ui.InfoSingleRecipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.recipeworld.AllRecipes;
import com.example.recipeworld.R;
import com.example.recipeworld.Recipe;

import java.util.StringTokenizer;
import java.util.UUID;

public class InfoSingleRecipe extends Fragment {
    private ImageView mImage;
    private TextView mRecipeName;
    private TextView mRecipeIngredients;
    private TextView mRecipeSteps;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        this.mRecipeSteps = v.findViewById(R.id.recipeSteps);
        Bundle bundle = this.getArguments();
        this.mRecipeName.setText(bundle.getString("recipeName"));
        StringTokenizer tokenIngredients = new StringTokenizer(bundle.getString("recipeIngredients"),",");
        while (tokenIngredients.hasMoreElements()){
            this.mRecipeIngredients.append("- " +tokenIngredients.nextToken(",")+"\n");
        }
        StringTokenizer tokenSteps = new StringTokenizer(bundle.getString("recipeSteps"),",");
        while (tokenSteps.hasMoreElements()){
            this.mRecipeSteps.append("- " +tokenSteps.nextToken(",")+"\n");
        }
        this.mImage.setImageBitmap((Bitmap) bundle.getParcelable("recipeImage"));

        return v;
    }

    public void onBackPressed() {
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }

}

