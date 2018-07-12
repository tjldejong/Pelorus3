package nl.soft.pelorus.pelorus3.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import nl.soft.pelorus.pelorus3.entity.Event;


/**
 * Created by tobia on 25-8-2017.
 */

@Dao
public interface EventDao {
    @Query("SELECT * FROM " + Event.TABLE_NAME + " WHERE id = :id LIMIT 1")
    LiveData<Event> getEvent(int id);

    @Query("SELECT * FROM " + Event.TABLE_NAME)
    LiveData<List<Event>> getAllEvents();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addEvent(Event event);

    @Query("DELETE FROM "+ Event.TABLE_NAME)
    void nukeTable();
}
