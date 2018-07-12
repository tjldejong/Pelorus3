package nl.soft.pelorus.pelorus3.ui.race.map;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.security.acl.Owner;
import java.util.List;

import javax.inject.Inject;

import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.entity.BoatLocation;
import nl.soft.pelorus.pelorus3.entity.Event;
import nl.soft.pelorus.pelorus3.entity.Location;
import nl.soft.pelorus.pelorus3.entity.Mark;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.repository.BoatRepository;
import nl.soft.pelorus.pelorus3.repository.EventRepository;
import nl.soft.pelorus.pelorus3.repository.LocationRepository;
import nl.soft.pelorus.pelorus3.repository.MarkRepository;

/**
 * Created by tobia on 4-9-2017.
 */

public class MapViewModel extends ViewModel implements PelorusComponent.Injectable {
    @Inject
    LocationRepository locationRepository;

    @Inject
    EventRepository eventRepository;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    MarkRepository markRepository;

    private LiveData<Event> event = new MutableLiveData<>();

    private LiveData<List<Mark>> marks = new MutableLiveData<>();

    private LiveData<List<BoatLocation>> boatLocation = new MutableLiveData<>();

    private LiveData<Location> myLocation = new MutableLiveData<>();

    @Override
    public void inject(PelorusComponent pelorusComponent){
        pelorusComponent.inject(this);
        boatLocation = locationRepository.getLastLocationsWithNames();
        myLocation = locationRepository.getMyLocation();

        event = eventRepository.getEvent();
        marks = markRepository.getAllMarksEvent();

    }

    public LiveData<Event> getEvent(){
         return event;
    }

    public LiveData<List<Mark>> getMarks(){return marks;}

    public LiveData<List<BoatLocation>> getLocations() {
        return boatLocation;
    }

    public LiveData<Location> getMyLocation(){return myLocation;}

}
