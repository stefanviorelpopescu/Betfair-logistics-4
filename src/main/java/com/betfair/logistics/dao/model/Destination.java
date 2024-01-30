package com.betfair.logistics.dao.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "destinations")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer distance;

//    @OneToMany(cascade=ALL, mappedBy="destination")
//    List<Order> orders;
}
