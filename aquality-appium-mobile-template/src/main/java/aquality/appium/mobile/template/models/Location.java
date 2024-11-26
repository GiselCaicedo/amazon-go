package aquality.appium.mobile.template.models;

public class Location {
    private double latitude;
    private double longitude;
    private long timestamp;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = System.currentTimeMillis();
    }

    public double getLatitude() { 
        return latitude; 
    }

    public void setLatitude(double latitude) { 
        this.latitude = latitude; 
    }

    public double getLongitude() { 
        return longitude; 
    }

    public void setLongitude(double longitude) { 
        this.longitude = longitude; 
    }

    public long getTimestamp() { 
        return timestamp; 
    }

    public void setTimestamp(long timestamp) { 
        this.timestamp = timestamp; 
    }
}