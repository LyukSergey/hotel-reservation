package com.example.hotelreservation.rest;

import com.example.hotelreservation.db.entity.Reservation;
import com.example.hotelreservation.db.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class ReservationController {
    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(path = "/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        List<Reservation> reservations = reservationService.findAll();
        return ResponseEntity.of(Optional.ofNullable(reservations));
    }

    @PostMapping(path = "/reservations")
    public ResponseEntity<Reservation> createReservations(@RequestBody Reservation reservation) {
        return ResponseEntity.of(Optional.ofNullable(reservationService.saveOrUpdate(reservation)));
    }

    @PutMapping(path = "/reservations")
    public ResponseEntity<Reservation> updateReservations(@RequestBody Reservation data) {
        return ResponseEntity.of(Optional.ofNullable(reservationService.saveOrUpdate(data)));
    }

    @DeleteMapping(path = "/reservations/{id}")
    public void deleteReservations(@PathVariable Long id) {
        reservationService.deleteById(id);
    }

    @GetMapping(path = "/reservations/between")
    public ResponseEntity<List<Reservation>> getReservationsByDateRange(
            @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Reservation> reservations = reservationService.findReservationsByDateRange(startDate, endDate);
        return ResponseEntity.of(Optional.ofNullable(reservations));
    }
}
