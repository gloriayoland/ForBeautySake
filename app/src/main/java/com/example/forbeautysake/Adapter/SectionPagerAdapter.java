package com.example.forbeautysake.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.forbeautysake.feed_fragment.MyReviewFragment;
import com.example.forbeautysake.feed_fragment.PublicReviewFragment;

import java.util.ArrayList;


public class SectionPagerAdapter extends FragmentPagerAdapter {

    //define variables
    private int numOfTabs;
    private ArrayList<String> title;

    //constructor
    // section page adapter is for input public review fragment and my review fragment
    public SectionPagerAdapter( FragmentManager fm, int numOfTabs) {

        super(fm);
        this.numOfTabs = numOfTabs;
        title = new ArrayList<>();
        title.add("Public Review");
        title.add("My Review");
    }

    @Override
    public Fragment getItem(int position) {

        //switch position of public review fragment and my review fragment
        switch (position){
            case 0:
                return new PublicReviewFragment();
            case 1:
                return new MyReviewFragment();
            default:
                return null;
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    @Override
    public int getCount() {

        return numOfTabs;
    }

}
