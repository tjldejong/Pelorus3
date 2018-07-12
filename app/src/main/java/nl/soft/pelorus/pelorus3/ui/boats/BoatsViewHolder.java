package nl.soft.pelorus.pelorus3.ui.boats;

import android.support.v7.widget.RecyclerView;

import nl.soft.pelorus.pelorus3.databinding.ListItemBoatBinding;
import nl.soft.pelorus.pelorus3.entity.Boat;

/**
 * Created by tobia on 25-8-2017.
 */

public class BoatsViewHolder extends RecyclerView.ViewHolder {

    private ListItemBoatBinding listItemBoatBinding;

    BoatsViewHolder(ListItemBoatBinding listItemBoatBinding) {
        super(listItemBoatBinding.getRoot());
        this.listItemBoatBinding = listItemBoatBinding;



    }

    void bindBoats(Boat boat, BoatsFragmentViewModel boatsFragmentViewModel){
        if(boatsFragmentViewModel.getSelectedBoat() == boat.getId()){
            listItemBoatBinding.getRoot().setSelected(true);
            boatsFragmentViewModel.setOldView(listItemBoatBinding.getRoot());
        }
        listItemBoatBinding.setBoat(boat);
        listItemBoatBinding.setBoatItemViewModel(boatsFragmentViewModel);
        listItemBoatBinding.executePendingBindings();
    }



}