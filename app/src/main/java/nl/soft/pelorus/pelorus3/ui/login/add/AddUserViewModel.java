package nl.soft.pelorus.pelorus3.ui.login.add;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nl.soft.pelorus.pelorus3.entity.User;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.repository.UserRepository;

/**
 * Created by tobia on 3-8-2017.
 */

public class AddUserViewModel extends ViewModel implements PelorusComponent.Injectable {

    @Inject
    UserRepository userRepository;

    private String userName = "";

    @Override
    public void inject(PelorusComponent pelorusComponent) {
        pelorusComponent.inject(this);
    }

    public void connect(){
        userRepository.connect();
    }

    public void disconnect(){
        userRepository.disconnect();
    }

    public Intent getSignInIntent(){
        return  userRepository.getGoogleSignInIntent();
    }

    public boolean setAccount(int requestCode, int resultCode, Intent data,int RC_SIGN_IN){
        Boolean unauthenticated = false;
        if (requestCode == RC_SIGN_IN) {  // TODO: of dit if statement iets toevoegd
            userName = userRepository.getGoogleSignInIdToken(data);
            if (userName == null){
                unauthenticated = true;
            }
        } else {
            // TODO: Signed out, show unauthenticated UI.
        }

        return unauthenticated;
    }







}
