package nl.soft.pelorus.pelorus3.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static nl.soft.pelorus.pelorus3.entity.Location.TABLE_NAME;

/**
 * Created by tobia on 5-9-2017.
 */
@Entity(tableName = TABLE_NAME)
public class Location {
    public static final String TABLE_NAME = "locations";

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "boatid")
    private int boatid;
    @ColumnInfo(name = "eventid")
    private int eventid;
    @ColumnInfo(name = "time")
    private Long time;
    @ColumnInfo(name = "lat")
    private double lat;
    @ColumnInfo(name = "lng")
    private double lng;
    @ColumnInfo(name = "bearing")
    private float bearing;
    @ColumnInfo(name = "speed")
    private float speed;

    private int nextMark;

    private double vmg;

    private long roundtime;

    private double totalDistance;

    public Location(int id, int boatid, int eventid, Long time, double lat, double lng, float bearing, float speed, int nextMark, double vmg, long roundtime, double totalDistance){
        this.id = id;
        this.boatid = boatid;
        this.eventid = eventid;
        this.time = time;
        this.lat = lat;
        this.lng = lng;
        this.bearing = bearing;
        this.speed = speed;
        this.nextMark = nextMark;
        this.vmg = vmg;
        this.roundtime = roundtime;
        this.totalDistance = totalDistance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBoatid() {
        return boatid;
    }

    public void setBoatid(int boatid) {
        this.boatid = boatid;
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getNextMark() {
        return nextMark;
    }

    public void setNextMark(int nextMark) {
        this.nextMark = nextMark;
    }

    public double getVmg() {
        return vmg;
    }

    public void setVmg(double vmg) {
        this.vmg = vmg;
    }

    public long getRoundtime() {
        return roundtime;
    }

    public void setRoundtime(long roundtime) {
        this.roundtime = roundtime;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }
}
