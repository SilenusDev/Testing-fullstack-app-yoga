package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Annotation pour indiquer que cette classe est un test Spring Boot
@SpringBootTest
public class SessionMapperTest {

    // Injection de dépendance pour le mapper de sessions
    @Autowired
    private SessionMapper sessionMapper;

    // Mock du service utilisateur pour simuler son comportement
    @MockBean
    private UserService userService;

    // Mock du service enseignant pour simuler son comportement
    @MockBean
    private TeacherService teacherService;

    // Objets de test
    private Teacher teacher;
    private User user1;
    private User user2;

    // Méthode exécutée avant chaque test pour initialiser l'environnement de test
    @BeforeEach
    void setupTestEnvironment() {
        // Initialisation d'un objet Teacher avec des valeurs prédéfinies
        teacher = Teacher.builder()
                .id(1001L)
                .firstName("Sarah")
                .lastName("Connor")
                .build();

        // Initialisation d'objets User avec des valeurs prédéfinies
        user1 = new User()
                .setId(501L)
                .setFirstName("Michael")
                .setLastName("Smith");

        user2 = new User()
                .setId(502L)
                .setFirstName("Alice")
                .setLastName("Johnson");

        // Configuration des comportements des mocks pour retourner les objets initialisés
        when(teacherService.findById(1001L)).thenReturn(teacher);
        when(userService.findById(501L)).thenReturn(user1);
        when(userService.findById(502L)).thenReturn(user2);
    }

    // Test pour vérifier la conversion d'une liste de sessions en liste de DTO de sessions
    @Test
    void shouldMapSessionListToSessionDtoList() {
        // Préparation : création d'objets Session
        Session sessionOne = Session.builder()
                .id(3001L)
                .name("Art Workshop")
                .teacher(teacher)
                .build();

        Session sessionTwo = Session.builder()
                .id(3002L)
                .name("Music Class")
                .teacher(teacher)
                .build();

        List<Session> sessionList = List.of(sessionOne, sessionTwo);

        // Action : conversion des objets Session en objets SessionDto
        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessionList);

        // Assertion : validation des résultats de la conversion
        assertNotNull(sessionDtoList, "La liste de SessionDto ne doit pas être nulle");
        assertEquals(2, sessionDtoList.size(), "La taille de la liste de SessionDto doit être 2");
        assertEquals(sessionOne.getId(), sessionDtoList.get(0).getId(), "L'ID de la première session doit correspondre");
        assertEquals(sessionTwo.getId(), sessionDtoList.get(1).getId(), "L'ID de la deuxième session doit correspondre");
    }

    // Test pour vérifier la conversion d'une liste de DTO de sessions en liste de sessions
    @Test
    void shouldMapSessionDtoListToSessionList() {
        // Préparation : création d'objets SessionDto
        SessionDto sessionDtoOne = new SessionDto();
        sessionDtoOne.setId(3001L);
        sessionDtoOne.setName("Art Workshop");
        sessionDtoOne.setTeacher_id(1001L);

        SessionDto sessionDtoTwo = new SessionDto();
        sessionDtoTwo.setId(3002L);
        sessionDtoTwo.setName("Music Class");
        sessionDtoTwo.setTeacher_id(1001L);

        List<SessionDto> sessionDtoList = List.of(sessionDtoOne, sessionDtoTwo);

        // Action : conversion des objets SessionDto en objets Session
        List<Session> sessionList = sessionMapper.toEntity(sessionDtoList);

        // Assertion : validation des résultats de la conversion
        assertNotNull(sessionList, "La liste de Session ne doit pas être nulle");
        assertEquals(2, sessionList.size(), "La taille de la liste de Session doit être 2");
        assertEquals(sessionDtoOne.getId(), sessionList.get(0).getId(), "L'ID de la première session doit correspondre");
        assertEquals(sessionDtoTwo.getId(), sessionList.get(1).getId(), "L'ID de la deuxième session doit correspondre");
    }
}
