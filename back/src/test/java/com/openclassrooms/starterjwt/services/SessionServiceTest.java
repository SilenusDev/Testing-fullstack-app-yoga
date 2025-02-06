package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    public void setUp() {
        // Initialisation : Initialiser les mocks avant chaque test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSession() {
        // Arrange : Préparer une session à créer
        Session session = new Session();
        session.setId(1L);
        when(sessionRepository.save(session)).thenReturn(session);

        // Act : Appeler la méthode create du service
        Session result = sessionService.create(session);

        // Assert : Vérifier que la session retournée est la même que celle créée
        assertNotNull(result, "La session créée ne doit pas être null");
        assertEquals(session.getId(), result.getId(), "L'ID de la session retournée doit correspondre à l'ID de la session créée");
    }

    @Test
    public void testDeleteSession() {
        // Arrange : Préparer l'ID de la session à supprimer
        Long sessionId = 1L;

        // Act : Appeler la méthode delete du service
        sessionService.delete(sessionId);

        // Assert : Vérifier que la méthode deleteById de sessionRepository a été appelée avec le bon ID
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    public void testFindAllSessions() {
        // Arrange : Préparer une liste de sessions et configurer le mock pour retourner cette liste
        Session session1 = new Session();
        session1.setId(1L);
        Session session2 = new Session();
        session2.setId(2L);
        List<Session> sessionList = Arrays.asList(session1, session2);

        when(sessionRepository.findAll()).thenReturn(sessionList);

        // Act : Appeler la méthode findAll du service
        List<Session> result = sessionService.findAll();

        // Assert : Vérifier que la liste retournée est la même que celle configurée
        assertNotNull(result, "La liste des sessions ne doit pas être null");
        assertEquals(2, result.size(), "La liste doit contenir deux sessions");
        assertEquals(session1.getId(), result.get(0).getId(), "La première session doit avoir l'ID 1");
        assertEquals(session2.getId(), result.get(1).getId(), "La deuxième session doit avoir l'ID 2");
    }

    @Test
    public void testGetSessionById_SessionExists() {
        // Arrange : Préparer une session et configurer le mock pour retourner cette session
        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Act : Appeler la méthode getById du service
        Session result = sessionService.getById(sessionId);

        // Assert : Vérifier que la session retournée est la même que celle configurée
        assertNotNull(result, "La session doit être trouvée et ne doit pas être null");
        assertEquals(sessionId, result.getId(), "L'ID de la session retournée doit correspondre à l'ID recherché");
    }

    @Test
    public void testGetSessionById_SessionDoesNotExist() {
        // Arrange : Configurer le mock pour retourner un Optional vide
        Long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act : Appeler la méthode getById du service
        Session result = sessionService.getById(sessionId);

        // Assert : Vérifier que le résultat est null lorsque la session n'existe pas
        assertNull(result, "Le résultat doit être null lorsque la session n'existe pas");
    }

    @Test
    public void testUpdateSession() {
        // Arrange : Préparer une session à mettre à jour
        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        when(sessionRepository.save(session)).thenReturn(session);

        // Act : Appeler la méthode update du service
        Session result = sessionService.update(sessionId, session);

        // Assert : Vérifier que la session retournée est la même que celle mise à jour
        assertNotNull(result, "La session mise à jour ne doit pas être null");
        assertEquals(sessionId, result.getId(), "L'ID de la session retournée doit correspondre à l'ID de la session mise à jour");
    }

    @Test
    public void testParticipateInSession_Success() {
        // Arrange : Préparer une session et un utilisateur
        Long sessionId = 1L;
        Long userId = 1L;
        Session session = new Session();
        session.setUsers(new ArrayList<>()); // Initialisation de la liste users
        User user = new User();
        user.setId(userId);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act : Appeler la méthode participate du service
        sessionService.participate(sessionId, userId);

        // Assert : Vérifier que l'utilisateur a été ajouté à la session
        assertTrue(session.getUsers().contains(user), "L'utilisateur doit être ajouté à la session");
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testParticipateInSession_SessionOrUserNotFound() {
        // Arrange : Configurer le mock pour retourner un Optional vide pour la session ou l'utilisateur
        Long sessionId = 1L;
        Long userId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act & Assert : Vérifier que l'exception NotFoundException est levée
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId),
                "Une NotFoundException doit être levée lorsque la session ou l'utilisateur n'existe pas");
    }

    @Test
    public void testParticipateInSession_AlreadyParticipating() {
        // Arrange : Préparer une session et un utilisateur déjà participant
        Long sessionId = 1L;
        Long userId = 1L;
        Session session = new Session();
        session.setUsers(new ArrayList<>()); // Initialisation de la liste users
        User user = new User();
        user.setId(userId);
        session.getUsers().add(user);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert : Vérifier que l'exception BadRequestException est levée
        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId),
                "Une BadRequestException doit être levée lorsque l'utilisateur participe déjà à la session");
    }

    @Test
    public void testNoLongerParticipateInSession_Success() {
        // Arrange : Préparer une session et un utilisateur
        Long sessionId = 1L;
        Long userId = 1L;
        Session session = new Session();
        session.setUsers(new ArrayList<>()); // Initialisation de la liste users
        User user = new User();
        user.setId(userId);
        session.getUsers().add(user);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Act : Appeler la méthode noLongerParticipate du service
        sessionService.noLongerParticipate(sessionId, userId);

        // Assert : Vérifier que l'utilisateur a été retiré de la session
        assertFalse(session.getUsers().contains(user), "L'utilisateur ne doit plus être dans la session");
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testNoLongerParticipateInSession_SessionNotFound() {
        // Arrange : Configurer le mock pour retourner un Optional vide pour la session
        Long sessionId = 1L;
        Long userId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act & Assert : Vérifier que l'exception NotFoundException est levée
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId),
                "Une NotFoundException doit être levée lorsque la session n'existe pas");
    }

    @Test
    public void testNoLongerParticipateInSession_NotParticipating() {
        // Arrange : Préparer une session et un utilisateur non participant
        Long sessionId = 1L;
        Long userId = 1L;
        Session session = new Session();
        session.setUsers(new ArrayList<>()); // Initialisation de la liste users
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Act & Assert : Vérifier que l'exception BadRequestException est levée
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId),
                "Une BadRequestException doit être levée lorsque l'utilisateur ne participe pas à la session");
    }
}
