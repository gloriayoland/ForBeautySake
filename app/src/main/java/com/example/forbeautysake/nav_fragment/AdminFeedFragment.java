package com.example.forbeautysake.nav_fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.forbeautysake.Adapter.SectionPagerAdapter;
import com.example.forbeautysake.R;
import com.example.forbeautysake.feed_fragment.MyReviewFragment;
import com.example.forbeautysake.feed_fragment.PublicReviewFragment;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class AdminFeedFragment extends Fragment {
    // define variables
    TabLayout tabLayout;
    TabItem publicReview;
    ViewPager vp;

    View myFragment;

    public AdminFeedFragment (){

    }

    public static FeedFragment getInstance(){
        return new FeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_admin_feed, container, false);

        // find components by id according to the defined variable
        vp = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);
        publicReview = myFragment.findViewById(R.id.publicReview);


        return myFragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set view pager
        setUpViewPager(vp);
        tabLayout.setupWithViewPager(vp);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        //set adapter for view pager
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
    }

}
