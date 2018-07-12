package nl.soft.pelorus.pelorus3.repository;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.location.Location;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;

import java.util.List;

import nl.soft.pelorus.pelorus3.entity.BoatLocation;

/**
 * Created by tobia on 4-9-2017.
 */

public interface LocationRepository {
    void connect();

    void disconnect();

    void startLocationUpdates(LocationCallback locationCallback, Activity activity);

    void stopLocationUpdates(LocationCallback locationCallback);

    void registerCallbacks(GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener);

    void unregisterCallbacks(GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener);

    LiveData<nl.soft.pelorus.pelorus3.entity.Location> getMyLocation();

    void addLocation(Location location,int goToMark, double vmg, long roundtime, double totaldistance);

    Location getLastKnowLocation(FragmentActivity fragmentActivity);

    LiveData<List<nl.soft.pelorus.pelorus3.entity.Location>> getLastLocationAllBoats();

    LiveData<List<BoatLocation>> getLastLocationsWithNames();
}
