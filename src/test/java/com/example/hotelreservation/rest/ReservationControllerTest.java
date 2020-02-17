package com.example.hotelreservation.rest;

import com.example.hotelreservation.db.entity.Reservation;
import com.example.hotelreservation.db.service.ReservationService;
import com.example.hotelreservation.utils.JsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ReservationControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservationService service;

    @Test
    void test_getReservations() throws Exception {
        //GIVEN
        when(service.findAll()).thenReturn(initListOfReservations());

        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk());

        //THEN
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        List<Reservation> reservations = Arrays.asList(objectMapper.readValue(contentAsString, Reservation[].class));
        Reservation returnedReservation = reservations.get(0);

        verify(service, times(1)).findAll();
        assertThat(reservations).hasSize(2);
        assertThat(returnedReservation.getId()).isEqualTo(1L);
        assertThat(returnedReservation.getRoomNumber()).isEqualTo(1);
        assertThat(returnedReservation.getLastName()).isEqualTo("LastName1");
        assertThat(returnedReservation.getFirstName()).isEqualTo("FirstName1");
        assertThat(returnedReservation.getStartDate()).isEqualTo(LocalDate.of(2020, 01, 01));
        assertThat(returnedReservation.getEndDate()).isEqualTo(LocalDate.of(2020, 01, 10));
    }

    @Test
    void test_createReservations() throws Exception {
        //GIVEN
        Reservation requestBody = initReservation1();

        when(service.saveOrUpdate(requestBody)).thenReturn(requestBody);

        //WHEN
        ResultActions resultActions = mockMvc.perform(
                post("/reservations")
                        .characterEncoding(StandardCharsets.UTF_8.displayName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConverter.convertObjectToJsonString(requestBody)))
                .andExpect(status().isOk());

        //THEN
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Reservation reservation = objectMapper.readValue(contentAsString, Reservation.class);

        verify(service, times(1)).saveOrUpdate(requestBody);
        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getRoomNumber()).isEqualTo(1);
        assertThat(reservation.getLastName()).isEqualTo("LastName1");
        assertThat(reservation.getFirstName()).isEqualTo("FirstName1");
        assertThat(reservation.getStartDate()).isEqualTo(LocalDate.of(2020, 01, 01));
        assertThat(reservation.getEndDate()).isEqualTo(LocalDate.of(2020, 01, 10));
    }

    @Test
    void test_updateReservations() throws Exception {
        //GIVEN
        Reservation requestBody = initReservation1();
        requestBody.setRoomNumber(22);
        requestBody.setFirstName("updated_first_name");
        requestBody.setLastName("updated_last_name");

        when(service.saveOrUpdate(requestBody)).thenReturn(requestBody);

        //WHEN
        ResultActions resultActions = mockMvc.perform(
                put("/reservations")
                        .characterEncoding(StandardCharsets.UTF_8.displayName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConverter.convertObjectToJsonString(requestBody)))
                .andExpect(status().isOk());
        //THEN
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Reservation reservation = objectMapper.readValue(contentAsString, Reservation.class);

        verify(service, times(1)).saveOrUpdate(requestBody);
        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getRoomNumber()).isEqualTo(22);
        assertThat(reservation.getLastName()).isEqualTo("updated_last_name");
        assertThat(reservation.getFirstName()).isEqualTo("updated_first_name");
        assertThat(reservation.getStartDate()).isEqualTo(LocalDate.of(2020, 01, 01));
        assertThat(reservation.getEndDate()).isEqualTo(LocalDate.of(2020, 01, 10));
    }


    @Test
    void test_deleteReservations() throws Exception {
        //GIVEN
        //WHEN
        ResultActions resultActions = mockMvc.perform(
                delete("/reservations/1"))
                .andExpect(status().isOk());
        //THEN
        verify(service, times(1)).deleteById(1L);
    }

    @Test
    void test_getReservationsByDateRange() throws Exception {
        //GIVEN
        LocalDate startDate = LocalDate.of(2020, 01, 01);
        LocalDate endDate = LocalDate.of(2020, 01, 31);

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("startDate", "2020-01-01");
        requestParams.add("endDate", "2020-01-31");

        List<Reservation> foundedReservations = Arrays.asList(initReservation1(), initReservation2());

        when(service.findReservationsByDateRange(startDate, endDate)).thenReturn(foundedReservations);

        //WHEN
        ResultActions resultActions = mockMvc.perform(
                get("/reservations/between").params(requestParams))
                .andExpect(status().isOk());
        //THEN
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        List<Reservation> returnedReservations = Arrays.asList(objectMapper.readValue(contentAsString, Reservation[].class));

        verify(service, times(1)).findReservationsByDateRange(startDate, endDate);
        assertThat(returnedReservations).hasSize(2);
        assertThat(returnedReservations).containsOnly(foundedReservations.get(0), foundedReservations.get(1));
    }

    private Reservation initReservation1() {
        Reservation reservation_1 = new Reservation();
        reservation_1.setId(1L);
        reservation_1.setRoomNumber(1);
        reservation_1.setLastName("LastName1");
        reservation_1.setFirstName("FirstName1");
        reservation_1.setStartDate(LocalDate.of(2020, 01, 01));
        reservation_1.setEndDate(LocalDate.of(2020, 01, 10));
        return reservation_1;
    }

    private Reservation initReservation2() {
        Reservation reservation_2 = new Reservation();
        reservation_2.setId(2L);
        reservation_2.setRoomNumber(2);
        reservation_2.setLastName("LastName1");
        reservation_2.setFirstName("FirstName1");
        reservation_2.setStartDate(LocalDate.of(2020, 01, 11));
        reservation_2.setEndDate(LocalDate.of(2020, 01, 21));
        return reservation_2;
    }

    private List<Reservation> initListOfReservations() {
        return Arrays.asList(initReservation1(), initReservation2());
    }
}