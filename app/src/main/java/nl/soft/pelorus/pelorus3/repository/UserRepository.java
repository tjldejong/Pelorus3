package nl.soft.pelorus.pelorus3.repository;

import android.arch.lifecycle.LiveData;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import nl.soft.pelorus.pelorus3.entity.User;

/**
 * Created by tobia on 3-8-2017.
 */

public interface UserRepository {
    void connect();

    void disconnect();

    boolean addUser(Intent data);

    LiveData<User> getUser(String id);

    Intent getGoogleSignInIntent();

    String getGoogleSignInDisplayName(Intent data);

//    String getGoogleSignInIdToken(Intent data);

    LiveData<User> getCurrentUser();

}
