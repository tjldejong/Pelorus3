package nl.soft.pelorus.pelorus3.ui.mainmenu;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;

import javax.inject.Inject;

import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.entity.User;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.repository.UserRepository;

/**
 * Created by tobia on 7-9-2017.
 */

public class MainMenuViewModel extends ViewModel implements PelorusComponent.Injectable {
    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    UserRepository userRepository;

    @Override
    public void inject(PelorusComponent pelorusComponent) {
        pelorusComponent.inject(this);
    }

    public boolean startCheck(Context context){
        if(!sharedPreferences.contains("eventId")){
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
            builder.setMessage("Please select an event")
                    .setTitle("No event selected")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return (sharedPreferences.contains("eventId"));
    }

    public void deleteSharedPref(){
        sharedPreferences.edit().remove("boatId").apply();
        sharedPreferences.edit().remove("eventId").apply();
    }

    public LiveData<User> getUser(){
        return userRepository.getCurrentUser();
    }
}
