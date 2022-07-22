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

    private static final double EARTH_RADIUS = 6371.0;

    public double callableMeasuring(final Double plz1,final Double plz2) throws IllegalArgumentException {
        //getting the locations form the cache or call them
        final Root location1 = infoCrawler.callBackend(plz1);
        final Root location2 = infoCrawler.callBackend(plz2);

        final double phi1 = Math.toRadians(location1.getLat());
        final double lambda1 = Math.toRadians(location1.getLon());
        final double phi2 = Math.toRadians(location2.getLat());
        final double lambda2 = Math.toRadians(location2.getLon());

        final double angleDistanceRadians = Math.acos(
                Math.max(
                        Math.min(
                                Math.sin(phi1) * Math.sin(phi2)
                                        + Math.cos(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1),
                                1.0
                        ),
                        -1.0
                )
        );

        return EARTH_RADIUS * angleDistanceRadians;
    }

}
