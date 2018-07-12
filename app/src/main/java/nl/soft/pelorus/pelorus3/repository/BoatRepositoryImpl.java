package nl.soft.pelorus.pelorus3.repository;

import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nl.soft.pelorus.pelorus3.api.MySQLDatabase.MySQLDatabaseClient;
import nl.soft.pelorus.pelorus3.db.UserDatabase;
import nl.soft.pelorus.pelorus3.entity.Boat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tobia on 25-8-2017.
 */

public class BoatRepositoryImpl implements BoatRepository {

    @Inject
    UserDatabase userDatabase;

    @Inject
    MySQLDatabaseClient mySQLDatabaseClient;

    @Inject
    SharedPreferences sharedPreferences;

    public BoatRepositoryImpl(UserDatabase userDatabase, MySQLDatabaseClient mySQLDatabaseClient, SharedPreferences sharedPreferences){
        this.userDatabase = userDatabase;
        this.mySQLDatabaseClient = mySQLDatabaseClient;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void getAllBoatsFromServer() {
        mySQLDatabaseClient.getAllBoats().enqueue(new retrofit2.Callback<List<Boat>>() {
            @Override
            public void onResponse(Call<List<Boat>> call, Response<List<Boat>> response) {

                Completable.fromAction(()->userDatabase.boatDao().nukeTable())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onComplete() {
                                Log.i("Boats","Deleted");
                                for (Boat boat :response.body()) {
                                    Completable.fromAction(()->userDatabase.boatDao().addBoat(boat))
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new CompletableObserver() {
                                                @Override
                                                public void onSubscribe(Disposable d) {
                                                }

                                                @Override
                                                public void onComplete() {
                                                    Log.i("Boatname insert local",boat.getName());
                                                    Log.i("Id insert local",Integer.toString(boat.getId()));
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
            public void onFailure(Call<List<Boat>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    @Override
    public LiveData<Boat> getBoat(int id) {
        return userDatabase.boatDao().getBoat(id);
    }

    @Override
    public LiveData<Boat> getMyBoat(){
        return userDatabase.boatDao().getBoat(sharedPreferences.getInt("boatId",0));
    }

    @Override
    public LiveData<List<Boat>> getMyBoats(){
        return userDatabase.boatDao().getBoatsFrom(sharedPreferences.getString("userId",""));
    }

    @Override
    public void addBoat(String boatname, double sw) {
        Boat boat = new Boat(0,boatname,sharedPreferences.getString("userId",""),sw,sharedPreferences.getInt("eventId",0));
        mySQLDatabaseClient.createBoat(boat).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                boat.setId(response.body());
                Completable.fromAction(()-> userDatabase.boatDao().addBoat(boat)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.i("Boatname added local",boat.getName());
                        Log.i("Id added local",Integer.toString(boat.getId()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Error",e.toString());
                    }
                });
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    @Override
    public void setActiveBoat(int id){
        sharedPreferences.edit().putInt("boatId",id).apply();
    }

    @Override
    public void updateBoat(Boat boat){
        Completable.fromAction(()->userDatabase.boatDao().updateBoat(boat)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Log.i("Boatname update",boat.getName());
                Log.i("Id update",Integer.toString(boat.getId()));
            }

            @Override
            public void onError(Throwable e) {
                Log.i("Error",e.toString());
            }
        });
    }

}
