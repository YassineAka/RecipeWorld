package com.example.recipeworld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mName,mEmailTextView, mPasswordTextView;
    ImageView mImageRegister;
    private Button mBtn,mBtnImage;
    private ProgressBar mProgressbar;
    private Uri mUri;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    int TAKE_IMAGE_CODE = 10001;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        mName = findViewById(R.id.name);
        mEmailTextView = findViewById(R.id.email);
        mPasswordTextView = findViewById(R.id.passwd);
        mBtnImage = findViewById(R.id.register_image_btn);
        mImageRegister = findViewById(R.id.register_image);
        mBtn = findViewById(R.id.btnregister);
        mBtnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick();
            }
        });
        mProgressbar = findViewById(R.id.progressbar);

        // Set on Click Listener on Registration button
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });
    }

    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        mProgressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = mEmailTextView.getText().toString();
        password = mPasswordTextView.getText().toString();
        // Validations for input email and password
        if (TextUtils.isEmpty(mName.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    "Please enter name!!",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",Toast.LENGTH_LONG).show();
            return;
        }

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password)
             .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            //update name
                            final UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(mName.getText().toString())
                                    .setPhotoUri(mUri)
                                    .build();
                            mAuth.getCurrentUser().updateProfile(request);

                            // hide the progress bar
                            mProgressbar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Registration successful!",Toast.LENGTH_LONG).show();

                            Map<String, Object> user = new HashMap<>();
                            user.put("uid", mAuth.getCurrentUser().getUid());
                            user.put("name",mName.getText().toString());
                            db.collection("users")
                                    .add(user);

                            StorageReference ref = storageReference.child("usersphotos/"+mAuth.getCurrentUser().getUid());
                            ref.putFile(mUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            Log.i( "Image", "Okayy");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i( "Image", "Not okayy");

                                        }
                                    });


                            Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Log.i("Registration",task.getException().toString());
                            // hide the progress bar
                            mProgressbar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
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
                this.mUri = data.getData();
                Uri uri = this.mUri;
                Picasso.get().load(mUri).fit().centerCrop().into(this.mImageRegister);
            }
        }
    }
}
