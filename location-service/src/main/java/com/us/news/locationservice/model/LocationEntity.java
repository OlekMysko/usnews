package com.us.news.locationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "location")
public class LocationEntity {
    @Id
    @GeneratedValue
    @Column(name = "location_id", nullable = false)
    private UUID locationId;
    @Column(name = "location_name", nullable = false)
    private String locationName;
    @Column(name = "location_latitude", nullable = false)
    private double locationLatitude;
    @Column(name = "location_longitude", nullable = false)
    private double locationLongitude;
}