package com.example.recipeworld.ui.recipeList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeworld.AllRecipes;
import com.example.recipeworld.R;
import com.example.recipeworld.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecipeListFragment extends Fragment {

    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeListAdapter mAdapter;
    private AllRecipes allrecipes;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recipelist, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        mRecipeListViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
/////////Recycler Things
        this.mRecyclerView = root.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
/////////Adapter Things
        updateUI();
        return root;
    }

        public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

            private final LayoutInflater mInflater;
            private List<Recipe> mRecipes;// Cached copy of Recipes
            private Context context;


            RecipeListAdapter(Context context, List<Recipe> recipes) {
                this.context = context;
                this.mRecipes = recipes;
                mInflater = LayoutInflater.from(context);
            }

            @Override
            public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
                return new RecipeViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(RecipeViewHolder holder, int position) {
                if (mRecipes != null) {
                    final Recipe current = mRecipes.get(position);
                    holder.recipe.setText(current.getRecipe());
                    //holder.ingredients.setText(current.getIngredients());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), RecipeActivity.class);
                            intent.putExtra("recipe_id",current.getmId());
                            startActivity(intent);
                        }
                    });
                } else {
                    // Covers the case of data not being ready yet.
                    holder.recipe.setText("No Recipe");
                }
            }

            void setRecipes(List<Recipe> recipes){
                mRecipes = recipes;
                notifyDataSetChanged();
            }

            // getItemCount() is called many times, and when it is first called,
            // mRecipes has not been updated (means initially, it's null, and we can't return null).
            @Override
            public int getItemCount() {
                if (mRecipes != null)
                    return mRecipes.size();
                else return 0;
            }

            class RecipeViewHolder extends RecyclerView.ViewHolder {
                private final TextView recipe;
                //private final TextView ingredients;

                private  RecipeViewHolder(final View itemView) {
                    super(itemView);
                    recipe = itemView.findViewById(R.id.textView);
                    //ingredients = itemView.findViewById(R.id.ingredients);
                }
            }
        }
    private void updateUI() {
        db.collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            LinkedList<Recipe> recipes = new LinkedList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Recipe recipe = new Recipe(document.getData().get("name").toString(),document.getData().get("ingredients").toString());
                                recipes.add(recipe);
                                if(mAdapter == null) {
                                    mAdapter = new RecipeListAdapter(getActivity(), recipes);
                                    mRecyclerView.setAdapter(mAdapter);
                                }else{
                                    mAdapter.notifyDataSetChanged();

                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        /*if(mAdapter == null) {
            mAdapter = new RecipeListAdapter(getActivity(), recipes);
            this.mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();

        }*/
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }


}
