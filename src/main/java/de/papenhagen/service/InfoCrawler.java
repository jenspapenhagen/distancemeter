package de.papenhagen.service;

import de.papenhagen.SerializerRegistrationCustomizer;
import de.papenhagen.enities.Root;
import io.quarkus.cache.CacheResult;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.isNull;

/**
 * InfoCrawler to crawl a given URL
 *
 * @author jpapenhagen
 */
@ApplicationScoped
@Slf4j
public class InfoCrawler {

    @ConfigProperty(name = "zipcode.url", defaultValue = "http://localhost:8000/zipcode/")
    URL url;

    @Inject
    SerializerRegistrationCustomizer jsonp;

    private final ExecutorService executorService = Executors.newCachedThreadPool();


    public Root callBackend(final Double zipcode){
        final Root fallback = new Root(0D,0D, "Fallback", 123);
        if(isNull(zipcode)) {
            log.error("try to get distance form invalid zipCode");
            return fallback;
        }

        try(Response response = callRemote(zipcode)) {
            String output = response.readEntity(String.class);

            return jsonp.jsonb().fromJson(output, Root.class);
        } catch (Exception e) {
            log.warn("An Exception get thrown: {}, sending the Fallback", e.getLocalizedMessage());
            return fallback;
        }
    }


    @CacheResult(cacheName = "distance-cache")
    private Response callRemote(final double zipcode) {
        Client client = ClientBuilder
                .newBuilder()
                .executorService(executorService)
                .build();

        return client.target(String.valueOf(url))
                .path(String.valueOf(zipcode))
                .request()
                .get();
    }

}
