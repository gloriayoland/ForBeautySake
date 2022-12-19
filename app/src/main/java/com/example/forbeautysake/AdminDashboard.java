package com.example.forbeautysake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.forbeautysake.nav_fragment.AdminFeedFragment;
import com.example.forbeautysake.nav_fragment.AdminHomeFragment;
import com.example.forbeautysake.nav_fragment.AdminProfileFragment;
import com.example.forbeautysake.nav_fragment.FeedFragment;
import com.example.forbeautysake.nav_fragment.HomeFragment;
import com.example.forbeautysake.nav_fragment.ProfileFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class AdminDashboard extends AppCompatActivity {

    // define variables
    private ChipNavigationBar chipnav;
    private Fragment fragment = null;

    ImageView addReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // find components by id according to the defined variable
        addReview = findViewById(R.id.addRev);
        chipnav = findViewById(R.id.chipnav);

        // set item selected in chipnav which is Home fragment
        chipnav.setItemSelected(R.id.nav_home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new HomeFragment()).commit();

        //set on item selected in chipnav to change the page according to the user select
        chipnav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {

            @Override
            public void onItemSelected(int i) {

                //switch page on dashboard
                switch(i){
                    case R.id.nav_feed:
                        fragment = new AdminFeedFragment();
                        break;
                    case R.id.nav_home:
                        fragment = new AdminHomeFragment();
                        break;
                    case R.id.nav_profile:
                        fragment = new AdminProfileFragment();
                        break;
                }

                //set fragment
                if (fragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, fragment).commit();

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, fragment).commit();
            }
        });

    }
}