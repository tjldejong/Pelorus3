package nl.soft.pelorus.pelorus3.ui.boats;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.databinding.ListItemBoatBinding;
import nl.soft.pelorus.pelorus3.entity.Boat;

/**
 * Created by tobia on 25-8-2017.
 */

public class BoatsAdapter extends RecyclerView.Adapter<BoatsViewHolder>  {
    private List<Boat> items;
    private BoatsFragmentViewModel boatsFragmentViewModel;



    BoatsAdapter(List<Boat> items,BoatsFragmentViewModel boatsFragmentViewModel) {
        this.items = items;
        this.boatsFragmentViewModel = boatsFragmentViewModel;
    }

    @Override
    public BoatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBoatBinding listItemBoatBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.list_item_boat,parent,false);
        return new BoatsViewHolder(listItemBoatBinding);
    }

    @Override
    public void onBindViewHolder(BoatsViewHolder holder, int position) {
        holder.bindBoats(items.get(position),boatsFragmentViewModel);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void setItems(List<Boat> boats) {
        this.items = boats;
        notifyDataSetChanged();
    }





}
