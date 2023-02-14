package de.papenhagen.service;

import de.papenhagen.enities.Root;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Slf4j
public class MeasuringService {

    @Inject
    InfoCrawler infoCrawler;

    //radius of Earth in kilometer WGS84
    private static final double EQUATOR_RADIUS = 6378.137;

    public double callableMeasuring(final Double plz1, final Double plz2) throws IllegalArgumentException {

        //getting the locations form the cache or call them
        final Root location1 = infoCrawler.callBackend(plz1);
        final Root location2 = infoCrawler.callBackend(plz2);

        final double lat01 = location1.getLat();
        final double lon01 = location1.getLon();

        final double lat02 = location2.getLat();
        final double lon02 = location2.getLon();

        final double dLat = Math.toRadians(lat02 - lat01);
        final double dLon = Math.toRadians(lon02 - lon01);

        final double latDelta = Math.sin(dLat / 2);
        final double lonDelta = Math.sin(dLon / 2);

        final double lat01Radios = Math.toRadians(lat01);
        final double lat02Radios = Math.toRadians(lat02);
        final double a = (latDelta * latDelta) + (lonDelta * lonDelta * Math.cos(lat01Radios) * Math.cos(lat02Radios));

        final double angleDistanceRadians = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));


        return EQUATOR_RADIUS * angleDistanceRadians;
    }

}
