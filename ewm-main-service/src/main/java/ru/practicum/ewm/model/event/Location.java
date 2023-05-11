package ru.practicum.ewm.model.event;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Location {

    private double lat;

    private double lon;
}
