package de.papenhagen;

import de.papenhagen.enities.Distance;
import de.papenhagen.enities.Root;
import de.papenhagen.service.InfoCrawler;
import de.papenhagen.service.MeasuringService;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class DistanceMeterResource {

    @Inject
    InfoCrawler infoCrawler;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Distance endpoint(@PathParam("plz1") @DefaultValue("123456") final Double plz1,
                             @PathParam("plz2") @DefaultValue("123456") final Double plz2) {

        //getting the locations form the cache or call them
        final Root location1 = this.infoCrawler.callBackend(plz1);
        final Root location2 = this.infoCrawler.callBackend(plz2);

        final double callableMeasuring = MeasuringService.calculateDistance(location1, location2);

        return new Distance(plz1, plz2, callableMeasuring);
    }

}
