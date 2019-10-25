package com.jgeodesy.base;

import com.jgeodesy.coordinate.Coordinate;
import com.jgeodesy.coordinate.Latitude;
import com.jgeodesy.coordinate.Longitude;
import com.jgeodesy.util.GeodesyUtil;

/**
 * Created by omeruluoglu on 24.10.2019.
 */
public class SphericalPoint extends Point {

    /**
     * Default initialize constructor
     */
    public SphericalPoint() {
        super();
    }

    /**
     * Initialize spherical point
     * @param latitude latitude
     * @param longitude longitude
     */
    public SphericalPoint(final Latitude latitude, final Longitude longitude) {
        super(latitude, longitude);
    }

    /**
     * Returns the distance along the surface of the earth from ‘this’ point to destination point
     * Uses haversine formula: a = sin²(Δφ/2) + cosφ1·cosφ2 · sin²(Δλ/2); d = 2 · atan2(√a, √(a-1))
     * @param sphericalPoint Latitude/longitude of destination point
     * @param radius Radius of earth (defaults to mean radius in metres)
     * @return Distance between this point and destination point, in same units as radius
     */
    public Double distanceTo(final SphericalPoint sphericalPoint, double radius) {
        // see mathforum.org/library/drmath/view/51879.html for derivation
        double phi1 = getLatitude().getRadians();
        double lambda1 = getLongitude().getRadians();
        double phi2 = sphericalPoint.getLatitude().getRadians();
        double lambda2 = sphericalPoint.getLongitude().getRadians();
        double deltaPhi = phi2 - phi1;
        double deltaLambda = lambda2 - lambda1;
        double a = Math.sin(deltaPhi / 2.0) * Math.sin(deltaPhi / 2.0) +
                Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2.0) * Math.sin(deltaLambda / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return radius * c;
    }

    /**
     * Returns the initial bearing from ‘this’ point to destination point
     * @param sphericalPoint Latitude/longitude of destination point
     * @return Initial bearing in degrees from north (0°..360°)
     */
    public Double initialBearingTo(final SphericalPoint sphericalPoint) {
        // see mathforum.org/library/drmath/view/55417.html for derivation
        double phi1 = getLatitude().getRadians();
        double phi2 = sphericalPoint.getLatitude().getRadians();
        double deltaLambda = sphericalPoint.getLongitude().getRadians()- getLongitude().getRadians();
        double y = Math.sin(deltaLambda) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(deltaLambda);
        double theta = Math.atan2(y, x);
        return GeodesyUtil.wrapTo360(Coordinate.toDegrees(theta));
    }
}