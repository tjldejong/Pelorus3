package nl.soft.pelorus.pelorus3.ui.mainmenu.events;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.databinding.ListItemEventBinding;
import nl.soft.pelorus.pelorus3.entity.Event;

/**
 * Created by tobia on 25-8-2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private List<Event> items;
    private EventFragmentViewModel eventFragmentViewModel;

    EventAdapter(List<Event> items, EventFragmentViewModel eventFragmentViewModel) {
        this.items = items;
        this.eventFragmentViewModel = eventFragmentViewModel;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemEventBinding listItemEventBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.list_item_event,parent,false);
        return new EventViewHolder(listItemEventBinding);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.bindEvents(items.get(position),eventFragmentViewModel);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void setItems(List<Event> events) {
        this.items = events;
        notifyDataSetChanged();
    }

}
