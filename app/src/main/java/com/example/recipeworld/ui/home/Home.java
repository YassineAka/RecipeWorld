package com.example.recipeworld.ui.home;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.recipeworld.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.LinkedList;
import java.util.UUID;

public class Home extends Fragment {

    private HomeViewModel homeViewModel;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView mRecyclerView;
    private Home.HomeAdapter mAdapter;
    private LinkedList<String> mUsersUid;
    private LinkedList<String> mUsersNames;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef;
    private StorageReference ref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        mUsersUid = new LinkedList<>();
        mUsersNames = new LinkedList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!document.getData().get("uid").toString().equals( mAuth.getCurrentUser().getUid())){
                                    mUsersUid.add(document.getData().get("uid").toString());
                                    mUsersNames.add(document.getData().get("name").toString());
                                }

                            }
                            mRecyclerView = root.findViewById(R.id.recyclerview_home);
                            HomeAdapter customAdapter = new HomeAdapter(getContext(),mUsersUid,mUsersNames);
                            mRecyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                });



        return root;
    }

    public class HomeAdapter extends RecyclerView.Adapter<Home.HomeAdapter.HomeHolder> {

        private final LayoutInflater mInflater;
        private LinkedList<String> mUsersUid;
        private LinkedList<String> mUsersNames;
        private Context context;


        HomeAdapter(Context context, LinkedList<String> usersUid, LinkedList<String> usersName) {
            this.context = context;
            this.mUsersUid = usersUid;
            this.mUsersNames = usersName;
            mInflater = LayoutInflater.from(context);

            Log.e("TESTUSERS", "taille : "+this.mUsersUid.size());
            Log.e("TESTUSERS", "taille : "+this.mUsersNames.size());


        }

        @Override
        public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.home_recyclerview_item, parent, false);
            return new HomeHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final Home.HomeAdapter.HomeHolder holder, int position) {
            holder.name.setText(mUsersNames.get(position));
            Log.i("TESTUSERS","nom mis à jour");




            //Get images from FirebaseStorage
            mStorageRef = FirebaseStorage.getInstance().getReference();
            String name = mStorageRef.child(mAuth.getCurrentUser().getUid()).getName();
            ref = mStorageRef.child("usersphotos/"+mUsersUid.get(position));

            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.e("TESTUSERS", "user image good");

                    Picasso.get().load(uri).rotate(90).resize(150,105).into(holder.image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TESTUSERS", e.getMessage());

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
        @Override
        public int getItemCount() {
            return mUsersUid.size();
        }


        class HomeHolder extends RecyclerView.ViewHolder {
            TextView name;
            ImageView image;

            public HomeHolder(View itemView) {
                super(itemView);
                // get the reference of item view's
                name = (TextView) itemView.findViewById(R.id.user_name);
                image = (ImageView) itemView.findViewById(R.id.user_image);
            }


        }
    }
    public void updateListsUsers(){

    }

}
