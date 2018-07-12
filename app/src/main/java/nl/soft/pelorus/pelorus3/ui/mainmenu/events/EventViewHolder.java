package nl.soft.pelorus.pelorus3.ui.mainmenu.events;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.databinding.ListItemEventBinding;
import nl.soft.pelorus.pelorus3.entity.Event;

/**
 * Created by tobia on 25-8-2017.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {

    ListItemEventBinding listItemEventBinding;

    EventViewHolder(ListItemEventBinding listItemEventBinding) {
        super(listItemEventBinding.getRoot());
        this.listItemEventBinding = listItemEventBinding;
    }

    void bindEvents(Event event, EventFragmentViewModel eventFragmentViewModel){
        if(eventFragmentViewModel.getSelectedEvent() == event.getId()){
            listItemEventBinding.getRoot().setSelected(true);
            eventFragmentViewModel.setEventId(event.getId());
            eventFragmentViewModel.setOldView(listItemEventBinding.getRoot());
        }
        listItemEventBinding.setEvent(event);
        listItemEventBinding.setEventItemViewModel(eventFragmentViewModel);
        listItemEventBinding.executePendingBindings();
    }
}
