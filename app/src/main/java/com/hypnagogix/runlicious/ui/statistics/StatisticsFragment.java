package com.hypnagogix.runlicious.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hypnagogix.runlicious.R;
import com.hypnagogix.runlicious.VOL1Fragment;
import com.hypnagogix.runlicious.VOL2Fragment;
import com.hypnagogix.runlicious.VOL3Fragment;
import com.hypnagogix.runlicious.VOL4Fragment;

public class StatisticsFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        BottomNavigationView bottomNav = root.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setBackgroundResource(R.color.BottomNavBarColor);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new VOL1Fragment()).commit();

        return root;
    }



    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {



                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_vol1_over_all_stats:
                            selectedFragment = new VOL1Fragment();
                            break;
                        case R.id.nav_vol2_by_week_stats:
                            selectedFragment = new VOL2Fragment();
                            break;
                        case R.id.nav_vol3_by_month_stats:
                            selectedFragment = new VOL3Fragment();
                            break;
                        case R.id.nav_vol4_by_year_stats:
                            selectedFragment = new VOL4Fragment();
                            break;
                    }
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };;
}
