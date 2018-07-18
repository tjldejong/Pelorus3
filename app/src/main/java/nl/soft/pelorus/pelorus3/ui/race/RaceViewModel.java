package nl.soft.pelorus.pelorus3.ui.race;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

import java.util.List;

import javax.inject.Inject;

import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.entity.Mark;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.repository.BoatRepository;
import nl.soft.pelorus.pelorus3.repository.LocationRepository;
import nl.soft.pelorus.pelorus3.repository.MarkRepository;
import nl.soft.pelorus.pelorus3.ui.finish.FinishActivity;
import nl.soft.pelorus.pelorus3.ui.mainmenu.MainMenuActivity;

/**
 * Created by tobia on 5-9-2017.
 */

public class RaceViewModel extends ViewModel implements PelorusComponent.Injectable {

    @Inject
    LocationRepository locationRepository;

    @Inject
    MarkRepository markRepository;

    @Inject
    SharedPreferences sharedPreferences;


    LiveData<List<Mark>> marks = new MutableLiveData<>();
    List<Mark> marksList;
    Mark toMark;
    Mark fromMark;

    int goToMark;
    int laps;

    long roundtime;

    double vmg;
    double totalDistance;

    private LocationCallback locationCallback;
    private GoogleApiClient.ConnectionCallbacks connectionCallbacks;
    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener;


    public void inject(PelorusComponent pelorusComponent){
        pelorusComponent.inject(this);
        marks = markRepository.getAllMarksEvent();
        roundtime = System.currentTimeMillis()/1000;
        goToMark = 0;
        laps = 0;
        totalDistance = 0;
    }

    public LiveData<List<Mark>> getMarks() {
        return marks;
    }

    public void setMarks(LiveData<List<Mark>> marks) {
        this.marks = marks;
    }

    public List<Mark> getMarkList() {
        return marksList;
    }

    public void setMarkList(List<Mark> markList) {
        this.marksList = markList;

        Mark startMark = marksList.get(0);
        Mark firstMark = marksList.get(1);
        float x = startMark.getLat()-(firstMark.getLat()-startMark.getLat());
        float y = startMark.getLng()-(firstMark.getLng()-startMark.getLng());
        fromMark = new Mark(0,0,0,x,y);

        toMark = marksList.get(goToMark);
    }

    public void registerCallbacksAndStartLocationUpdates(LifecycleActivity activity) {
        connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Log.i("googleApiClient","connected");
                startLocationUpdates(activity);
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.i("googleApiClient","connection suspended");
            }
        };

        onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.i("googleApiClient","connection failed");
            }
        };

        locationRepository.registerCallbacks(connectionCallbacks,onConnectionFailedListener);
    }

    public void unregisterCallbacks(){
        locationRepository.unregisterCallbacks(connectionCallbacks,onConnectionFailedListener);
    }

    public void connect(){
        locationRepository.connect();
    }

    public void disconnect(){
        locationRepository.disconnect();
    }

    public void startLocationUpdates(Activity activity){
        int boatid = sharedPreferences.getInt("boatId",0);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                Log.i("Location found", location.toString());
                if(marksList!=null){
                    calculateMarkTimeVmgDist(location,activity.getApplicationContext());
                    locationRepository.addLocation(location,goToMark,vmg,roundtime,totalDistance);
                }
            }
        }};
        locationRepository.startLocationUpdates(locationCallback,activity);

    }

    public void stopLocationUpdates(){
        locationRepository.stopLocationUpdates(locationCallback);
        unregisterCallbacks();
    }

    private void calculateMarkTimeVmgDist(Location location,Context context){
        Coordinate c90 = calculate90degreePoint(fromMark.getLat(),fromMark.getLng(),toMark.getLat(),toMark.getLng(),true);
        boolean nextMark = nextMarkCheck(toMark.getLat(),toMark.getLng(),c90.x,c90.y,location.getLatitude(),location.getLongitude(),fromMark.getLat(),fromMark.getLng());

        if(nextMark){
            goToMark++;
            roundtime = (System.currentTimeMillis()/1000);
            if(goToMark<marksList.size()) {
                fromMark = marksList.get(goToMark - 1);
            }
            else{
                laps++;
                goToMark=0;
                fromMark = marksList.get(marksList.size()-1);
            }
            toMark = marksList.get(goToMark);


            if(goToMark==1 && laps ==1){
                Toast.makeText(context, "Finished!", Toast.LENGTH_LONG).show();

//                Intent intent = new Intent(context, FinishActivity.class);
//                context.startActivity(intent);
            }

            Toast.makeText(context, "Mark rounded go to mark " + Integer.toString(goToMark), Toast.LENGTH_SHORT).show();
        }

        setVmg(location);
        setDistance(location);
    }

