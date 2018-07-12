package nl.soft.pelorus.pelorus3.ui.race.leaderboard;

import android.arch.lifecycle.ViewModel;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import nl.soft.pelorus.pelorus3.BR;


/**
 * Created by tobia on 13-9-2017.
 */

public class LeaderboardListItemViewModel extends BaseObservable {

    private String name;
    private String time;

    public LeaderboardListItemViewModel() {
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(long seconds) {
        long millis = seconds*1000;
        this.time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        notifyPropertyChanged(BR.time);
    }

}
