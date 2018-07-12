package nl.soft.pelorus.pelorus3.ui.race.map;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.function.UnaryOperator;

import io.reactivex.annotations.Nullable;
import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.entity.Location;
import nl.soft.pelorus.pelorus3.entity.Mark;
import nl.soft.pelorus.pelorus3.injection.PelorusFactory;

/**
 * Created by tobia on 1-9-2017.
 */

public class MapFragment extends LifecycleFragment {

    MapView mapView;
    MapViewModel mapViewModel;
    GoogleMap googleMap;

    List<Marker> boatMarkers = new ArrayList<Marker>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);

        PelorusApplication pelorusApplication = (PelorusApplication) getActivity().getApplication();
        mapViewModel = ViewModelProviders.of(this, new PelorusFactory(pelorusApplication))
                .get(MapViewModel.class);

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
//                LatLng pampus = new LatLng(52.364814, 5.070587);
                loadMyLoaction();
                loadMarks();
                loadLocations();
            }
        });

        return view;
    }

    private void loadMarks(){
        mapViewModel.getMarks().observe(this,marks -> {
            for(Mark mark: marks){
                LatLng posMark = new LatLng(mark.getLat(),mark.getLng());
                googleMap.addMarker(new MarkerOptions().position(posMark).flat(true));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posMark,15));
            }
        });
    }

    private void loadMyLoaction(){
        mapViewModel.getMyLocation().observe(this,location -> {
            LatLng myPos = new LatLng(location.getLat(),location.getLng());
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(myPos));
        });
    }

    private void loadLocations(){
        mapViewModel.getLocations().observe(this,locations -> {
            for(int i = 0; i < locations.size(); i++) {
                LatLng posBoat = new LatLng(locations.get(i).lat, locations.get(i).lng);
                if(boatMarkers.size()!=locations.size()){
                    boatMarkers.add(i,googleMap.addMarker(new MarkerOptions()
                            .position(posBoat)
                            .title(locations.get(i).name)
                            .flat(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.boat)))
                    );
                }
                else {
                    boatMarkers.get(i).setPosition(posBoat);
                    boatMarkers.get(i).setRotation(locations.get(i).bearing);
                }
            }
        });

    }


}
