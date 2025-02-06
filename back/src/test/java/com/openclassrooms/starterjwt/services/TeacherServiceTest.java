package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        // Initialisation : Initialiser les mocks avant chaque test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllTeachers() {
        // Arrange : Préparer une liste de professeurs et configurer le mock pour retourner cette liste
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        List<Teacher> teacherList = Arrays.asList(teacher1, teacher2);

        when(teacherRepository.findAll()).thenReturn(teacherList);

        // Act : Appeler la méthode findAll du service
        List<Teacher> result = teacherService.findAll();

        // Assert : Vérifier que la liste retournée est la même que celle configurée
        assertNotNull(result, "La liste des professeurs ne doit pas être null");
        assertEquals(2, result.size(), "La liste doit contenir deux professeurs");
        assertEquals(teacher1.getId(), result.get(0).getId(), "Le premier professeur doit avoir l'ID 1");
        assertEquals(teacher2.getId(), result.get(1).getId(), "Le deuxième professeur doit avoir l'ID 2");
    }

    @Test
    public void testFindTeacherById_TeacherExists() {
        // Arrange : Préparer un professeur et configurer le mock pour retourner ce professeur
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        // Act : Appeler la méthode findById du service
        Teacher result = teacherService.findById(teacherId);

        // Assert : Vérifier que le professeur retourné est le même que celui configuré
        assertNotNull(result, "Le professeur doit être trouvé et ne doit pas être null");
        assertEquals(teacherId, result.getId(), "L'ID du professeur retourné doit correspondre à l'ID recherché");
    }

    @Test
    public void testFindTeacherById_TeacherDoesNotExist() {
        // Arrange : Configurer le mock pour retourner un Optional vide
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // Act : Appeler la méthode findById du service
        Teacher result = teacherService.findById(teacherId);

        // Assert : Vérifier que le résultat est null lorsque le professeur n'existe pas
        assertNull(result, "Le résultat doit être null lorsque le professeur n'existe pas");
    }
}
