package nl.soft.pelorus.pelorus3.ui.boats;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.view.View;

import java.util.List;


import javax.inject.Inject;

import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.repository.BoatRepository;

/**
 * Created by tobia on 25-8-2017.
 */

public class BoatsFragmentViewModel  extends ViewModel implements PelorusComponent.Injectable {
    @Inject
    BoatRepository boatRepository;

    private LiveData<List<Boat>> boats = new MutableLiveData<>();

    private int selectedBoat;
    private View oldView;

    @Override
    public void inject(PelorusComponent pelorusComponent) {
        pelorusComponent.inject(this);
        boats = boatRepository.getMyBoats();
    }

    public LiveData<List<Boat>> getAllMyBoats(){return boats;}

    public void onItemClick(View view,Boat boat) {
        if (oldView!=null) {
            oldView.setSelected(false);
        }
        view.setSelected(true);
        selectedBoat = boat.getId();

        Log.i("clicked",boat.getName());
        boatRepository.setActiveBoat(boat.getId());

        oldView = view;
    }

    public int getSelectedBoat() {
        return selectedBoat;
    }

    public void setSelectedBoat(int selectedBoat) {
        this.selectedBoat = selectedBoat;
    }

    public View getOldView() {
        return oldView;
    }

    public void setOldView(View oldView) {
        this.oldView = oldView;
    }

    public void getAllBoatsFromServer(){
        boatRepository.getAllBoatsFromServer();
    }
}