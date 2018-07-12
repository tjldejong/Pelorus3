package nl.soft.pelorus.pelorus3.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import io.reactivex.annotations.Nullable;

import static nl.soft.pelorus.pelorus3.entity.Boat.TABLE_NAME;

/**
 * Created by tobia on 25-8-2017.
 */

@Entity(tableName = TABLE_NAME)
public class Boat {
    public static final String TABLE_NAME = "boats";

    @PrimaryKey
    @ColumnInfo(name="id")
    private int id;
    @ColumnInfo(name="name")
    private String name;

    private String captain;

    private double sw;

    private int competesIn;

    public Boat(int id, String name, String captain, double sw, int competesIn) {
        this.id = id;
        this.name = name;
        this.captain = captain;
        this.sw = sw;
        this.competesIn = competesIn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName() { return name; }

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public double getSw() {
        return sw;
    }

    public void setSw(double sw) {
        this.sw = sw;
    }

    public int getCompetesIn() {
        return competesIn;
    }

    public void setCompetesIn(int competesIn) {
        this.competesIn = competesIn;
    }
}
