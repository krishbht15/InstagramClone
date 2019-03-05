package com.example.krishbhatia.instagramclone.Search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.krishbhatia.instagramclone.R;
import com.example.krishbhatia.instagramclone.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class SearchActivity extends AppCompatActivity {
    private Context mContext;
    public static final int ACTIVITY_NUM=1;
    private static final String TAG = "SearchActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext=SearchActivity.this;
        Log.d(TAG, "onCreate: searchis started");
        setupBottomNavigation();
    }
    private void setupBottomNavigation(){
        Log.d(TAG, "setupBottomNavigation:");
        BottomNavigationViewEx  bottomNavigationViewEx=findViewById(R.id.bottomnavigationview);
        BottomNavigationViewHelper.setupBottomnavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,bottomNavigationViewEx);
        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
