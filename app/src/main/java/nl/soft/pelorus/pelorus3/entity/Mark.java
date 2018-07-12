package nl.soft.pelorus.pelorus3.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static nl.soft.pelorus.pelorus3.entity.Mark.TABLE_NAME;

/**
 * Created by tobia on 12-9-2017.
 */

@Entity(tableName = TABLE_NAME,indices = {@Index(value = {"eventid", "number"},
        unique = true)})
public class Mark {
    public static final String TABLE_NAME = "marks";

    @PrimaryKey
    private int id;

    private int eventid;

    private int number;

    private float lat;

    private float lng;

    public Mark(int id, int eventid, int number, float lat, float lng) {
        this.id = id;
        this.eventid = eventid;
        this.number = number;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

}
