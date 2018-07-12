package nl.soft.pelorus.pelorus3.ui.boats;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.injection.PelorusFactory;
import nl.soft.pelorus.pelorus3.ui.boats.manage.ManageBoatActivity;
import nl.soft.pelorus.pelorus3.ui.race.RaceActivity;

/**
 * Created by tobia on 18-9-2017.
 */

public class BoatsActivity  extends AppCompatActivity implements LifecycleRegistryOwner {

    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    private FloatingActionButton floatingActionButtonBoat;
    BoatsActivityViewModel boatsActivityViewModel;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boats);

        toolbar = (Toolbar) findViewById(R.id.boats_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Boats");

        PelorusApplication pelorusApplication = (PelorusApplication) getApplication();
        boatsActivityViewModel = ViewModelProviders.of(this, new PelorusFactory(pelorusApplication))
                .get(BoatsActivityViewModel.class);

        floatingActionButtonBoat = (FloatingActionButton) findViewById(R.id.fab_start_race);
        floatingActionButtonBoat.setOnClickListener(v1 -> {
            if (boatsActivityViewModel.startCheck(this)) {
                startActivity(new Intent(this, RaceActivity.class));
            }
        });

        Button addBoatButton = (Button) findViewById(R.id.button_add_boat);
        addBoatButton.setOnClickListener(view -> {
            startActivity(new Intent(this,ManageBoatActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        boatsActivityViewModel.getUser().observe(this,user -> {
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
        });


        return true;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return registry;
    }


}
