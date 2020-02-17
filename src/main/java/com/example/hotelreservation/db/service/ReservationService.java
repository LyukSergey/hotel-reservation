package com.example.hotelreservation.db.service;

import com.example.hotelreservation.db.entity.Reservation;
import com.example.hotelreservation.db.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation saveOrUpdate(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Entity with id [%s] was not found.", id)
        ));
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findReservationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate);
    }
}
