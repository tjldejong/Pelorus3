package nl.soft.pelorus.pelorus3.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import nl.soft.pelorus.pelorus3.entity.Boat;

/**
 * Created by tobia on 25-8-2017.
 */

public interface BoatRepository {
    void getAllBoatsFromServer();

    LiveData<Boat> getBoat(int id);

    LiveData<Boat> getMyBoat();

    LiveData<List<Boat>> getMyBoats();

    void addBoat(String boatname, double sw);

    void setActiveBoat(int id);

    void updateBoat(Boat boat);
}
