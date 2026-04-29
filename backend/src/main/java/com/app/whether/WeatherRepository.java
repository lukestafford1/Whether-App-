package com.app.whether;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Object, Long> {
    //Note: Object is just a placeholder here
}
