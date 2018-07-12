package nl.soft.pelorus.pelorus3.repository;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nl.soft.pelorus.pelorus3.api.Google.GoogleApi;
import nl.soft.pelorus.pelorus3.api.MySQLDatabase.MySQLDatabaseClient;
import nl.soft.pelorus.pelorus3.db.UserDatabase;
import nl.soft.pelorus.pelorus3.entity.BoatLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tobia on 4-9-2017.
 */

public class LocationRepositoryImpl implements LocationRepository {
    @Inject
    GoogleApi googleApi;

    @Inject
    UserDatabase userDatabase;

    @Inject
    MySQLDatabaseClient mySQLDatabaseClient;

    @Inject
    SharedPreferences sharedPreferences;

    private LocationRequest locationRequest;

    public LocationRepositoryImpl(GoogleApi googleApi,UserDatabase userDatabase, SharedPreferences sharedPreferences,MySQLDatabaseClient mySQLDatabaseClient) {
        this.googleApi = googleApi;
        this.userDatabase = userDatabase;
        this.sharedPreferences = sharedPreferences;
        this.mySQLDatabaseClient = mySQLDatabaseClient;

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
    public void startLocationUpdates(LocationCallback locationCallback,Activity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            return;
        }
        if((PermissionChecker.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApi.getGoogleApiClient(),locationRequest,locationCallback,null);
        }
    }

    @Override
    public void stopLocationUpdates(LocationCallback locationCallback){
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApi.getGoogleApiClient(),locationCallback);
    }

    @Override
    public void addLocation(Location location,int goToMark, double vmg, long roundtime,double totaldistance) {
        int boatid = sharedPreferences.getInt("boatId",0);
        int eventid = sharedPreferences.getInt("eventId",0);
        Long time = location.getTime();
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        float bearing = location.getBearing();
        float speed = location.getSpeed();

        nl.soft.pelorus.pelorus3.entity.Location location1 = new nl.soft.pelorus.pelorus3.entity.Location(0,boatid,eventid,time,lat,lng,bearing,speed,goToMark,vmg,roundtime,totaldistance);

        Completable.fromAction(()-> mySQLDatabaseClient.createLocation(location1).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                getAllLastLocationsToUserDB();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        })).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.i("Location","uploaded");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("Error",e.toString());

            }
        });
    }

    @Override
    public LiveData<nl.soft.pelorus.pelorus3.entity.Location> getMyLocation() {
        return userDatabase.locationDao().getLocation(sharedPreferences.getInt("boatId",0));
    }

    public LiveData<List<nl.soft.pelorus.pelorus3.entity.Location>> getLastLocationAllBoats(){
        return userDatabase.locationDao().getLastLocations(sharedPreferences.getInt("eventId",0));
    }

    @Override
    public LiveData<List<BoatLocation>> getLastLocationsWithNames(){
        return userDatabase.locationDao().getLastLocationsWithNames(sharedPreferences.getInt("eventId",0));
    }



    @Override
    public Location getLastKnowLocation(FragmentActivity fragmentActivity) {
        if (ContextCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(fragmentActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
        Log.i("Location","permission granted");
        Log.i("Google connected",Boolean.toString(googleApi.getGoogleApiClient().isConnected()));
        return LocationServices.FusedLocationApi.getLastLocation(googleApi.getGoogleApiClient());
    }

    public void registerCallbacks(GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener){
        if (googleApi.getGoogleApiClient() != null) {
            googleApi.getGoogleApiClient().registerConnectionCallbacks(connectionCallbacks);
            googleApi.getGoogleApiClient().registerConnectionFailedListener(onConnectionFailedListener);
        }
    }

    public void unregisterCallbacks(GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener){
        if (googleApi.getGoogleApiClient() != null) {
            googleApi.getGoogleApiClient().unregisterConnectionCallbacks(connectionCallbacks);
            googleApi.getGoogleApiClient().unregisterConnectionFailedListener(onConnectionFailedListener);
        }
    }

    private void getAllLastLocationsToUserDB(){
        Map<String,String> map = new HashMap<>();
        map.put("time","max");
        map.put("eventid",Integer.toString(sharedPreferences.getInt("eventId",0)));
        Completable.fromAction(()-> mySQLDatabaseClient.getLastLocations(map).enqueue(new Callback<List<nl.soft.pelorus.pelorus3.entity.Location>>() {
            @Override
            public void onResponse(Call<List<nl.soft.pelorus.pelorus3.entity.Location>> call, Response<List<nl.soft.pelorus.pelorus3.entity.Location>> response) {
                for (nl.soft.pelorus.pelorus3.entity.Location location: response.body())
                    Completable.fromAction(()->userDatabase.locationDao().addLocation(location))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onComplete() {
                                    Log.i("Location insert local","completed");
                                }

                                @Override
                                public void onError(Throwable e) {
                                }
                            });
            }

            @Override
            public void onFailure(Call<List<nl.soft.pelorus.pelorus3.entity.Location>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        })).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.i("Locations","downloaded");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("Error",e.toString());

            }
        });
    }

    public void deleteAllLocations(){

        Completable.fromAction(()->userDatabase.locationDao().nukeTable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.i("Location insert local","completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }



}
