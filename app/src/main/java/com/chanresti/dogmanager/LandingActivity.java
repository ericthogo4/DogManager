package com.chanresti.dogmanager;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import static android.view.View.GONE;

public class LandingActivity extends AppCompatActivity implements LandingInterface{


    private TabLayout lActivityTabLayout;
    private ViewPager lActivityViewPager;
    private LandingActivityViewPagerAdapter lActivityViewPagerAdapter = new LandingActivityViewPagerAdapter(getSupportFragmentManager());
    private int tab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Float elevation=0.0f;
        getSupportActionBar().setElevation(elevation);

        lActivityViewPager = (ViewPager) findViewById(R.id.l_activity_viewpager);
        setupViewPager(lActivityViewPager);

        lActivityTabLayout = (TabLayout) findViewById(R.id.tabs);
        lActivityTabLayout.setupWithViewPager(lActivityViewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        lActivityViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lActivityViewPagerAdapter.notifyDataSetChanged();
    }

    private void setupViewPager(ViewPager viewPager) {
        lActivityViewPagerAdapter = new LandingActivityViewPagerAdapter(getSupportFragmentManager());
        lActivityViewPagerAdapter.addFragment(new RemindersFragment(), "REMINDERS");
        lActivityViewPagerAdapter.addFragment(new DogTrainingFragment(), "DOG TRAINING");
        lActivityViewPagerAdapter.addFragment(new ProfilesFragment(), "PROFILES");
        viewPager.setAdapter(lActivityViewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing_activity_options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.landing_activity_help:
                openHelp();
                return true;

            default:
                return false;
        }
    }


    protected void openHelp(){
        Intent homeToHelpActivityIntent = new Intent(LandingActivity.this,HelpActivity.class);
        startActivity(homeToHelpActivityIntent);
    }


    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);

        if(lActivityTabLayout.getVisibility()==View.VISIBLE){
            lActivityTabLayout.setVisibility(GONE);
        }

    }


    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);

        if(lActivityTabLayout.getVisibility()==GONE){
            lActivityTabLayout.setVisibility(View.VISIBLE);
        }

    }

    public void hideActionBar(){
        getSupportActionBar().hide();
        if(lActivityTabLayout.getVisibility()==View.VISIBLE){
            lActivityTabLayout.setVisibility(GONE);
        }
    }

    public void showActionBar(){
        getSupportActionBar().show();
        if(lActivityTabLayout.getVisibility()==GONE){
            lActivityTabLayout.setVisibility(View.VISIBLE);
        }
    }


    protected class LandingActivityViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public LandingActivityViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }



}


