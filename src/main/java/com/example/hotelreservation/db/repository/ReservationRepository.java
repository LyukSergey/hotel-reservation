package com.example.hotelreservation.db.repository;

import com.example.hotelreservation.db.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(
            @Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate);
}
