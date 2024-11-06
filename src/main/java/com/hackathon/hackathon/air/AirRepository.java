package com.hackathon.hackathon.air;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface AirRepository extends JpaRepository<Air, Long > {
    Optional<Air> findByDateAndAndStationName(String date, String stationName);

    List<Air> findBySidoNameAndDate(String sidoName, String date);

    List<Air> findAll();

    @Query("SELECT a.sidoName, a.stationName FROM Air a")
    List<Object[]> findAllSidoAndStation();

    default Map<String, List<String>> findSidoStationMap() {
        return findAllSidoAndStation().stream()
                .collect(Collectors.groupingBy(
                        entry -> (String) entry[0],
                        Collectors.mapping(entry -> (String) entry[1], Collectors.toSet())
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new ArrayList<>(entry.getValue())  // Set을 List로 변환
                ));
    }
}
