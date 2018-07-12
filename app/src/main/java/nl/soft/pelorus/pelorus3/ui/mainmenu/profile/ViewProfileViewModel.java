package nl.soft.pelorus.pelorus3.ui.mainmenu.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import nl.soft.pelorus.pelorus3.entity.User;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.repository.UserRepository;

/**
 * Created by tobia on 10-8-2017.
 */

public class ViewProfileViewModel extends ViewModel implements PelorusComponent.Injectable {

    @Inject
    UserRepository userRepository;

    LiveData<User> user = new MutableLiveData<>();

    @Override
    public void inject(PelorusComponent pelorusComponent) {
        pelorusComponent.inject(this);
        user = userRepository.getCurrentUser();
    }

    public LiveData<User> getCurrentUser() {
        return user;
    }

}
