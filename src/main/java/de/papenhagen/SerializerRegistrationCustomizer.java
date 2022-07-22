package de.papenhagen;

import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

@Singleton
public class SerializerRegistrationCustomizer {

    public Jsonb jsonb() {
        JsonbConfig config = new JsonbConfig();
        config.withAdapters(new RootAdapter());
        return JsonbBuilder.create(config);
    }
}



