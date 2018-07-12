package nl.soft.pelorus.pelorus3.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import nl.soft.pelorus.pelorus3.entity.BoatLocation;
import nl.soft.pelorus.pelorus3.entity.Location;

/**
 * Created by tobia on 5-9-2017.
 */
@Dao
public interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addLocation(Location location);

    @Query("SELECT * FROM "+ Location.TABLE_NAME+" WHERE boatid=:boatid ORDER BY time DESC LIMIT 1 ")
    LiveData<Location> getLocation(double boatid);

    @Query("SELECT * FROM "+ Location.TABLE_NAME+" WHERE eventid=:eventid AND time IN (SELECT MAX(time) FROM "+ Location.TABLE_NAME+" GROUP BY boatid) ")
    LiveData<List<Location>> getLastLocations(int eventid);

    @Query("SELECT boats.name,boats.sw,locations.* FROM boats, locations WHERE locations.eventid = :eventid AND boats.id = locations.boatid AND time IN (SELECT MAX(time) FROM locations GROUP BY boatid)")
    LiveData<List<BoatLocation>>getLastLocationsWithNames(int eventid);

    @Query("DELETE FROM "+Location.TABLE_NAME)
    void nukeTable();
}
