package nl.soft.pelorus.pelorus3.ui.race.leaderboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;
import android.util.Log;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import java.util.List;

import javax.inject.Inject;

import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.entity.BoatLocation;
import nl.soft.pelorus.pelorus3.entity.Event;
import nl.soft.pelorus.pelorus3.entity.Mark;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.repository.BoatRepository;
import nl.soft.pelorus.pelorus3.repository.EventRepository;
import nl.soft.pelorus.pelorus3.repository.LocationRepository;
import nl.soft.pelorus.pelorus3.repository.MarkRepository;

/**
 * Created by tobia on 12-9-2017.
 */

public class LeaderboardViewModel extends ViewModel implements PelorusComponent.Injectable {
    @Inject
    LocationRepository locationRepository;

    @Inject
    MarkRepository markRepository;

    @Inject
    EventRepository eventRepository;

    LiveData<Event> event = new MutableLiveData<>();

    LiveData<List<BoatLocation>> boatLoactions = new MutableLiveData<>();

    long starttime;

    @Override
    public void inject(PelorusComponent pelorusComponent) {
        pelorusComponent.inject(this);
        boatLoactions = locationRepository.getLastLocationsWithNames();
        event = eventRepository.getEvent();
    }

    public LiveData<List<BoatLocation>> getBoatLoactions() {
        return boatLoactions;
    }

    public LiveData<Event> getEvent() {
        return event;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public double getEstimatedTimeAtLead(BoatLocation leadBoatLocation, BoatLocation boatLocation){
        double timeInRace = (double)((System.currentTimeMillis()/1000) - starttime);
        Log.i("time",Double.toString(timeInRace));
        Log.i("leadBoat totalDistance" ,Double.toString(leadBoatLocation.totalDistance));
        Log.i("boat totalDistance" ,Double.toString( boatLocation.totalDistance));
        double distanceToLead = leadBoatLocation.totalDistance - boatLocation.totalDistance;
        Log.i("distanceToLead",Double.toString(distanceToLead));
        Log.i("vmg",Double.toString(boatLocation.vmg));
        Log.i("sw",Double.toString(boatLocation.sw));
        if(boatLocation.vmg!=0) {
            return ((timeInRace + (distanceToLead / boatLocation.vmg)) * (100 / boatLocation.sw));
        }
        else{
            return ((timeInRace) * (100 / boatLocation.sw));
        }
    }

//    public double getCorrectedTime(long starttime,BoatLocation boat, Mark fromMark, Mark toMark,long roundtime, double sw){
//        android.location.Location boatLocation = new android.location.Location("");
//        boatLocation.setLatitude(boat.lat);        boatLocation.setLongitude(boat.lng);
//
//        double time = (double)((System.currentTimeMillis()/1000) - starttime);
//        double distanceToMark = getDistanceToMark(boatLocation,fromMark,toMark);
//        Log.i("distanceToMark",Double.toString(distanceToMark));
//        double averageSpeedLeg = getSpeedToMark(boatLocation,fromMark,toMark,roundtime);
//        Log.i("averageSpeedLeg",Double.toString(averageSpeedLeg));
//
//        return (time+(distanceToMark/averageSpeedLeg))*(100/sw);
//    }
//
//    private double getDistanceToMark(BoatLocation locationBoat, Mark fromMark, Mark toMark)
//    {
//        LineSegment ls = new LineSegment(fromMark.getLat(), fromMark.getLng(), toMark.getLat(), toMark.getLng());
//        Coordinate c = new Coordinate(locationBoat.lat, locationBoat.lng);
//        Coordinate snappedPoint = ls.closestPoint(c);
//
//        Log.i("snappedPoint",snappedPoint.toString());
//
//        Location locationProjected = new Location("");
//        locationProjected.setLatitude(snappedPoint.x); locationProjected.setLongitude(snappedPoint.y);
//
//        Location toMarkLocation = new Location("");
//        toMarkLocation.setLatitude(toMark.getLat()); toMarkLocation.setLongitude(toMark.getLng());
//
//        return locationProjected.distanceTo(toMarkLocation);
//    }
//
//    private double getSpeedToMark(Location locationBoat, Mark fromMark, Mark toMark,long roundtime){
//        LineSegment ls = new LineSegment(fromMark.getLat(), fromMark.getLng(), toMark.getLat(), toMark.getLng());
//        Coordinate c = new Coordinate(locationBoat.getLatitude(), locationBoat.getLongitude());
//        Coordinate snappedPoint = ls.closestPoint(c);
//
//        Location locationProjected = new Location("");
//        locationProjected.setLatitude(snappedPoint.x); locationProjected.setLongitude(snappedPoint.y);
//
//        Location fromMarkLocation = new Location("");
//        fromMarkLocation.setLatitude(fromMark.getLat()); fromMarkLocation.setLongitude(fromMark.getLng());
//
//        return locationProjected.distanceTo(fromMarkLocation)/((System.currentTimeMillis()/1000)-roundtime);
//    }

}
