package cupid.common.value;

import jakarta.annotation.Nullable;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Point {

    private static final int EARTH_RADIUS_KM = 6371;

    // 위도
    private Double latitude;

    // 위도 (경도)
    private Double longitude;

    public Point() {
    }

    public Point(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean hasLocation() {
        return latitude != null && longitude != null;
    }

    @Nullable
    public Integer distance(Point point) {
        if (point == null || !point.hasLocation() || !this.hasLocation()) {
            return null;
        }

        double lat1Rad = Math.toRadians(this.latitude);
        double lat2Rad = Math.toRadians(point.getLatitude());
        double deltaLat = Math.toRadians(point.getLatitude() - this.latitude);
        double deltaLng = Math.toRadians(point.getLongitude() - this.longitude);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_KM * c;

        return (int) Math.round(distance);
    }
}
