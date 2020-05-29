package com.example.recipeworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;


public class LoginActivity extends BaseActivity {

    private Button mRegister;
    private Button mSignIn;
    private Button mSignOut;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;



    //private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mRegister = (Button) findViewById(R.id.main_activity_button_register);
        this.mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (isCurrentUserIsLogged()) {
                    Log.i("SIIIGN IN",getCurrentUser().getEmail());
                    startApplication();
                } else {
                    Log.i("REGISTER","REGIIIIIISTER");
                    startRegisterActivity();
                }*/
                startRegistration();
            }
        });

        // Configure Google Sign In
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);*/
        this.mSignIn = findViewById(R.id.main_activity_button_sign_in);
        this.mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });
        this.mSignOut = findViewById(R.id.main_activity_button_sign_out);
        this.mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });  }


    /*public void updateWhenResuming(){
        this.mSignIn.setText(isCurrentUserIsLogged() ? "User Logged" : "User not logged");
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //updateWhenResuming();
    }

    //FOR DATA
    // 1 - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    // --------------------
    // ACTIONS
    // --------------------

    public int getFragmentLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void startRegistration(){
        Intent intent = new Intent (this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void startLogin(){
        Intent intent = new Intent (this, LoginAActivity.class);
        startActivity(intent);
    }

    // 2 - Launch Sign-In Activity
    private void startRegisterActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                              new AuthUI.IdpConfig.GoogleBuilder().build(),
                                              new AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_logo_auth)
                        .build(),RC_SIGN_IN);
    }

    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                showToast( getString(R.string.Registering_succeed));
            } else { // ERRORS
                if (response == null) {
                    showToast(getString(R.string.error_authentication_canceled));
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showToast(getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showToast(getString(R.string.error_unknown_error));
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }


}
