package nl.soft.pelorus.pelorus3.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import nl.soft.pelorus.pelorus3.entity.Mark;
import nl.soft.pelorus.pelorus3.entity.User;

/**
 * Created by tobia on 12-9-2017.
 */
@Dao
public interface MarkDao {
    @Query("SELECT * FROM "+Mark.TABLE_NAME+" WHERE eventid = :eventid")
    LiveData<List<Mark>> getAllMarks(int eventid);

    @Query("SELECT * FROM "+Mark.TABLE_NAME+" WHERE eventid = :eventid AND number = :number")
    LiveData<Mark> getMark(int eventid,int number);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMark(Mark mark);

    @Query("DELETE FROM "+ Mark.TABLE_NAME)
    void nukeTable();

}
