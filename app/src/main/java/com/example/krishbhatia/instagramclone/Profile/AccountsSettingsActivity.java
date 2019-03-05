package com.example.krishbhatia.instagramclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.krishbhatia.instagramclone.R;
import com.example.krishbhatia.instagramclone.Utils.BottomNavigationViewHelper;
import com.example.krishbhatia.instagramclone.Utils.FirebaseMethods;
import com.example.krishbhatia.instagramclone.Utils.SectionStatePagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class AccountsSettingsActivity extends AppCompatActivity {
    private static final String TAG = "AccountsSettingsActivit";
    private static final int ACTIVITY_NUM=4;

    Context mContext;
    private SectionStatePagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private RelativeLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        
        viewPager=findViewById(R.id.centerview);
        layout=findViewById(R.id.relLayout1);
        mContext=AccountsSettingsActivity.this;
        Log.d(TAG, "onCreate: started");
        ImageView backarrow=findViewById(R.id.blackArrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: going back");
                finish();
            }
        });
        setupBottomNavigation();
        setupSettingsLis();

        setupFrqagment();
        getincomingIntent();
    }
    public void getincomingIntent(){
        Intent intent=getIntent();

        if(intent.hasExtra(getString(R.string.calling_activity))){
            setViewPager(pagerAdapter.getFragmentNumber(getString(R.string.edit_profile_fragment)));
        }

    }
    private void setViewPager(int FragmentNumber){
        layout.setVisibility(View.GONE);
        Log.d(TAG, "setViewPager: navigating through number");
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(FragmentNumber);

    }
    public void setupFrqagment(){
        pagerAdapter=new SectionStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragments(),getString(R.string.edit_profile_fragment));
        

        pagerAdapter.addFragment(new SignOutFragment(),getString(R.string.sign_out));
    }
    private void setupSettingsLis(){
        Log.d(TAG, "setupSettingsLis: seeting up list");
        ListView listView=findViewById(R.id.listAccountSettings);
        ArrayList<String> options=new ArrayList<>();

        options.add(getString(R.string.edit_profile_fragment));
        options.add(getString(R.string.sign_out));

        ArrayAdapter adapter=new ArrayAdapter(mContext,android.R.layout.simple_list_item_1,options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setViewPager(position);
            }
        });
    }
    private void setupBottomNavigation(){
        Log.d(TAG, "setupBottomNavigation:");
        BottomNavigationViewEx bottomNavigationViewEx= findViewById(R.id.bottomnavigationview);
        BottomNavigationViewHelper.setupBottomnavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,bottomNavigationViewEx);
        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
