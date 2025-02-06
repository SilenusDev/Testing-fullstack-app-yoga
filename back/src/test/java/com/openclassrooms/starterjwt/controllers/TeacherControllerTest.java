package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
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

public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    public void setUp() {
        // Initialise les mocks avant chaque test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        // Préparation des données de test
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        TeacherDto teacherDto = new TeacherDto();

        // Configuration des comportements des mocks
        when(teacherService.findById(teacherId)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // Appel de la méthode à tester
        ResponseEntity<?> response = teacherController.findById(teacherId.toString());

        // Vérification des résultats
        assertEquals(ResponseEntity.ok(teacherDto), response);
        verify(teacherService, times(1)).findById(teacherId);
        verify(teacherMapper, times(1)).toDto(teacher);
    }

    @Test
    public void testFindAll() {
        // Préparation des données de test
        List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
        List<TeacherDto> teacherDtos = Arrays.asList(new TeacherDto(), new TeacherDto());

        // Configuration des comportements des mocks
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        // Appel de la méthode à tester
        ResponseEntity<?> response = teacherController.findAll();

        // Vérification des résultats
        assertEquals(ResponseEntity.ok(teacherDtos), response);
        verify(teacherService, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(teachers);
    }
}
