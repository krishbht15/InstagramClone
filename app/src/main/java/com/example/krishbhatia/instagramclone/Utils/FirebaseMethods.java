package com.example.krishbhatia.instagramclone.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.krishbhatia.instagramclone.R;
import com.example.krishbhatia.instagramclone.models.User;
import com.example.krishbhatia.instagramclone.models.UserAccountSettings;
import com.example.krishbhatia.instagramclone.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";
    private Context mContext;
    private FirebaseAuth mAuth;
    private String UId;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    public FirebaseMethods(Context context){
        mAuth=FirebaseAuth.getInstance();
        mContext=context;
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();

        if(mAuth.getCurrentUser()!=null){
            UId=mAuth.getCurrentUser().getUid();
        }

    }
//    public boolean  checkIfUSernameExists(String username, DataSnapshot dataSnapshot){
//        Log.d(TAG, "checkIfUSernameExists: checking if "+username+" is available or not");
//        User user= new User();
//        for (DataSnapshot dataSnapshot1:dataSnapshot.child("users").getChildren()){
//            Log.d(TAG, "checkIfUSernameExists: datasnapshots"+dataSnapshot);
//            user.setUsername(dataSnapshot1.getValue(User.class).getUsername());
//            Log.d(TAG, "checkIfUSernameExists: cusername"+user.getUsername());
//
//            if(StringManipulation.expandingUsername(user.getUsername()).equals(username)){
//                Log.d(TAG, "checkIfUSernameExists: WE FOUND A MATCH");
//                return true;
//            }
//        }
//        return false;
//    }

    public void sendVerficationEmail(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else {
                        Toast.makeText(mContext, "Couldn't send verification code" , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void registerNewEmail(String email,String password,String username){


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            sendVerficationEmail();
                            UId=mAuth.getUid();
                            Log.d(TAG, "createUserWithEmail:success"+UId);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText( mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    public  void addNewUser(String email,String username,String description,String website,String profilePhote){
        User user = new User(email,UId,StringManipulation.condensingUsername(username),1);
        Log.d(TAG, "addNewUser: adding new user");
        myRef.child(mContext.getString(R.string.dbname_users)).child(UId ).setValue(user);
        UserAccountSettings userAccountSettings= new UserAccountSettings(description,username,0,0,0,"",StringManipulation.condensingUsername(username),website );
        Log.d(TAG, "addNewUser: adding new user account");

        myRef.child("user_account_settings").child(UId).setValue(userAccountSettings);
    }
    public void updateUsername(String username){
        Log.d(TAG, "updateUsername: udating username");
        myRef.child("users"/*mContext.getString(R.string.dbname_users)*/).child(UId).child(mContext.getString(R.string.field)).setValue(username);
        myRef.child("user_account_settings"/*mContext.getString(R.string.dbname_users_account_settings)*/).child(UId).child(mContext.getString(R.string.field)).setValue(username);

    }
    public void updateUserAccountSettings(String displayname,String website,String description,long phoneNumber){
        if(displayname!=null){
            myRef.child("user_account_settings"/*mContext.getString(R.string.dbname_users_account_settings)*/).child(UId).child("display_name").setValue(displayname);

        }
        if(website!=null) {

            myRef.child("user_account_settings"/*mContext.getString(R.string.dbname_users_account_settings)*/).child(UId).child("website").setValue(website);
        }
        if(description!=null) {

            myRef.child("user_account_settings"/*mContext.getString(R.string.dbname_users_account_settings)*/).child(UId).child("description").setValue(description);
        }
        if(phoneNumber!=0) {

            myRef.child("user_account_settings"/*mContext.getString(R.string.dbname_users_account_settings)*/).child(UId).child("phone_number").setValue(phoneNumber);
        }

    }
    public void updateEmail(String email){
        Log.d(TAG, "updateUsername: udating email");
        myRef.child("users"/*mContext.getString(R.string.dbname_users)*/).child(UId).child(mContext.getString(R.string.field_email)).setValue(email);

    }

    public UserSettings getUserSettings(DataSnapshot dataSnapshot) {
        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();

        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
            if (dataSnapshot1.getKey().equals("user_account_settings")) {
                Log.d(TAG, "getUserAccountSettings:datasnapshots  " + dataSnapshot1);
                try {
                    settings.setDisplay_name(dataSnapshot1.child(UId).getValue(UserAccountSettings.class).getDisplay_name());

                    settings.setUsername(dataSnapshot1.child(UId).getValue(UserAccountSettings.class).getUsername());

                    settings.setWebsite(dataSnapshot1.child(UId).getValue(UserAccountSettings.class).getWebsite());
                    settings.setDescription(dataSnapshot1.child(UId).getValue(UserAccountSettings.class).getDescription());
                    settings.setProfile_photo(dataSnapshot1.child(UId).getValue(UserAccountSettings.class).getProfile_photo());
                    settings.setFollowers(dataSnapshot1.child(UId).getValue(UserAccountSettings.class).getFollowers());
                    settings.setFollowing(dataSnapshot1.child(UId).getValue(UserAccountSettings.class).getFollowing());
                    settings.setPosts(dataSnapshot1.child(UId).getValue(UserAccountSettings.class).getPosts());


                } catch (NullPointerException e) {

                }

            }
            if (dataSnapshot1.getKey().equals("users")) {
                Log.d(TAG, "getUserAccountSettings:datasnapshots  " + dataSnapshot1);
                try {

                    user.setUsername(dataSnapshot1.child(UId).getValue(User.class).getUsername());
                    user.setEmail(dataSnapshot1.child(UId).getValue(User.class).getEmail());
                    user.setPhone_number(dataSnapshot1.child(UId).getValue(User.class).getPhone_number());
                    user.setUser_id(dataSnapshot1.child(UId).getValue(User.class).getUser_id());

                } catch (NullPointerException e) {

                }

            }
        }
    return  new UserSettings(user,settings);}
    }
