package de.papenhagen.enities;

import lombok.Value;

@Value
public class Root {
    double lat;
    double lon;
    String state;
    int zipCode;
}
