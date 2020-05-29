package com.example.recipeworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

public class RegistrationActivity extends AppCompatActivity {

    private EditText mName,mEmailTextView, mPasswordTextView;
    private Button mBtn;
    private ProgressBar mProgressbar;
    private FirebaseAuth mAuth;

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
        mBtn = findViewById(R.id.btnregister);
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
                                    .build();
                            mAuth.getCurrentUser().updateProfile(request);

                            // hide the progress bar
                            mProgressbar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Registration successful!",Toast.LENGTH_LONG).show();


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
}
