package com.example.recipeworld.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.example.recipeworld.R;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileUpdate extends Fragment {

    private static final String TAG = "ProfileUpdate";

    private CircleImageView profileImageView;
    private TextInputEditText displayNameEditText;
    private TextInputEditText displayNameEditTextEmail;
    private Uri uri;
    private Button updateProfileButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    private String DISPLAY_NAME = null;
    private String DISPLAY_EMAIL = null;
    int TAKE_IMAGE_CODE = 10001;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_update, container, false);
        
        this.mAuth = FirebaseAuth.getInstance();
        profileImageView = root.findViewById(R.id.profileImageView);
        displayNameEditText = root.findViewById(R.id.displayNameEditText);
        displayNameEditTextEmail = root.findViewById(R.id.displayNameEditTextEmail);
        progressBar = root.findViewById(R.id.progressBar);
        updateProfileButton = root.findViewById(R.id.updateProfileButton);
        if (this.mAuth.getCurrentUser() != null) {
            Log.d(TAG, "onCreate: " + this.mAuth.getCurrentUser().getDisplayName());
            if (this.mAuth.getCurrentUser().getDisplayName() != null ) {
                displayNameEditText.setText(this.mAuth.getCurrentUser().getDisplayName());
                displayNameEditText.setSelection(this.mAuth.getCurrentUser().getDisplayName().length());
                displayNameEditTextEmail.setText(this.mAuth.getCurrentUser().getEmail());
                displayNameEditTextEmail.setSelection(this.mAuth.getCurrentUser().getEmail().length());
            }
            if (this.mAuth.getCurrentUser().getPhotoUrl() != null) {
                Log.i("IMAGEUPDATE","EXIIIIIIIIIIST");
                this.uri =this.mAuth.getCurrentUser().getPhotoUrl();
                        Glide.with(this)
                        .load(this.uri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profileImageView);
            }else{
                Log.i("IMAGEUPDATE","DONT EXIIIIIIIIIIST");
                this.uri = Uri.parse("drawable/user.png");
                Glide.with(this)
                        .load(R.drawable.user)
                        .into(profileImageView);
            }


        }
        this.updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(v,uri);
            }
        });

        this.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick();
            }
        });


        // UPDATING THE INFORMATION OF THE USER





        progressBar.setVisibility(View.GONE);

        return root;
    }

    public void updateProfile(final View view,Uri uri) {

        view.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        DISPLAY_NAME = displayNameEditText.getText().toString();
        DISPLAY_EMAIL = displayNameEditTextEmail.getText().toString();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(DISPLAY_NAME)
                .setPhotoUri(uri)
                .build();

        user.updateEmail(DISPLAY_EMAIL)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("EMAIL", "User email address updated.");
                            user.updateProfile(request)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            view.setEnabled(true);
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getContext(), "Succesfully updated profile", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            view.setEnabled(true);
                                            progressBar.setVisibility(View.GONE);
                                            Log.e(TAG, "onFailure: ", e.getCause());
                                        }
                                    });
                        }else{
                            view.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_LONG).show();
                            Log.d("EMAIL", "User email address failed.");
                            Log.d("EMAIL", task.getException().toString());
                        }
                    }
                });


    }




    public void handleImageClick() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,TAKE_IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Log.i("Pick-picture", "Ok je suis rentré");
            if (requestCode == TAKE_IMAGE_CODE) {
                this.uri = data.getData();
                Picasso.get().load(uri).fit().centerCrop().into(this.profileImageView);
            }
        }
    }




    public Uri getUriFromBitmap(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}