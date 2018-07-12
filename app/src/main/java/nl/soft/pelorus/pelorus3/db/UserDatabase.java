package nl.soft.pelorus.pelorus3.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import nl.soft.pelorus.pelorus3.dao.BoatDao;
import nl.soft.pelorus.pelorus3.dao.EventDao;
import nl.soft.pelorus.pelorus3.dao.LocationDao;
import nl.soft.pelorus.pelorus3.dao.MarkDao;
import nl.soft.pelorus.pelorus3.dao.UserDao;
import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.entity.Event;
import nl.soft.pelorus.pelorus3.entity.Location;
import nl.soft.pelorus.pelorus3.entity.Mark;
import nl.soft.pelorus.pelorus3.entity.User;

/**
 * Created by tobia on 3-8-2017.
 */
@Database(entities = {User.class,Event.class, Boat.class, Location.class, Mark.class}, version = 39)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract EventDao eventDao();

    public abstract BoatDao boatDao();

    public abstract LocationDao locationDao();

    public abstract MarkDao markDao();
}
