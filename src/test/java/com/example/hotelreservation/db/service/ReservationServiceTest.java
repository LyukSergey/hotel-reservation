package com.example.hotelreservation.db.service;

import com.example.hotelreservation.BaseIntegrationTest;
import com.example.hotelreservation.db.entity.Reservation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class ReservationServiceTest extends BaseIntegrationTest {
    @Autowired
    private ReservationService reservationService;

    @Test
    @DatabaseSetup(value = "classpath:/db/reservationServiceIT/findAll.xml")
    public void test_findAll() {
        //GIVEN
        //WHEN
        List<Reservation> reservations = reservationService.findAll();

        //THEN
        assertThat(reservations).hasSize(2);
    }

    @Test
    @DatabaseSetup(value = "classpath:/db/reservationServiceIT/save.xml")
    void test_save() {
        //GIVEN
        Reservation reservation = new Reservation();
        reservation.setFirstName("FirstName");
        reservation.setLastName("LastName");
        reservation.setRoomNumber(1);
        reservation.setStartDate(LocalDate.of(2020, 02, 01));
        reservation.setEndDate(LocalDate.of(2020, 02, 02));

        //WHEN
        reservationService.saveOrUpdate(reservation);

        //THEN
        List<Reservation> reservations = reservationService.findAll();
        Reservation saved = reservations.get(0);
        assertThat(reservations).hasSize(1);
        assertThat(saved.getFirstName()).isEqualTo("FirstName");
        assertThat(saved.getLastName()).isEqualTo("LastName");
        assertThat(saved.getRoomNumber()).isEqualTo(1);
        assertThat(saved.getStartDate()).isEqualTo(LocalDate.of(2020, 02, 01));
        assertThat(saved.getEndDate()).isEqualTo(LocalDate.of(2020, 02, 02));
    }

    @Test
    @DatabaseSetup(value = "classpath:/db/reservationServiceIT/update.xml")
    void test_update() {
        //GIVEN
        Reservation reservation = reservationService.findById(2L);
        reservation.setFirstName("updated_first_name");
        reservation.setLastName("update_last_name");

        //WHEN
        Reservation updatedReservation = reservationService.saveOrUpdate(reservation);

        //THEN
        assertThat(updatedReservation.getId()).isEqualTo(2L);
        assertThat(updatedReservation.getFirstName()).isEqualTo("updated_first_name");
        assertThat(updatedReservation.getLastName()).isEqualTo("update_last_name");
        assertThat(reservation.getStartDate()).isEqualTo(LocalDate.of(2020, 02, 12));
        assertThat(reservation.getEndDate()).isEqualTo(LocalDate.of(2020, 02, 18));
    }

    @Test
    @DatabaseSetup(value = "classpath:/db/reservationServiceIT/findById.xml")
    void test_findById() {
        //GIVEN
        //WHEN
        Reservation reservation = reservationService.findById(2L);

        //THEN
        assertThat(reservation).isNotNull();
        assertThat(reservation.getFirstName()).isEqualTo("first_name2");
        assertThat(reservation.getLastName()).isEqualTo("last_name2");
        assertThat(reservation.getRoomNumber()).isEqualTo(2);
        assertThat(reservation.getStartDate()).isEqualTo(LocalDate.of(2020, 02, 12));
        assertThat(reservation.getEndDate()).isEqualTo(LocalDate.of(2020, 02, 18));
    }

    @Test
    @DatabaseSetup(value = "classpath:/db/reservationServiceIT/findById.xml")
    void test_findById_Exception() {
        //GIVEN
        //WHEN
        //THEN exception
        assertThatThrownBy(() -> reservationService.findById(3L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Entity with id [3] not found.");
    }

    @Test
    @DatabaseSetup(value = "classpath:/db/reservationServiceIT/delete.xml")
    void test_delete() {
        //GIVEN
        List<Reservation> reservations = reservationService.findAll();
        assertThat(reservations).hasSize(2);
        Reservation reservation = reservations.get(0);

        //WHEN
        reservationService.deleteById(reservation.getId());

        //THEN
        reservations = reservationService.findAll();
        assertThat(reservations).hasSize(1);
    }

    @Test
    @DatabaseSetup(value = "classpath:/db/reservationServiceIT/findByDateRange.xml")
    public void test_findReservationsByDateRange() {
        //GIVEN
        LocalDate from = LocalDate.of(2020, 02, 01);
        LocalDate to = LocalDate.of(2020, 03, 01);

        //WHEN
        List<Reservation> reservations = reservationService.findReservationsByDateRange(from, to);

        //THEN
        assertThat(reservations).hasSize(3);
    }
}