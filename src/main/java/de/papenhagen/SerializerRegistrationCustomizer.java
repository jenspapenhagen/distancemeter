package de.papenhagen;


import jakarta.inject.Singleton;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

@Singleton
public class SerializerRegistrationCustomizer {

    public Jsonb jsonb() {
        JsonbConfig config = new JsonbConfig();
        config.withAdapters(new RootAdapter());
        return JsonbBuilder.create(config);
    }
}



