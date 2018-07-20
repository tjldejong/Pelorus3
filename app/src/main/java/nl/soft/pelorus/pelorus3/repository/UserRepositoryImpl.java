package nl.soft.pelorus.pelorus3.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nl.soft.pelorus.pelorus3.api.Google.GoogleApi;
import nl.soft.pelorus.pelorus3.api.MySQLDatabase.MySQLDatabaseClient;
import nl.soft.pelorus.pelorus3.db.UserDatabase;
import nl.soft.pelorus.pelorus3.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tobia on 3-8-2017.
 */

public class UserRepositoryImpl implements UserRepository {

    @Inject
    UserDatabase userDatabase;

    @Inject
    GoogleApi googleApi;

    @Inject
    MySQLDatabaseClient mySQLDatabaseClient;

    @Inject
    SharedPreferences sharedPreferences;

    public UserRepositoryImpl(UserDatabase userDatabase, GoogleApi googleApi, MySQLDatabaseClient mySQLDatabaseClient,SharedPreferences sharedPreferences) {
        this.userDatabase = userDatabase;
        this.googleApi = googleApi;
        this.mySQLDatabaseClient = mySQLDatabaseClient;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void connect() {
        googleApi.getGoogleApiClient().connect();
    }

    @Override
    public void disconnect() {
        googleApi.getGoogleApiClient().disconnect();
    }


    @Override
    public boolean addUser(Intent data)  {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            sharedPreferences.edit().putString("userId",acct.getId()).apply();
            User user = new User(acct.getId(),acct.getDisplayName(),acct.getPhotoUrl().toString());
            mySQLDatabaseClient.createUser(user)
                    .enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            addUserLocal(user);
                        }
                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            //TODO: no connection to database
                        }
                    });
            return true;
        }
        else {
            return false;
        }
    }

    private void addUserLocal(User user){
        Completable.fromAction(()->userDatabase.userDao().addUser(user))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.i("Username insert local", user.getName());
                        Log.i("Id insert local", user.getId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Error", e.toString());
                    }
                });
    }

    @Override
    public LiveData<User> getUser(String id) {
        //Here is where we would do more complex logic, like getting events from a cache
        //then inserting into the database etc. In this example we just go straight to the dao.
        return userDatabase.userDao().getUser(id);
    }

    @Override
    public LiveData<User> getCurrentUser() {
        String id = sharedPreferences.getString("userId","");
        return userDatabase.userDao().getUser(id);
    }

    @Override
    public Intent getGoogleSignInIntent() {
        return Auth.GoogleSignInApi.getSignInIntent(googleApi.getGoogleApiClient());
    }

//    @Override
//    public String getGoogleSignInIdToken(Intent data) {
//        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//
//        Log.i("GoogleSignInResult",Boolean.toString(result.isSuccess()));
//        if (result.isSuccess()) {
//            GoogleSignInAccount acct = result.getSignInAccount();
//
//            Completable.fromAction(()-> mySQLDatabaseClient.sendToken(acct.getIdToken()).enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//                    Log.i("uri",acct.getPhotoUrl().toString());
//                    sharedPreferences.edit().putString("userId",response.body()).apply();
//                    User user = new User(response.body(),acct.getDisplayName(),acct.getPhotoUrl().toString());
//                    addUser(user);
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//                    Log.d("Error", t.getMessage());
//
//                }
//            })).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
//                @Override
//                public void onSubscribe(Disposable d) {
//
//                }
//
//                @Override
//                public void onComplete() {
//                    Log.i("sendToken","Completed");
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.i("Error",e.toString());
//
//                }
//            });
//
//            return acct.getDisplayName();
//        } else {
//            // TODO: Signed out, show unauthenticated UI.
//            Auth.GoogleSignInApi.signOut(googleApi.getGoogleApiClient()).setResultCallback(
//                    new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(Status status) {
//                            Log.i("log out",status.toString());
//                        }
//                    });
//
//
//            return null;
//        }
//    }



    @Override
    public String getGoogleSignInDisplayName(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            return acct.getDisplayName();
        } else {
            // TODO: Signed out, show unauthenticated UI.
            return "Signed out, show unauthenticated UI";
        }
    }

}
