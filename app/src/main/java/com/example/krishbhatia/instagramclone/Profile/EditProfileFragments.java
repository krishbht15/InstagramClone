package com.example.krishbhatia.instagramclone.Profile;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krishbhatia.instagramclone.R;
import com.example.krishbhatia.instagramclone.Utils.FirebaseMethods;
import com.example.krishbhatia.instagramclone.Utils.UniversalImageLoader;
import com.example.krishbhatia.instagramclone.dialogs.ConfirmPwdDialog;
import com.example.krishbhatia.instagramclone.models.User;
import com.example.krishbhatia.instagramclone.models.UserAccountSettings;
import com.example.krishbhatia.instagramclone.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragments extends Fragment implements ConfirmPwdDialog.OnConfirmPassword {
    private static final String TAG = "EditProfileFragments";
    private ImageView mProfilePhote;
    private EditText mDisplayName, mUsername, mWebsite, mDescription, mEmail, mPhoneNumber;
    private TextView mChangeProfilePhoto;
    private CircleImageView mProfilePhoto;
    private UserSettings mUserSettings;
    private String userID;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseMethods mFirebaseMethods;

    private DatabaseReference myRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_editprofile,container,false);
        mProfilePhote=view.findViewById(R.id.profilephoto);
        mFirebaseMethods=new FirebaseMethods(getActivity());

        mDisplayName = (EditText) view.findViewById(R.id.display_name);
        mUsername = (EditText) view.findViewById(R.id.username);
        mWebsite = (EditText) view.findViewById(R.id.website);
        mDescription = (EditText) view.findViewById(R.id.description);
        mEmail = (EditText) view.findViewById(R.id.email);
        mPhoneNumber = (EditText) view.findViewById(R.id.phonenumber);
        mChangeProfilePhoto = (TextView) view.findViewById(R.id.changeprofilephoto);
        mFirebaseMethods = new FirebaseMethods(getActivity());

        ImageView checkSave=view.findViewById(R.id.savechanges);

        checkSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes");
                saveProfileSettings();
            }
        });

        ImageView backbutton= view.findViewById(R.id.backArrow);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

//        setProfileImage();
            setupFirebaseAuth();
            userID=mAuth.getUid();
        return view;
    }
    private void saveProfileSettings(){
        final String displayName = mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());
//    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//

            if(!mUserSettings.getUser().getUsername().equals(username)){
                checkIfUsernameExists(username);
            }
            if(!mUserSettings.getUser().getEmail().equals(email)){
                ConfirmPwdDialog confirmPwdDialog=new ConfirmPwdDialog();
                confirmPwdDialog.show(getFragmentManager(),"ConfirmPwdDialog");
                confirmPwdDialog.setTargetFragment(EditProfileFragments.this,1);
            }
            if(!mUserSettings.getSettings().getDisplay_name().equals(displayName)){
                mFirebaseMethods.updateUserAccountSettings(displayName,null ,null,0);
            }
            if(!mUserSettings.getSettings().getWebsite().equals(website)){
                mFirebaseMethods.updateUserAccountSettings(null,website ,null,0);

            }
        if(!mUserSettings.getSettings().getDescription().equals(description)) {
            mFirebaseMethods.updateUserAccountSettings(displayName,null ,null,0);

        }
        }


//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    });}

    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: checking if  " + username+ " already exists");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        Query  query=reference.child("users").orderByChild(getString(R.string.field)).equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    mFirebaseMethods.updateUsername(username );
                    Toast.makeText(getActivity(), "Adding this username.", Toast.LENGTH_SHORT).show();

                }
                for (DataSnapshot singleDataSnapshots:dataSnapshot.getChildren()){
                    if(singleDataSnapshots.exists()){
                        Log.d(TAG, "onDataChange: found a match"+singleDataSnapshots.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "Username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupProfileWidget(UserSettings userSettings){
        Log.d(TAG, "setupProfileWidget: retrieving db from firebasse");
//        User user=userSettings.getUser();
        UserAccountSettings settings=userSettings.getSettings();

//        UniversalImageLoader.setImage(settings.getProfile_photo(),mProfilePhoto,null,"");
        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
       mEmail.setText(userSettings.getUser().getEmail());
       mPhoneNumber.setText(String.valueOf(userSettings.getUser().getPhone_number()));
       mUserSettings=userSettings;


    }


    private void setProfileImage(){
        Log.d(TAG, "setProfileImage: setting profile image");
        String imgUrl="https://images.techhive.com/images/article/2017/01/google-android-apps-100705848-large.jpg";
        UniversalImageLoader.setImage(imgUrl,mProfilePhote,null,"");

    }
    /**
     * Setup the firebase auth object
     */

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                setupProfileWidget(mFirebaseMethods.getUserSettings(dataSnapshot));
                //retrieve info from db

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    @Override
    public void onConfirmpwd(String password) {
        Log.d(TAG, "onConfirmpwd: the password is '" +password+"'");

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(), password);

// Prompt the user to re-provide their sign-in credentials
        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: re-Authenticated");
                            mAuth.fetchProvidersForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                    if(task.isSuccessful()){
                                        try {


                                            if (task.getResult().getProviders().size() == 1) {
                                                Toast.makeText(getActivity(), "that eamil is already in use", Toast.LENGTH_SHORT).show();
                                            } else if (task.getResult().getProviders().size() == 0) {
                                                Log.d(TAG, "onComplete: that email is available");
                                                Toast.makeText(getActivity(), "that email is available", Toast.LENGTH_SHORT).show();

                                                mAuth.getCurrentUser().updateEmail(mEmail.getText().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "User email address updated.");
                                                                    mFirebaseMethods.updateEmail(mEmail.getText().toString());
                                                                    Toast.makeText(getActivity(), "email is updated", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                        catch (NullPointerException e){
                                            Log.d(TAG, "onComplete: exception is caught");
                                        }
                                    }
                                }
                            });
                            }
                        else{
                            Log.d(TAG, "onComplete: re-Authentication failed");
                        }
                    }
                });
    }
}
