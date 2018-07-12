package nl.soft.pelorus.pelorus3.ui.race;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.injection.PelorusFactory;
import nl.soft.pelorus.pelorus3.ui.mainmenu.MainMenuActivity;
import nl.soft.pelorus.pelorus3.ui.race.dashboard.DashboardFragment;
import nl.soft.pelorus.pelorus3.ui.race.leaderboard.LeaderboardFragment;
import nl.soft.pelorus.pelorus3.ui.race.map.MapFragment;

/**
 * Created by tobia on 1-9-2017.
 */

public class RaceActivity extends LifecycleActivity {
    private ViewPager pager;
    private TabLayout tabs;

    private TabsPagerAdapter pagerAdapter;

    private FloatingActionButton floatingActionButtonEndRace;

    private RaceViewModel raceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PelorusApplication pelorusApplication = (PelorusApplication) getApplication();
        raceViewModel = ViewModelProviders.of(this, new PelorusFactory(pelorusApplication))
                .get(RaceViewModel.class);

        raceViewModel.getMarks().observe(this,marks -> {
            if(!marks.isEmpty()){
                raceViewModel.setMarkList(marks);
            }
        });

        setContentView(R.layout.activity_race);

        tabs = (TabLayout) findViewById(R.id.tabs_race);
        pager = (ViewPager) findViewById(R.id.pager_race);
        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(pager);

        floatingActionButtonEndRace = (FloatingActionButton) findViewById(R.id.fab_end_race);
        floatingActionButtonEndRace.setOnClickListener(v1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
            builder.setMessage("Are you sure you want to leave the race?")
                    .setTitle("End Race?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            raceViewModel.unregisterCallbacks();
                            raceViewModel.stopLocationUpdates();
                            raceViewModel.disconnect();
                            startActivity(new Intent(RaceActivity.this, MainMenuActivity.class));
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        raceViewModel.connect();
        raceViewModel.registerCallbacksAndStartLocationUpdates(this);



    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        raceViewModel.unregisterCallbacks();
        raceViewModel.stopLocationUpdates();
        raceViewModel.disconnect();
    }

    private class TabsPagerAdapter extends FragmentStatePagerAdapter {
        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new DashboardFragment();
                case 1: return new LeaderboardFragment();
                case 2: return new MapFragment();
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position){
                case 0: title = "Dashboard";
                    break;
                case 1: title = "LeaderBoard";
                    break;
                case 2: title = "Map";
                    break;
            }
            return title;
        }
    }
}
