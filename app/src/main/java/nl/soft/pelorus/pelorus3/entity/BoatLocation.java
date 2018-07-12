package nl.soft.pelorus.pelorus3.entity;

/**
 * Created by tobia on 14-9-2017.
 */

public class BoatLocation {

    public String name;
    public double sw;

    public int id;
    public int boatid;
    public int eventid;
    public long time;
    public double lat;
    public double lng;
    public float bearing;
    public double vmg;
    public long roundtime;
    public int nextMark;
    public double totalDistance;
    private Long correctedTime;

    public Long getCorrectedTime() {
        return correctedTime;
    }

    public void setCorrectedTime(Long correctedTime) {
        this.correctedTime = correctedTime;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }
}
