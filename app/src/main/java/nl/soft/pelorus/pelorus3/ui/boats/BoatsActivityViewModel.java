package nl.soft.pelorus.pelorus3.ui.boats;


import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import javax.inject.Inject;

import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.entity.User;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.repository.UserRepository;

/**
 * Created by tobia on 18-9-2017.
 */

public class BoatsActivityViewModel extends ViewModel implements PelorusComponent.Injectable{
    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    UserRepository userRepository;

    @Override
    public void inject(PelorusComponent pelorusComponent) {
        pelorusComponent.inject(this);
    }

    public boolean startCheck(Context context){
        if(!sharedPreferences.contains("boatId")){
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
            builder.setMessage("Please select a boat")
                    .setTitle("No boat selected")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return (sharedPreferences.contains("boatId"));
    }

    public LiveData<User> getUser(){
        return userRepository.getCurrentUser();
    }



}
