package com.example.recipeworld.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileUpdate extends Fragment {

    private static final String TAG = "ProfileUpdate";

    CircleImageView profileImageView;
    TextInputEditText displayNameEditText;
    TextInputEditText displayNameEditTextEmail;
    Button updateProfileButton;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    String DISPLAY_NAME = null;
    String DISPLAY_EMAIL = null;
    String PROFILE_IMAGE_URL = null;
    int TAKE_IMAGE_CODE = 10001;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_update, container, false);
        
        this.mAuth = FirebaseAuth.getInstance();
        profileImageView = root.findViewById(R.id.profileImageView);
        displayNameEditText = root.findViewById(R.id.displayNameEditText);
        displayNameEditTextEmail = root.findViewById(R.id.displayNameEditTextEmail);
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
                Glide.with(this)
                        .load(this.mAuth.getCurrentUser().getPhotoUrl())
                        .into(profileImageView);
            }else{
                Glide.with(this)
                        .load(R.drawable.user)
                        .into(profileImageView);
            }
        }
        this.updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(v);
            }
        });
        progressBar = root.findViewById(R.id.progressBar);

        // UPDATING THE INFORMATION OF THE USER





        progressBar.setVisibility(View.GONE);

        return root;
    }

    public void updateProfile(final View view) {

        view.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        DISPLAY_NAME = displayNameEditText.getText().toString();
        DISPLAY_EMAIL = displayNameEditTextEmail.getText().toString();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(DISPLAY_NAME)
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
                                            Toast.makeText(getActivity(), "Succesfully updated profile", Toast.LENGTH_SHORT).show();
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

    public void storageDemo(View view) {

//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference reference = storage.getReference();
//        StorageReference horseRef = reference.child("horse__.jpg");
//        StorageReference horseRef = storage.getReference().child("images").child("girl.jpg");
//        StorageReference horseRef = storage.getReference().child("images").child("profileImages").child("123.jpg");
//        Bitmap horseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.horse);
//        ByteArrayOutputStream boas = new ByteArrayOutputStream();
//        horseBitmap.compress(Bitmap.CompressFormat.JPEG, 20, boas);
//        horseRef.putBytes(boas.toByteArray())
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(ProfileActivity.this, "upload succesfull", Toast.LENGTH_SHORT).show();
//                    }
//                });

//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference imageRef = storage.getReference()
//                .child("images")
//                .child("profileImages")
//                .child("123.jpg");

//        imageRef.getBytes(1024*1024)
//                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                        profileImageView.setImageBitmap(bitmap);
//                    }
//                });
//        imageRef.getDownloadUrl()
//                .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Log.d(TAG, "Download url is: " + uri.toString());
//                    }
//                });
    }

    public void handleImageClick(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_IMAGE_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profileImageView.setImageBitmap(bitmap);
                    handleUpload(bitmap);
            }
        }
    }

    private void handleUpload(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid + ".jpeg");

        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ",e.getCause() );
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: " + uri);
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Updated succesfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Profile image failed...", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}