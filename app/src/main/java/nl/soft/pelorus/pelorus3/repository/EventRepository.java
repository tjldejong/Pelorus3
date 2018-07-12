package nl.soft.pelorus.pelorus3.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import nl.soft.pelorus.pelorus3.entity.Event;


/**
 * Created by tobia on 25-8-2017.
 */

public interface EventRepository {
    LiveData<Event> getEvent();

    LiveData<List<Event>> getAllEvents();

    void setActiveEvent(int id);
}
