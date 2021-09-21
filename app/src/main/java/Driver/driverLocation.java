package Driver;

public class driverLocation  {
    private double latitude;
    private double longitude;

    public driverLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

   public driverLocation(){}

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


}
