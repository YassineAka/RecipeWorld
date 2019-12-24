package com.example.recipeworld;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.recipeworld.ui.recipeList.RecipeListFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AllRecipes {
    private static AllRecipes sRecipeLab;
    private List<Recipe> mRecipes;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;


    public static AllRecipes get(Context context) {
        if(sRecipeLab == null) {
            sRecipeLab = new AllRecipes(context);
        }
        return sRecipeLab;
    }
    private AllRecipes(Context context) {

        //Log.d("YAAAAAAAAASSINE",getDb());
        /*db.collection("recipes")
                .add(recipe)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("YAAAAAAAAASSINE","Truuuuuuuuuue");
                        Log.d("Yaaaaassine", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("YAAAAAAAAASSINE","Faaaaaaaaaaaaalse");
                        Log.d("Yaaaaassine", "Error adding document", e);
                    }
                });*/

        /*mRecipes.add(new Recipe("lasagne","ingédients,ingédients,ingédients,ingédients"));
        mRecipes.add(new Recipe("pâte","ingédients,ingédients,ingédients,ingédients"));
        mRecipes.add(new Recipe("cake","ingédients,ingédients,ingédients,ingédients"));
        mRecipes.add(new Recipe("poisson","ingédients,ingédients,ingédients,ingédients"));*/


    }

    /*public QuerySnapshot getRecipes(){
        QuerySnapshot recipes;
        db.collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            recipes = task.getResult();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }*/
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

    public FirebaseFirestore getDb() {
        return db;
    }

}

