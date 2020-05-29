package com.example.recipeworld.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipeworld.R;
import com.google.firebase.auth.FirebaseAuth;


public class Profile extends Fragment {

        private ImageView imageViewProfile;
        private TextView textInputEditTextUsername;
        //private TextView textViewEmail;
        private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        this.mAuth = FirebaseAuth.getInstance();
        this.imageViewProfile = root.findViewById(R.id.profile_activity_imageview_profile);
        this.textInputEditTextUsername = root.findViewById(R.id.profile_activity_edit_text_username);
        //this.textViewEmail = root.findViewById(R.id.profile_activity_text_view_email);
        this.updateUIWhenCreating();
            //this.configureToolbar();

        return root;
        }



    private void updateUIWhenCreating(){

        if (this.mAuth.getCurrentUser() != null){

            //Get picture URL from Firebase
            if (this.mAuth.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.mAuth.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewProfile);
            }else{
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




}
