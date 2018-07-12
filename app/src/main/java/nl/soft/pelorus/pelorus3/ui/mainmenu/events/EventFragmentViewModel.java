package nl.soft.pelorus.pelorus3.ui.mainmenu.events;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import nl.soft.pelorus.pelorus3.entity.Event;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.repository.EventRepository;

/**
 * Created by tobia on 25-8-2017.
 */

public class EventFragmentViewModel extends ViewModel implements PelorusComponent.Injectable {
    @Inject
    EventRepository eventRepository;

    @Inject
    SharedPreferences sharedPreferences;

    private int selectedEvent;
    private View oldView;

    @Override
    public void inject(PelorusComponent pelorusComponent) {
        pelorusComponent.inject(this);
    }

    public LiveData<List<Event>> getAllEvents(){return eventRepository.getAllEvents();}

    public void onItemClick(View view, Event event) {
        if (oldView!=null){
            oldView.setSelected(false);
        }
        view.setSelected(true);
        selectedEvent = event.getId();

        Log.i("clicked",event.getName());
        eventRepository.setActiveEvent(event.getId());

        oldView = view;
    }

    public int getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(int selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public View getOldView() {
        return oldView;
    }

    public void setOldView(View oldView) {
        this.oldView = oldView;
    }

    public void setEventId(int eventid){
        sharedPreferences.edit().putInt("eventId",eventid).apply();
    }

}
