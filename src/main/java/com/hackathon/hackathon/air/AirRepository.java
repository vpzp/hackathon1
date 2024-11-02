package com.hackathon.hackathon.air;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirRepository extends JpaRepository<Air, Long > {
}
