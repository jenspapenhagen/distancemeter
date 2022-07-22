package de.papenhagen;

import de.papenhagen.enities.Distance;
import de.papenhagen.service.MeasuringService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class DistanceMeterResource {

    @Inject
    MeasuringService measuringService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Distance endpoint(@PathParam("plz1") @DefaultValue("123456") final Double plz1,
                             @PathParam("plz2") @DefaultValue("123456") final Double plz2) {
        final double callableMeasuring = measuringService.callableMeasuring(plz1, plz2);
        return new Distance(plz1, plz2, callableMeasuring);
    }

}
