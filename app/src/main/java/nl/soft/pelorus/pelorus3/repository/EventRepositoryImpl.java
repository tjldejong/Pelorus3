package nl.soft.pelorus.pelorus3.repository;

import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;
import javax.security.auth.callback.Callback;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nl.soft.pelorus.pelorus3.api.MySQLDatabase.MySQLDatabaseClient;
import nl.soft.pelorus.pelorus3.db.UserDatabase;
import nl.soft.pelorus.pelorus3.entity.Event;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by tobia on 25-8-2017.
 */

public class EventRepositoryImpl implements EventRepository {
    @Inject
    UserDatabase userDatabase;

    @Inject
    MySQLDatabaseClient mySQLDatabaseClient;

    @Inject
    SharedPreferences sharedPreferences;


    public EventRepositoryImpl(UserDatabase userDatabase, MySQLDatabaseClient mySQLDatabaseClient, SharedPreferences sharedPreferences){
        this.userDatabase = userDatabase;
        this.mySQLDatabaseClient = mySQLDatabaseClient;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public LiveData<Event> getEvent() {
        return userDatabase.eventDao().getEvent(sharedPreferences.getInt("eventId",0));
    }

    @Override
    public LiveData<List<Event>> getAllEvents() {
        mySQLDatabaseClient.getAllEvents().enqueue(new retrofit2.Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

                Completable.fromAction(()-> userDatabase.eventDao().nukeTable()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.i("Events","Deleted");
                        for (Event event :response.body()) {
                            Completable.fromAction(()->userDatabase.eventDao().addEvent(event)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onComplete() {
                                    Log.i("Event insert local",event.getName());
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.i("Error",e.toString());
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Error",e.toString());
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
        return  userDatabase.eventDao().getAllEvents();
    }

    @Override
    public void setActiveEvent(int id){
        sharedPreferences.edit().putInt("eventId",id).apply();
    }

}
