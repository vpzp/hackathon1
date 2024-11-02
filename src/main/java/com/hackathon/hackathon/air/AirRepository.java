package com.hackathon.hackathon.air;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirRepository extends JpaRepository<Air, Long > {
    Optional<Air> findByDateAndAndStationName(String date, String stationName);

    List<Air> findAll();
}
