package com.example.krishbhatia.instagramclone.Utils;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionStatePagerAdapter extends FragmentStatePagerAdapter {
    private List mFragmentList=new ArrayList();
    private HashMap<Fragment,Integer> mFragments=new HashMap<>();
    private HashMap<String,Integer> mFragmentsNumber=new HashMap<>();
    private HashMap<Integer,String> mFragmentName=new HashMap<>();
    public SectionStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
    return (Fragment) mFragmentList.get(i);
    }

    @Override
    public int getCount() {
     return mFragmentList.size();
    }

    public void addFragment(Fragment fragment,String name){
        mFragmentList.add(fragment);
        mFragments.put(fragment,mFragmentList.size()-1);
        mFragmentsNumber.put(name,mFragmentList.size()-1);
        mFragmentName.put(mFragmentList.size()-1,name);
    }
    public Integer getFragmentNumber(String fragmentName){
        if(mFragmentsNumber.containsKey(fragmentName)){
            return mFragmentsNumber.get(fragmentName);
        }
        else {
            return null;
        }
    }

    public Integer getFragmentNumber(Fragment fragment){
        if(mFragments.containsKey(fragment)){
            return mFragmentsNumber.get(fragment);
        }
        else {
            return null;
        }
    }

    public String  getFragmentName(Integer number){
        if(mFragmentName.containsKey(number)){
            return mFragmentName.get(number);
        }
        else {
            return null;
        }
    }
}
