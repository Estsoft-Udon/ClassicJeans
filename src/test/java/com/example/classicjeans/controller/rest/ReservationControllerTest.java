package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.request.ReservationRequest;
import com.example.classicjeans.entity.HospitalData;
import com.example.classicjeans.entity.Reservation;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.enums.Grade;
import com.example.classicjeans.service.ReservationNotificationService;
import com.example.classicjeans.service.ReservationService;
import com.example.classicjeans.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReservationControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationNotificationService notificationService;

    @InjectMocks
    private ReservationController reservationController;

    Users mockUser;

    @BeforeAll
    static void setUpClass() {
        mockStatic(SecurityUtil.class);
    }

    @BeforeEach
    void setUp() {
        // MockMvc 설정
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();

        mockUser = new Users();
        mockUser.setId(1L);
        mockUser.setLoginId("testLoginId");
        mockUser.setName("testName");
        mockUser.setNickname("testNickname");
        mockUser.setEmail("testEmail");
        mockUser.setGrade(Grade.CHUNGBAZI);
        mockUser.setGender(Gender.MALE);
        mockUser.setPassword("testPassword");
        mockUser.setDateOfBirth(LocalDate.now());
        mockUser.setIsLunar(false);
        mockUser.setHourOfBirth(1);
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setUpdatedAt(LocalDateTime.now());
        mockUser.setUniqueKey(null);

        when(getLoggedInUser()).thenReturn(mockUser);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Java 8 날짜/시간 모듈 등록
    }

    @Test
    void testReserve() throws Exception {
        // 준비: 예약 요청 객체 생성
        ReservationRequest request = new ReservationRequest();
        request.setUserId(1L);
        request.setReserverName("John Doe");
        request.setHospitalId(100L);
        request.setTime(LocalDateTime.of(2024, 12, 11, 14, 30));

        // 준비: 예약 엔티티 생성

        HospitalData hospital = new HospitalData();
        hospital.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(mockUser);
        reservation.setReserverName(request.getReserverName());
        reservation.setHospital(hospital);
        reservation.setTime(request.getTime());

        when(reservationService.addReservation(request)).thenReturn(reservation);

        // 테스트 실행: POST 요청
        mockMvc.perform(post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReservations() throws Exception {
        // 준비: 예약 리스트
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReserverName("John Doe");
        reservation.setHospital(new HospitalData());
        reservation.setTime(LocalDateTime.of(2024, 12, 11, 14, 30));
        reservation.setUser(mockUser);

        when(reservationService.findAll()).thenReturn(Collections.singletonList(reservation));

        // 테스트 실행: GET 요청
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reserverName").value("John Doe"));

        // 서비스 메소드 호출 확인
        verify(reservationService, times(1)).findAll();
    }

    @Test
    void testDeleteReservation() throws Exception {
        // 준비: 예약 ID
        Long reservationId = 1L;

        doNothing().when(reservationService).deleteReservationById(reservationId);

        // 테스트 실행: DELETE 요청
        mockMvc.perform(delete("/api/reservation/{id}", reservationId))
                .andExpect(status().isOk());

        // 서비스 메소드 호출 확인
        verify(reservationService, times(1)).deleteReservationById(reservationId);
    }

    @Test
    void testToggleReservationReadStatus() throws Exception {
        // 준비: 예약 ID 및 상태 변경
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setIsRead(true);

        when(reservationService.toggleReadStatus(reservationId)).thenReturn(reservation);

        // 테스트 실행: PATCH 요청
        mockMvc.perform(patch("/api/reservation/read/{id}", reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationId))
                .andExpect(jsonPath("$.isRead").value(true));

        // 서비스 메소드 호출 확인
        verify(reservationService, times(1)).toggleReadStatus(reservationId);
    }

    @Test
    void testGetNotifications() throws Exception {
        // 준비: 사용자 및 알림 목록
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(mockUser);
        reservation.setReserverName("John Doe");
        reservation.setHospital(new HospitalData());
        reservation.setTime(LocalDateTime.of(2024, 12, 11, 14, 30));

        when(reservationService.findAllByUserId(mockUser.getId())).thenReturn(Collections.singletonList(reservation));

        // 테스트 실행: GET 요청
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        // 서비스 메소드 호출 확인
        verify(reservationService, times(1)).findAllByUserId(mockUser.getId());
    }
}
