package com.example.recipeworld.ui.RecipeList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeworld.R;
import com.example.recipeworld.Recipe;

import java.util.LinkedList;
import java.util.List;

public class RecipeListFragment extends Fragment {

    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeListAdapter mAdapter;

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
        this.mAdapter = new RecipeListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
/////////Adapter Things
        List<Recipe> recipes = new LinkedList<>();
        recipes.add(new Recipe("lasagne","ingédients,ingédients,ingédients,ingédients"));
        recipes.add(new Recipe("pâte","ingédients,ingédients,ingédients,ingédients"));
        recipes.add(new Recipe("cake","ingédients,ingédients,ingédients,ingédients"));
        recipes.add(new Recipe("poisson","ingédients,ingédients,ingédients,ingédients"));
        this.mAdapter.setRecipes(recipes);
        return root;
    }

    public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

        private final LayoutInflater mInflater;
        private List<Recipe> mRecipes;// Cached copy of Recipes
        private Context context;


        RecipeListAdapter(Context context) {
            this.context = context;
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
                holder.ingredients.setText(current.getIngredients());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), RecipeActivity.class);
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
            private final TextView ingredients;

            private  RecipeViewHolder(final View itemView) {
                super(itemView);
                recipe = itemView.findViewById(R.id.textView);
                ingredients = itemView.findViewById(R.id.ingredients);
            }
        }
    }
}
