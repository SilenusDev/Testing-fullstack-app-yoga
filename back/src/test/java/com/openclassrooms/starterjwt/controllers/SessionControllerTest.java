package com.openclassrooms.starterjwt.controllers;


import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    public void setUp() {
        // Initialise les mocks avant chaque test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        // Préparation des données de test
        Long sessionId = 1L;
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();

        // Configuration des comportements des mocks
        when(sessionService.getById(sessionId)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Appel de la méthode à tester
        ResponseEntity<?> response = sessionController.findById(sessionId.toString());

        // Vérification des résultats
        assertEquals(ResponseEntity.ok(sessionDto), response);
        verify(sessionService, times(1)).getById(sessionId);
        verify(sessionMapper, times(1)).toDto(session);
    }

    @Test
    public void testFindAll() {
        // Préparation des données de test
        List<Session> sessions = Arrays.asList(new Session(), new Session());
        List<SessionDto> sessionDtos = Arrays.asList(new SessionDto(), new SessionDto());

        // Configuration des comportements des mocks
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        // Appel de la méthode à tester
        ResponseEntity<?> response = sessionController.findAll();

        // Vérification des résultats
        assertEquals(ResponseEntity.ok(sessionDtos), response);
        verify(sessionService, times(1)).findAll();
        verify(sessionMapper, times(1)).toDto(sessions);
    }

    @Test
    public void testCreate() {
        // Préparation des données de test
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();

        // Configuration des comportements des mocks
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Appel de la méthode à tester
        ResponseEntity<?> response = sessionController.create(sessionDto);

        // Vérification des résultats
        assertEquals(ResponseEntity.ok(sessionDto), response);
        verify(sessionMapper, times(1)).toEntity(sessionDto);
        verify(sessionService, times(1)).create(session);
        verify(sessionMapper, times(1)).toDto(session);
    }

    @Test
    public void testUpdate() {
        // Préparation des données de test
        Long sessionId = 1L;
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();

        // Configuration des comportements des mocks
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(sessionId, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Appel de la méthode à tester
        ResponseEntity<?> response = sessionController.update(sessionId.toString(), sessionDto);

        // Vérification des résultats
        assertEquals(ResponseEntity.ok(sessionDto), response);
        verify(sessionMapper, times(1)).toEntity(sessionDto);
        verify(sessionService, times(1)).update(sessionId, session);
        verify(sessionMapper, times(1)).toDto(session);
    }

    @Test
    public void testDelete() {
        // Préparation des données de test
        Long sessionId = 1L;
        Session session = new Session();

        // Configuration des comportements des mocks
        when(sessionService.getById(sessionId)).thenReturn(session);

        // Appel de la méthode à tester
        ResponseEntity<?> response = sessionController.save(sessionId.toString());

        // Vérification des résultats
        assertEquals(ResponseEntity.ok().build(), response);
        verify(sessionService, times(1)).getById(sessionId);
        verify(sessionService, times(1)).delete(sessionId);
    }

    @Test
    public void testParticipate() {
        // Préparation des données de test
        Long sessionId = 1L;
        Long userId = 1L;

        // Appel de la méthode à tester
        ResponseEntity<?> response = sessionController.participate(sessionId.toString(), userId.toString());

        // Vérification des résultats
        assertEquals(ResponseEntity.ok().build(), response);
        verify(sessionService, times(1)).participate(sessionId, userId);
    }

    @Test
    public void testNoLongerParticipate() {
        // Préparation des données de test
        Long sessionId = 1L;
        Long userId = 1L;

        // Appel de la méthode à tester
        ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId.toString(), userId.toString());

        // Vérification des résultats
        assertEquals(ResponseEntity.ok().build(), response);
        verify(sessionService, times(1)).noLongerParticipate(sessionId, userId);
    }
}
