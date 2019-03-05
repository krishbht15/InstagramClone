package com.example.krishbhatia.instagramclone.Login;

import android.content.Context;
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

import com.example.krishbhatia.instagramclone.R;
import com.example.krishbhatia.instagramclone.Utils.FirebaseMethods;
import com.example.krishbhatia.instagramclone.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    private Context mContext;
    private EditText mEmail, mPassword, mUsername;
    private String email, password, username;
    private TextView loading;
    private Button register;
    private ProgressBar mProgressBar;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String append = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mContext = RegisterActivity.this;


        firebaseMethods = new FirebaseMethods(mContext);
        initializeWidget();

        init();
        componentsIntialise();

    }

    private void init() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: register is clicked");

                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                username = mUsername.getText().toString();
                if (isNull(email) || isNull(password) || isNull(password)) {
                    Toast.makeText(mContext, "All the fields must be filled", Toast.LENGTH_SHORT).show();

                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.VISIBLE);

                    firebaseMethods.registerNewEmail(email, password, username);
//                    componentsIntialise();

                }

            }
        });
        mProgressBar.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }

    private static boolean isNull(String string) {
        if (string.equals("")) {
            return true;
        }
        return false;
    }

    private void initializeWidget() {
        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mEmail = findViewById(R.id.input_emailid);
        mPassword = findViewById(R.id.input_password);
        mUsername = findViewById(R.id.input_name);
        loading = findViewById(R.id.register_please);
        mProgressBar = findViewById(R.id.loginRequestLoadingProgressbar);
        register = findViewById(R.id.btn_register);
        mProgressBar.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }

    private void componentsIntialise() {
        Log.d(TAG, "componentsIntialise: checking if user is null or not to upload information");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            checkIfUsernameExists(username);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
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

    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: checking if  " + username + " already exists");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbname_users)).orderByChild(getString(R.string.field)).equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot singleDataSnapshots : dataSnapshot.getChildren()) {
                    if (singleDataSnapshots.exists()) {
                        Log.d(TAG, "onDataChange: found a match" + singleDataSnapshots.getValue(User.class).getUsername());
                        append = myRef.push().getKey().substring(3, 10);
                        Log.d(TAG, "onDataChange: username already exists. Appending random string to name: " + append);

                    }
                }
                //1st check: Make sure the username is not already in use
                String mUsernamee = "";
                mUsernamee = username + append;

                //add new user to the database
                firebaseMethods.addNewUser(email, mUsernamee, "", "", "");

                Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

}


