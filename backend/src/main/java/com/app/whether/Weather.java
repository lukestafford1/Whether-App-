package com.app.whether;

import jakarta.persistence.*;

@Entity
@Table(name = "weather_tbl")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
