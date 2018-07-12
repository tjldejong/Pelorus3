package nl.soft.pelorus.pelorus3.ui.race.leaderboard;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.Nullable;
import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.entity.BoatLocation;
import nl.soft.pelorus.pelorus3.entity.Event;
import nl.soft.pelorus.pelorus3.entity.Location;
import nl.soft.pelorus.pelorus3.entity.Mark;
import nl.soft.pelorus.pelorus3.injection.PelorusFactory;

/**
 * Created by tobia on 1-9-2017.
 */

public class LeaderboardFragment extends LifecycleFragment {

    LeaderboardViewModel leaderboardViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_leaderboard, container, false);

        PelorusApplication pelorusApplication = (PelorusApplication) getActivity().getApplication();
        leaderboardViewModel = ViewModelProviders.of(this, new PelorusFactory(pelorusApplication))
                .get(LeaderboardViewModel.class);

        ListView listView = (ListView) view.findViewById(R.id.list_view_leaderboard);

        LeaderboardAdapter leaderboardAdapter = new LeaderboardAdapter(new ArrayList<>());
        listView.setAdapter(leaderboardAdapter);

        leaderboardViewModel.getEvent().observe(this,event -> {
            if(event!=null)
                {leaderboardViewModel.setStarttime(event.getStarttime());
            }
        });


        leaderboardViewModel.getBoatLoactions().observe(this,boatLocations -> {
            if(boatLocations.size()>=2) {
                BoatLocation leadBoat = Collections.max(boatLocations, new Comparator<BoatLocation>() {
                    @Override
                    public int compare(BoatLocation boat1, BoatLocation boat2) {
                        Double distanceBoat1 = boat1.getTotalDistance();
                        Double distanceBoat2 = boat2.getTotalDistance();
                        return  distanceBoat1.compareTo(distanceBoat2);
                    }
                });


                for (BoatLocation boat : boatLocations) {
                    Log.i("StartCalc for boat",boat.name);
                    double correctedTime = leaderboardViewModel.getEstimatedTimeAtLead(leadBoat,boat);
                    Log.i("Corrected time",Double.toString(correctedTime));
                    boat.setCorrectedTime((long)correctedTime);
                }

                double leadTime = Collections.min(boatLocations, new Comparator<BoatLocation>() {
                    @Override
                    public int compare(BoatLocation boat1, BoatLocation boat2) {
                        return boat1.getCorrectedTime().compareTo(boat2.getCorrectedTime());
                    }
                }).getCorrectedTime();

                for (BoatLocation boat : boatLocations) {
                    Log.i("LeadTime",Double.toString(leadTime));
                    double correctedTimeRelative = boat.getCorrectedTime()-leadTime;
                    Log.i("correctedTimeRelative",Double.toString(correctedTimeRelative));
                    boat.setCorrectedTime((long)correctedTimeRelative);
                }

            }

            Collections.sort(boatLocations, new Comparator<BoatLocation>() {
                @Override
                public int compare(BoatLocation boat1, BoatLocation boat2) {
                    if (boat1.getCorrectedTime() == null) {
                        return (boat2.getCorrectedTime() == null) ? 0 : 1;
                    }
                    if (boat2.getCorrectedTime() == null) {
                        return -1;
                    }
                    return boat1.getCorrectedTime().compareTo(boat2.getCorrectedTime());
                }
            });

            leaderboardAdapter.setItems(boatLocations);
        });



        return view;
    }
}
