package de.papenhagen.service;

import de.papenhagen.enities.Root;
import lombok.NonNull;

public class MeasuringService {

    //radius of Earth in kilometer WGS84
    private static final double EQUATOR_RADIUS = 6378.137;

    public static double calculateDistance(@NonNull final Root location1, @NonNull final Root location2) throws IllegalArgumentException {
        // all split into single lines for better profiling.
        final double lat01 = location1.getLat();
        final double lon01 = location1.getLon();

        final double lat02 = location2.getLat();
        final double lon02 = location2.getLon();

        final double Lat1InRadian = Math.toRadians(lat01);
        final double Lat2InRadian = Math.toRadians(lat02);

        final double cosLat1 = Math.cos(Lat1InRadian);
        final double cosLat2 = Math.cos(Lat2InRadian);

        final double toGether = cosLat1 * cosLat2;

        final double deltaLat = lat02 - lat01;
        final double deltaLon = lon02 - lon01;

        final double dLat = Math.toRadians(deltaLat);
        final double dLon = Math.toRadians(deltaLon);

        final double halfdLat = dLat / 2;
        final double halfdLon = dLon / 2;

        final double part1 = Math.sin(halfdLat);
        final double part2 = Math.sin(halfdLon);

        final double doublePart1 = part1 * part1;
        final double doublePart2 = part2 * part2;

        final double a = Math.fma(doublePart2, toGether, doublePart1);

        final double sqrtOfA = Math.sqrt(a);
        final double restOfA = 1 - a;
        final double sqrtRestOfA = Math.sqrt(restOfA);

        final double angleDistanceRadians = 2 * Math.atan2(sqrtOfA, sqrtRestOfA);

        return EQUATOR_RADIUS * angleDistanceRadians;
    }

}
