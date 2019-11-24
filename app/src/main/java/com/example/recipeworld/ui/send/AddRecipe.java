package com.example.recipeworld.ui.send;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.recipeworld.R;
import com.example.recipeworld.Recipe;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRecipe extends Fragment {

    private AddRecipeViewModel addRecipeViewModel;
    private EditText mAddRecipeName;
    private EditText mAddRecipeIngredients;
    private Button mSave;
    private Button mTakePicture;
    private ImageView mPicture;
    private Bitmap picture;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference linkToRecipes = database.child("recipes");


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addRecipeViewModel =ViewModelProviders.of(this).get(AddRecipeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_addrecipe, container, false);
        /*final TextView textView = root.findViewById(R.id.text_addrecipe);
        addRecipeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        mAddRecipeName = root.findViewById(R.id.addrecipename);
        mAddRecipeIngredients = root.findViewById(R.id.addrecipeingredients);
        mPicture = root.findViewById(R.id.addrecipepicture);
        mTakePicture = root.findViewById(R.id.addrecipetakepicture);
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
        mSave = root.findViewById(R.id.addrecipesave);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addRecipe();
                //Save the recipe in the database
            }
        });
        if (savedInstanceState != null){
            this.picture = savedInstanceState.getParcelable("bitmap");
            mAddRecipeName.setText(savedInstanceState.getString("nameRecipe"));
            mAddRecipeIngredients.setText(savedInstanceState.getString("ingredientsRecipe"));
            mPicture.setImageBitmap(this.picture);

        }
        return root;
    }
    private void addRecipe(){
        /*String name =mAddRecipeName.getText().toString().intern();
        String ingredients = mAddRecipeIngredients.getText().toString().intern();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(ingredients)){
            String id = linkToRecipes.push().getKey();
            Recipe newRecipe = new Recipe(id,name,ingredients);
            Toast.makeText(getContext(),"Recipe added successfully",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),"The name or the ingredients is empty",Toast.LENGTH_LONG).show();
        }*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.picture = ((Bitmap) data.getExtras().get("data"));
        //Save the picture in the database
        mPicture.setImageBitmap(this.picture);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nameRecipe",mAddRecipeName.getText().toString());
        outState.putString("ingredientsRecipe",mAddRecipeIngredients.getText().toString());
        outState.putParcelable("bitmap", this.picture);


    }
}
