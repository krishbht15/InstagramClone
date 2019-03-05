package com.example.krishbhatia.instagramclone.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krishbhatia.instagramclone.Home.HomeActivity;
import com.example.krishbhatia.instagramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText email, password;
    private Button login;
    private TextView message;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        email = findViewById(R.id.input_emailid);
        password = findViewById(R.id.input_password);
        message = findViewById(R.id.pleasewaitsignin);
        progressBar = findViewById(R.id.loginRequestLoadingProgressbar);

        progressBar.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        setupFirebaseAuth();
        checkingUserisreg();


    }

    private static boolean isNull(String string) {
        if (string.equals("")) {
            return true;
        }
        return false;
    }

    public void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void checkingUserisreg(){
        login=findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaill=email.getText().toString();
                String passwordd=password.getText().toString();
                if(isNull(emaill) || isNull(passwordd)){
                    Toast.makeText(mContext,"All the fields must be filled",Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    message.setVisibility(View.VISIBLE);


                    mAuth.signInWithEmailAndPassword(emaill, passwordd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                        try {
                                            if(user.isEmailVerified()){
                                                Log.d(TAG, "onComplete: email is verified");
                                                Toast.makeText(mContext, "Authentication success.",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent i=new Intent(mContext,HomeActivity.class);
                                                startActivity(i);
                                            }
                                            else {
                                                Toast.makeText(mContext, "Email isn't verified. PLease Check your mail", Toast.LENGTH_SHORT).show();

                                                progressBar.setVisibility(View.GONE);
                                                message.setVisibility(View.GONE);
                                                mAuth.signOut();
                                            }
                                        }
                                        catch (NullPointerException e){
                                            Log.d(TAG, "onComplete: null pointer exception");
                                        }


                                        progressBar.setVisibility(View.GONE);
                                        message.setVisibility(View.GONE);


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                                    }


                                }
                            });

                }


            }
        });
        TextView createAccount=findViewById(R.id.link_signup);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(mContext,RegisterActivity.class);
                startActivity(i);
            }
        });

        if(mAuth.getCurrentUser()!=null){
            Intent i=new Intent(mContext,HomeActivity.class);
            startActivity(i);
            finish();
        }

    }
//    private void updateUI(FirebaseUser currentUser) {
//        Log.d(TAG, "updateUI: calling update UI");
//        if(currentUser==null){
//            Intent i=new Intent(mContext,LoginActivity.class);
//            startActivity(i);
//        }
//        else {
//            Toast.makeText(mContext, "Authentication failed.",
//                    Toast.LENGTH_SHORT).show();
//
//        }
//    }
}
