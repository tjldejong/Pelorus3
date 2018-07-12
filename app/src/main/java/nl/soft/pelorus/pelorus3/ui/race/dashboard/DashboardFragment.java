package nl.soft.pelorus.pelorus3.ui.race.dashboard;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.annotations.Nullable;
import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.databinding.FragmentDashboardBinding;
import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.injection.PelorusFactory;

/**
 * Created by tobia on 1-9-2017.
 */

public class DashboardFragment extends LifecycleFragment {

    DashboardViewModel dashboardViewModel;

    Timer timer = new Timer(true);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDashboardBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard,container,false);
        View view = binding.getRoot();

        PelorusApplication pelorusApplication = (PelorusApplication) getActivity().getApplication();
        dashboardViewModel = ViewModelProviders.of(this, new PelorusFactory(pelorusApplication))
                .get(DashboardViewModel.class);

        binding.setViewModelDashbord(dashboardViewModel);

        dashboardViewModel.getMark().observe(this,marks->{
            if(marks!=null) {
                dashboardViewModel.setMarks(marks);
                loadEventTime();
            }
        });



        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        timer.cancel();
    }

    private void loadEventTime(){
        dashboardViewModel.getEvent().observe(this,event -> {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    dashboardViewModel.setTime(event.getStarttime());
                }
            },0,100);
            loadMeters();
        });
    }

    private void loadMeters(){
        dashboardViewModel.getMyLocation().observe(this,location -> {
            if(location!=null) {
                dashboardViewModel.setGoToMark(location.getNextMark());

                dashboardViewModel.setLat(location.getLat());
                dashboardViewModel.setLng(location.getLng());

                dashboardViewModel.setSpeed(location.getSpeed());
                dashboardViewModel.setBearing(location.getBearing());

                dashboardViewModel.setVmg(location.getVmg());
                dashboardViewModel.setDtw(location);
            }
        });
    }

}
