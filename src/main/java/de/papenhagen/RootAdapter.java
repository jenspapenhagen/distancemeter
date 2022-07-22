package de.papenhagen;

import de.papenhagen.enities.Root;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;

public class RootAdapter implements JsonbAdapter<Root, JsonObject> {
    @Override
    public JsonObject adaptToJson(Root root) throws Exception {
        return Json.createObjectBuilder()
                .add("lat", root.getLat())
                .add("lon", root.getLon())
                .add("state", root.getState())
                .add("zipCode", root.getZipCode())
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
