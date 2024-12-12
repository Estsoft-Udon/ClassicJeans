package com.example.classicjeans.service;

import com.example.classicjeans.entity.HospitalData;
import com.example.classicjeans.entity.Reservation;
import com.example.classicjeans.entity.ReservationQueue;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.HospitalRepository;
import com.example.classicjeans.repository.ReservationRepository;
import com.example.classicjeans.dto.request.ReservationRequest;
import com.example.classicjeans.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableScheduling
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private ReservationQueue reservationQueue;

    @Mock
    private ReservationNotificationService notificationService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Users mockUser;

    @Mock
    private HospitalData mockHospital;

    private Reservation mockReservation;

    @BeforeAll
    static void setUpClass() {
        mockStatic(SecurityUtil.class);
    }

    @BeforeEach
    void setUp() {
        // mock 객체 초기화
        mockUser = mock(Users.class);
        mockHospital = mock(HospitalData.class);
        mockReservation = new Reservation(mockUser, "John Doe", mockHospital, LocalDateTime.now().plusDays(1));

        // 예시 예약 객체를 저장할 때 호출될 때마다 mock 예약을 반환
        when(SecurityUtil.getLoggedInUser()).thenReturn(mockUser); // static 메소드 mock
        when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);
        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.of(mockReservation));
        when(hospitalRepository.findById(any(Long.class))).thenReturn(Optional.of(mockHospital));
    }

    @Test
    void testAddReservation() {
        ReservationRequest request = new ReservationRequest();
        request.setHospitalId(1L);
        request.setReserverName("John Doe");
        request.setTime(LocalDateTime.now().plusDays(1));

        Reservation result = reservationService.addReservation(request);

        assertNotNull(result);
        assertEquals("John Doe", result.getReserverName());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testGetNextReservation() {
        when(reservationQueue.getNextReservation()).thenReturn(mockReservation);

        Reservation result = reservationService.getNextReservation();

        assertNotNull(result);
        assertEquals(mockReservation, result);
    }

    @Test
    void testNotifyReservation() {
        Reservation result = reservationService.notifyReservation(mockReservation);

        assertTrue(result.getIsNotificated());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testDeleteReservationById() {
        reservationService.deleteReservationById(1L);

        // 삭제 메소드가 호출되었는지 확인
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testToggleReadStatus() {
        Reservation result = reservationService.toggleReadStatus(1L);

        assertTrue(result.getIsRead());
        result = reservationService.toggleReadStatus(1L);

        assertFalse(result.getIsRead());
        verify(reservationRepository, times(2)).save(any(Reservation.class));
    }
}
