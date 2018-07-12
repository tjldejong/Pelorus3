package nl.soft.pelorus.pelorus3.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static nl.soft.pelorus.pelorus3.entity.Event.TABLE_NAME;

/**
 * Created by tobia on 25-8-2017.
 */
@Entity(tableName = TABLE_NAME)
public class Event {
    public static final String TABLE_NAME = "events";


    @PrimaryKey
    @ColumnInfo(name="id")
    private int id;
    @ColumnInfo(name="name")
    private String name;

    private long starttime;

    public Event(int id, String name, long starttime) {
        this.id = id;
        this.name = name;
        this.starttime = starttime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

}
