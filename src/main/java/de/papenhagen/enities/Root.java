package de.papenhagen.enities;

import lombok.Value;

@Value
public class Root {
    Double lat;
    Double lon;
    String state;
    Integer zipCode;
}
