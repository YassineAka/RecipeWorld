package com.example.recipeworld.ui.auth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipeworld.R;
import com.example.recipeworld.Recipe;
import com.example.recipeworld.ui.InfoSingleRecipe.InfoSingleRecipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Profile extends Fragment {
    private ImageView imageViewProfile;
    private TextView textInputEditTextUsername;
    //private TextView textViewEmail;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage ;
    private StorageReference mStorageRef ;
    private StorageReference ref;
    private RecyclerView mRecyclerView;
    private ProfileAdapter mAdapter;
    private LinkedList<String> mRecipesNames;
    private LinkedList<String> mRecipesIngredients;
    private LinkedList<String> mRecipesSteps;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        this.mAuth = FirebaseAuth.getInstance();
        this.imageViewProfile = root.findViewById(R.id.profile_activity_imageview_profile);
        this.textInputEditTextUsername = root.findViewById(R.id.profile_activity_edit_text_username);
        //this.textViewEmail = root.findViewById(R.id.profile_activity_text_view_email);
        this.updateUIWhenCreating();
            //this.configureToolbar();
/////////Recycler Things
        this.mRecipesNames = new LinkedList<String>();
        this.mRecipesIngredients = new LinkedList<String>();
        this.mRecipesSteps = new LinkedList<String>();
        updateLists();
        this.mRecyclerView = root.findViewById(R.id.recyclerview_profile);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        mRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        ProfileAdapter customAdapter = new ProfileAdapter(getContext(),this.mRecipesNames,this.mRecipesIngredients);
        mRecyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        updateUI();
        return root;
        }



    private void updateUIWhenCreating(){

        if (this.mAuth.getCurrentUser() != null){

            //Get picture URL from Firebase
            if (this.mAuth.getCurrentUser().getPhotoUrl() != null) {
                Log.i("IMAGE","EXIIIIIIIIIIST");
                Glide.with(this)
                        .load(this.mAuth.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewProfile);
            }else{
                Log.i("IMAGE","DONT EXIIIIIIIIIIST");
                Glide.with(this)
                        .load(R.drawable.user)
                        .into(imageViewProfile);
            }

            //Get email & username from Firebase
            String email = TextUtils.isEmpty(this.mAuth.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.mAuth.getCurrentUser().getEmail();
            String username = TextUtils.isEmpty(this.mAuth.getCurrentUser().getDisplayName()) ? getString(R.string.info_no_username_found) : this.mAuth.getCurrentUser().getDisplayName();

            //Update views with data
            this.textInputEditTextUsername.setText(username);
            //this.textViewEmail.setText(email);
        }
    }

    public class ProfileAdapter extends RecyclerView.Adapter<Profile.ProfileAdapter.ProfileViewHolder> {

        private final LayoutInflater mInflater;
        private LinkedList<String> mRecipesNames;
        private LinkedList<String> mRecipesIngredients;
        private Context context;


        ProfileAdapter(Context context, LinkedList<String> recipeNames, LinkedList<String> recipeIngredients) {
            this.context = context;
            this.mRecipesNames = recipeNames;
            this.mRecipesIngredients = recipeIngredients;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
            return new Profile.ProfileAdapter.ProfileViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ProfileViewHolder holder, int position) {
            holder.name.setText(mRecipesNames.get(position));
            holder.ingredient = mRecipesIngredients.get(position);
            holder.step = mRecipesSteps.get(position);



            //Get images from FirebaseStorage
            mStorageRef = FirebaseStorage.getInstance().getReference();
            String name = mStorageRef.child(mAuth.getCurrentUser().getUid()).getName();
            ref = mStorageRef.child(name+"/"+mRecipesNames.get(position));

            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).resize(200, 200)
                            .into(holder.image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("STORAGEUPDATEIIIN", e.getMessage());

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("recipeName",holder.name.getText().toString());
                bundle.putString("recipeIngredients",holder.ingredient);
                bundle.putString("recipeSteps",holder.step);
                bundle.putParcelable("recipeImage",((BitmapDrawable)holder.image.getDrawable()).getBitmap());
                InfoSingleRecipe newFragment = new InfoSingleRecipe();
                newFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,newFragment).commit();
            }
        });
        }
        @Override
        public int getItemCount() {
            return mRecipesNames.size();
        }


        class ProfileViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            String ingredient;
            String step;
            ImageView image;

            public ProfileViewHolder(View itemView) {
                super(itemView);
                // get the reference of item view's
                name = (TextView) itemView.findViewById(R.id.name);
                image = (ImageView) itemView.findViewById(R.id.image);
                ingredient = null;
                step = null;
            }


        }
    }
    private void updateUI() {
        getRecipes()
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
                                    mAdapter = new Profile.ProfileAdapter(getContext(), mRecipesNames, mRecipesIngredients);
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
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public CollectionReference getRecipes(){
        return db.collection(getCurrentUid());

    }
    public String getCurrentUid(){
        return mAuth.getCurrentUser().getUid();

    }


    public void updateLists(){
        getRecipes()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mRecipesNames.add(document.getData().get("name").toString());
                                mRecipesIngredients.add(document.getData().get("ingredients").toString());
                                mRecipesSteps.add(document.getData().get("steps").toString());

                            }
                        }
                    }
                });
    }
}
