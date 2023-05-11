package ru.practicum.ewm.model.event;

import javax.persistence.Embeddable;

@Embeddable
public class Location {

    private double lat;

    private double lon;
}
