package nl.soft.pelorus.pelorus3.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.util.Log;

import static nl.soft.pelorus.pelorus3.entity.User.TABLE_NAME;


/**
 * Created by tobia on 3-8-2017.
 */

@Entity(tableName = TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "users";

    @PrimaryKey
    @ColumnInfo(name="id")
    private String id;
    @ColumnInfo(name="name")
    private String name;

    private String photo;


    public User(String id, String name,String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name){ this.name = name;}

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
