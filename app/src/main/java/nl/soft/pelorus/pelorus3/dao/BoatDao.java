package nl.soft.pelorus.pelorus3.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;
import nl.soft.pelorus.pelorus3.entity.Boat;

/**
 * Created by tobia on 25-8-2017.
 */
@Dao
public interface BoatDao {
    @Query("SELECT * FROM " + Boat.TABLE_NAME)
    LiveData<List<Boat>> getAllBoats();

    @Query("SELECT * FROM " + Boat.TABLE_NAME + " WHERE id=:id LIMIT 1")
    LiveData<Boat> getBoat(int id);

    @Query("SELECT * FROM " + Boat.TABLE_NAME + " WHERE captain = :id")
    LiveData<List<Boat>> getBoatsFrom(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBoat(Boat boat);

    @Update
    void updateBoat(Boat boat);

    @Query("DELETE FROM "+Boat.TABLE_NAME)
    void nukeTable();


}
