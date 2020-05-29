package com.example.recipeworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class LoginAActivity extends AppCompatActivity {
    //instance
    private FirebaseAuth mAuth;

    //email,password login
    private EditText mEmailTextView, mPasswordTextView;
    private Button mLoginButton;

    //facebook loogin
    private LoginButton mLoginButtonFcbk;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListner;
    private CallbackManager mCallbackManager;

    //register
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if (isCurrentUserIsLogged()) {
            Intent intent = new Intent(this,MainActivity.class);
            this.startActivity(intent);
        }

        setContentView(R.layout.login_activity);

        /*FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);*/
        // taking instance of FirebaseAuth

        // initialising all views through id defined above
        mEmailTextView = findViewById(R.id.email);
        mPasswordTextView = findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.login);

        // Set on Click Listener on Sign-in button
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount();
            }
        });


        //Facebook login stuff
        mCallbackManager = CallbackManager.Factory.create();

        mLoginButtonFcbk = (LoginButton) findViewById(R.id.login_fcbk);
        mLoginButtonFcbk.setReadPermissions(Arrays.asList("email"));
        mLoginButtonFcbk.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });

        //
        this.mRegisterButton = findViewById(R.id.registerRedirect);
        this.mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // Login by email, password
    private void loginUserAccount()
    {

        // Take the value of two edit texts in Strings
        String email, password;
        email = mEmailTextView.getText().toString();
        password = mPasswordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Login successful!!",Toast.LENGTH_LONG).show();

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent = new Intent(LoginAActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }

                                else {

                                    // sign-in failed
                                    Log.e("Sign in",task.getException().toString());
                                    Toast.makeText(getApplicationContext(),"Login failed!!", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
    }


    //Login in Facebook
    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.i("FACEBOOK","Handle OK");
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("FACEBOOK","Handle Complete");

                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Error in signing with Facebook", Toast.LENGTH_LONG).show();
                }else{
                    Log.i("FACEBOOK","Intent launched");
                    Intent intent = new Intent(LoginAActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    //if there is a user logged
    protected Boolean isCurrentUserIsLogged() {
        return (this.mAuth.getCurrentUser() != null);
    }
}

