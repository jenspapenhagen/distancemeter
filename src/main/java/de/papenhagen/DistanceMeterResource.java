package de.papenhagen;

import de.papenhagen.enities.Distance;
import de.papenhagen.enities.Root;
import de.papenhagen.service.InfoCrawler;
import de.papenhagen.utils.MeasuringUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


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

        final double callableMeasuring = MeasuringUtil.calculateDistance(location1, location2);

        return new Distance(plz1, plz2, callableMeasuring);
    }

}
