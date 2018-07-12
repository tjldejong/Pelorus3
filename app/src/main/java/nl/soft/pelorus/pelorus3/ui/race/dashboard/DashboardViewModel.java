package nl.soft.pelorus.pelorus3.ui.race.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.util.AffineTransformation;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import nl.soft.pelorus.pelorus3.BR;
import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.entity.Event;
import nl.soft.pelorus.pelorus3.entity.Location;
import nl.soft.pelorus.pelorus3.entity.Mark;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.repository.BoatRepository;
import nl.soft.pelorus.pelorus3.repository.EventRepository;
import nl.soft.pelorus.pelorus3.repository.LocationRepository;
import nl.soft.pelorus.pelorus3.repository.MarkRepository;

/**
 * Created by tobia on 1-9-2017.
 */

public class DashboardViewModel extends ViewModel implements PelorusComponent.Injectable,Observable {
    @Inject
    BoatRepository boatRepository;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    EventRepository eventRepository;

    @Inject
    MarkRepository markRepository;

    @Inject
    LocationRepository locationRepository;

    private LiveData<Location> myLocation = new MutableLiveData<>();
    private LiveData<Event> event = new MutableLiveData<>();
    private LiveData<List<Mark>> marks = new MutableLiveData<>();

    private PropertyChangeRegistry registry =
            new PropertyChangeRegistry();

    public String lat;
    public String lng;
    private String time;
    private String speed;
    private String dtw;
    private String bearing;
    private String vmg;

    private List<Mark> marksList;

    private float mToNm = 0.000539956803f;
    private double msToKnots = 0.5144;

    private int goToMark;

    @Override
    public void inject(PelorusComponent pelorusComponent){
        pelorusComponent.inject(this);
        myLocation = locationRepository.getMyLocation();
        event = eventRepository.getEvent();
        marks = markRepository.getAllMarksEvent();
    }

    public LiveData<Event> getEvent(){return event;}

    public LiveData<List<Mark>> getMark(){return marks;}

    public LiveData<Location> getMyLocation(){return myLocation;}

    public List<Mark> getMarks() {
        return marksList;
    }

    public void setMarks(List<Mark> marks) {
        this.marksList = marks;
    }

    public int getGoToMark() {
        return goToMark;
    }

    public void setGoToMark(int goToMark) {
        this.goToMark = goToMark;
    }

    @Bindable
    public String getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = Double.toString(lat);
        registry.notifyChange(this, BR.lat);
    }

    @Bindable
    public String getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = Double.toString(lng);
        registry.notifyChange(this, BR.lng);
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(Long time) {
        long millis = System.currentTimeMillis()-(time*1000);

        this.time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        registry.notifyChange(this, BR.time);
    }

    @Bindable
    public String getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = String.format("%.2f",speed*msToKnots);
        registry.notifyChange(this, BR.speed);
    }

    @Bindable
    public String getDtw() {
        return dtw;
    }

    public void setDtw(Location boatLocation) {
        android.location.Location locationBoat = new android.location.Location("");
        locationBoat.setLatitude(boatLocation.getLat());
        locationBoat.setLongitude(boatLocation.getLng());

        android.location.Location locationMark = new android.location.Location("");
        locationMark.setLatitude(marksList.get(goToMark).getLat());
        locationMark.setLongitude(marksList.get(goToMark).getLng());

        this.dtw = String.format("%.2f",locationBoat.distanceTo(locationMark)*mToNm);
        registry.notifyChange(this, BR.dtw);
    }

    @Bindable
    public String getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = Integer.toString((int) bearing);
        registry.notifyChange(this, BR.bearing);
    }

    @Bindable
    public String getVmg() {
        return vmg;
    }

    public void setVmg(double vmg){
        this.vmg = String.format("%.2f",vmg*msToKnots);
        registry.notifyChange(this,BR.vmg);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        registry.add(onPropertyChangedCallback);

    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        registry.add(onPropertyChangedCallback);
    }
}
