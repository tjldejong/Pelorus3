package nl.soft.pelorus.pelorus3.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import nl.soft.pelorus.pelorus3.entity.User;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by tobia on 3-8-2017.
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM " + User.TABLE_NAME + " WHERE id = :id LIMIT 1")
    LiveData<User> getUser(String id);

    // @Query("SELECT localId,name FROM "+ User.TABLE_NAME + " WHERE localId = :localId")
    // String getUserName(int localId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM "+User.TABLE_NAME)
    void nukeTable();

    @Update(onConflict = REPLACE)
    void updateUser(User user);
}
