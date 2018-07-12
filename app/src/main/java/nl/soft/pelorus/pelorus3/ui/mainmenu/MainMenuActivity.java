package nl.soft.pelorus.pelorus3.ui.mainmenu;

import android.app.ActionBar;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;

import nl.soft.pelorus.pelorus3.injection.PelorusFactory;
import nl.soft.pelorus.pelorus3.ui.boats.BoatsActivity;
import nl.soft.pelorus.pelorus3.ui.boats.BoatsFragment;
import nl.soft.pelorus.pelorus3.ui.boats.manage.ManageBoatActivity;
import nl.soft.pelorus.pelorus3.ui.mainmenu.events.EventsFragment;
import nl.soft.pelorus.pelorus3.ui.mainmenu.profile.ViewProfileFragment;
import nl.soft.pelorus.pelorus3.ui.race.RaceActivity;


/**
 * Created by tobia on 10-8-2017.
 */

public class MainMenuActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    private FloatingActionButton floatingActionButtonEvent;
    MainMenuViewModel mainMenuViewModel;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = (Toolbar) findViewById(R.id.main_menu_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Join Event");

        PelorusApplication pelorusApplication = (PelorusApplication) getApplication();
        mainMenuViewModel = ViewModelProviders.of(this, new PelorusFactory(pelorusApplication))
                .get(MainMenuViewModel.class);

        mainMenuViewModel.deleteSharedPref();

        floatingActionButtonEvent = (FloatingActionButton) findViewById(R.id.fab_join_event);
        floatingActionButtonEvent.setOnClickListener(v1 -> {
            if (mainMenuViewModel.startCheck(this)) {
                startActivity(new Intent(this, BoatsActivity.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        mainMenuViewModel.getUser().observe(this,user -> {
            if(user!=null){
                Picasso.with(this).load(user.getPhoto()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Drawable drawable = new BitmapDrawable(getResources(),bitmap);
                        toolbar.getMenu().findItem(R.id.item_profile).setIcon(drawable);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        });
        return true;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return registry;
    }
}