//    public void goToNextMark(Location location,Context context){
//        toMark = marksList.get(goToMark);
//
//        if(goToMark ==0 && laps==0){
//            Mark startMark = marksList.get(0);
//            Mark firstMark = marksList.get(1);
//            float x = startMark.getLat()-(firstMark.getLat()-startMark.getLat());
//            float y = startMark.getLng()-(firstMark.getLng()-startMark.getLng());
//
//            fromMark = new Mark(0,0,0,x,y);
//        }
//        else{
//            if((goToMark - 1)>=0) {
//                fromMark = marksList.get(goToMark - 1);
//            }
//            else{
//                fromMark = marksList.get(marksList.size()-1);
//            }
//        }
//
//        boolean bb = true;
//        Coordinate c90 = calculate90degreePoint(fromMark.getLat(),fromMark.getLng(),toMark.getLat(),toMark.getLng(),bb);
//        Log.i("c90",c90.toString());
//
//        boolean NextMark = nextMarkCheck(toMark.getLat(),toMark.getLng(),c90.x,c90.y,location.getLatitude(),location.getLongitude(),fromMark.getLat(),fromMark.getLng());
//
//        if(NextMark){
//            roundtime = (System.currentTimeMillis()/1000);
//            goToMark++;
//            Toast.makeText(context, "Mark rounded go to mark "+Integer.toString(goToMark), Toast.LENGTH_SHORT).show();
//
//            toMark = marksList.get(goToMark);
//            fromMark = marksList.get(goToMark - 1);
//
//            if(goToMark==marksList.size()){
//                goToMark=0;
//                fromMark = marksList.get(marksList.size()-1);
//                laps++;
//            }
//
//
//            if(goToMark==1&&laps==1){
//                context.startActivity(new Intent(context, FinishActivity.class));
//            }
//        }
//
//    }


    private Boolean nextMarkCheck(double tx, double ty, double cx, double cy, double lx, double ly, double fx, double fy){
        if ((fy - ty) >= ((fx - tx)*(cy - ty)/(cx - tx))){
            return (ly - ty) <= ((lx - tx)*(cy - ty)/(cx - tx));
        }
        else {
            return (ly - ty) >= ((lx - tx) * (cy - ty) / (cx - tx));
        }
    }

    private Coordinate calculate90degreePoint(double fx, double fy, double tx, double ty, boolean bb){
        double c90x;
        double c90y;
        if(bb){
            c90x = tx + fy - ty;
            c90y = ty + tx - fx;
        }
        else{
            c90x = tx + ty - fy;
            c90y = ty + fx - tx;
        }
        return new Coordinate(c90x, c90y);
    }

    private void setVmg(Location locationBoat){
        if(roundtime == 0){
            this.vmg = 0;
        }
        if(locationBoat.getSpeed()!=0 && locationBoat.getBearing() !=0){
            android.location.Location fromMarkLocation = new android.location.Location("");
            fromMarkLocation.setLatitude(fromMark.getLat()); fromMarkLocation.setLongitude(fromMark.getLng());

            android.location.Location toMarkLocation = new android.location.Location("");
            toMarkLocation.setLatitude(toMark.getLat()); toMarkLocation.setLongitude(toMark.getLng());

            double courseBearing = fromMarkLocation.bearingTo(toMarkLocation);

            this.vmg = Math.cos(Math.toRadians(courseBearing - locationBoat.getBearing()))*locationBoat.getSpeed();
        }
        else{
            LineSegment ls = new LineSegment(fromMark.getLat(), fromMark.getLng(), toMark.getLat(), toMark.getLng());
            Coordinate c = new Coordinate(locationBoat.getLatitude(), locationBoat.getLongitude());
            Coordinate snappedPoint = ls.closestPoint(c);

            android.location.Location locationProjected = new android.location.Location("");
            locationProjected.setLatitude(snappedPoint.x); locationProjected.setLongitude(snappedPoint.y);

            android.location.Location toMarkLocation = new android.location.Location("");
            toMarkLocation.setLatitude(toMark.getLat()); toMarkLocation.setLongitude(toMark.getLng());

            this.vmg = (locationProjected.distanceTo(toMarkLocation)/((System.currentTimeMillis()/1000)-roundtime));
        }
    }

    private void setDistance(Location locationBoat){
        double totalDistance = distanceUntilMark(goToMark-1);

        LineSegment ls = new LineSegment(fromMark.getLat(), fromMark.getLng(), toMark.getLat(), toMark.getLng());
        Coordinate c = new Coordinate(locationBoat.getLatitude(), locationBoat.getLongitude());
        Coordinate snappedPoint = ls.closestPoint(c);

        Log.i("snappedPoint",snappedPoint.toString());

        Location locationProjected = new Location("");
        locationProjected.setLatitude(snappedPoint.x); locationProjected.setLongitude(snappedPoint.y);

        Location fromMarkLocation = new Location("");
        fromMarkLocation.setLatitude(fromMark.getLat()); fromMarkLocation.setLongitude(fromMark.getLng());

        this.totalDistance = totalDistance + fromMarkLocation.distanceTo(locationProjected);
    }

    private double distanceUntilMark(int mark){
        double distance = 0;

        for (int i = 1;i<mark;i++){
            Location location1 = new Location("");
            location1.setLatitude(marksList.get(i-1).getLat()); location1.setLongitude(marksList.get(i-1).getLng());

            Location location2 = new Location("");
            location2.setLatitude(marksList.get(i).getLat()); location2.setLongitude(marksList.get(i).getLng());

            distance = distance + location1.distanceTo(location2);
        }
        return distance;
    }





}
