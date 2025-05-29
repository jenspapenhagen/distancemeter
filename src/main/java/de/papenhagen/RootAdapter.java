package de.papenhagen;

import de.papenhagen.enities.Root;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.bind.adapter.JsonbAdapter;


public class RootAdapter implements JsonbAdapter<Root, JsonObject> {
    @Override
    public JsonObject adaptToJson(Root root) throws Exception {
        return Json.createObjectBuilder()
                .add("lat", root.lat())
                .add("lon", root.lon())
                .add("state", root.state())
                .add("zipCode", root.zipCode())
                .build();

    }

    @Override
    public Root adaptFromJson(JsonObject jsonObject) throws Exception {

        return new Root(jsonObject.getJsonNumber("lat").doubleValue(),
                jsonObject.getJsonNumber("log").doubleValue(),
                jsonObject.getJsonString("state").getString(),
                jsonObject.getJsonNumber("zipCode").intValue());
    }
}
