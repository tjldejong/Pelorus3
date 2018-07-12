package nl.soft.pelorus.pelorus3.ui.boats.manage;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import nl.soft.pelorus.pelorus3.api.MySQLDatabase.MySQLDatabaseClient;
import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.repository.BoatRepository;

/**
 * Created by tobia on 26-8-2017.
 */

public class ManageBoatViewModel extends ViewModel implements PelorusComponent.Injectable {

    @Inject
    BoatRepository boatRepository;

    @Inject
    MySQLDatabaseClient mySQLDatabaseClient;

    private String boatname;
    private double sw;

    @Override
    public void inject(PelorusComponent pelorusComponent) {
        pelorusComponent.inject(this);
    }

    public String getBoatName() {
        return boatname;
    }

    public void setBoatName(String boatname) {
        this.boatname = boatname;
    }

    public double getSw() {
        return sw;
    }

    public void setSw(double sw) {
        this.sw = sw;
    }

    public void addBoat(){
        boatRepository.addBoat(boatname,sw);
    }


}
