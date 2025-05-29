package de.papenhagen.service;

import de.papenhagen.SerializerRegistrationCustomizer;
import de.papenhagen.enities.Root;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.isNull;

/**
 * InfoCrawler to crawl a given URL.
 *
 * @author jens papenhagen
 */
@ApplicationScoped
public class InfoCrawler {

    private static final Logger log = LoggerFactory.getLogger(InfoCrawler.class);

    @ConfigProperty(name = "zipcode.url", defaultValue = "http://localhost:8000/zipcode/")
    URL url;

    @Inject
    SerializerRegistrationCustomizer jsonp;

    private final ExecutorService executorService = Executors.newCachedThreadPool();


    public Root callBackend(final Double zipcode) {
        final Root fallback = new Root(0.0, 0.0, "Fallback", 12323);
        if (isNull(zipcode)) {
            log.error("try to get distance form invalid zipCode");
            return fallback;
        }

        try (Response response = callRemote(zipcode)) {
            final String output = response.readEntity(String.class);

            return jsonp.jsonb().fromJson(output, Root.class);
        }
        catch (Exception e) {
            log.warn("An Exception get thrown: {}, sending the Fallback", e.getLocalizedMessage());
            return fallback;
        }
    }


    @CacheResult(cacheName = "distance-cache")
    public Response callRemote(final double zipcode) {
        try (Client client = ClientBuilder
                .newBuilder()
                .executorService(this.executorService)
                .build()) {

            return client.target(String.valueOf(this.url))
                    .path(String.valueOf(zipcode))
                    .request()
                    .get();
        }
    }

}
