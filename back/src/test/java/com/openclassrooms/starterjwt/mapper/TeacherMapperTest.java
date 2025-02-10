package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Annotation pour indiquer que cette classe est un test Spring Boot
@SpringBootTest
public class TeacherMapperTest {

    // Utilisation de l'instance réelle du mapper généré par MapStruct
    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    // Test pour vérifier la conversion d'un DTO Teacher en entité Teacher
    @Test
    void shouldMapTeacherDtoToTeacherCorrectly() {
        // Préparation : création d'un objet TeacherDto avec des données de test
        TeacherDto dto = new TeacherDto();
        dto.setId(42L);
        dto.setLastName("Taylor");
        dto.setFirstName("Alex");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        // Conversion de TeacherDto en Teacher
        Teacher teacher = teacherMapper.toEntity(dto);

        // Assertions pour vérifier que la conversion est correcte
        assertNotNull(teacher, "L'objet Teacher ne doit pas être null");
        assertEquals(dto.getId(), teacher.getId(), "L'ID doit correspondre");
        assertEquals(dto.getLastName(), teacher.getLastName(), "Le nom de famille doit correspondre");
        assertEquals(dto.getFirstName(), teacher.getFirstName(), "Le prénom doit correspondre");
        assertEquals(dto.getCreatedAt(), teacher.getCreatedAt(), "La date de création doit correspondre");
        assertEquals(dto.getUpdatedAt(), teacher.getUpdatedAt(), "La date de mise à jour doit correspondre");
    }

    // Test pour vérifier la conversion d'une liste de DTO Teacher en liste d'entités Teacher
    @Test
    void shouldMapTeacherDtoListToTeacherList() {
        // Création d'une liste d'objets TeacherDto
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(10L);
        dto1.setLastName("Parker");
        dto1.setFirstName("Jordan");

        TeacherDto dto2 = new TeacherDto();
        dto2.setId(11L);
        dto2.setLastName("Adams");
        dto2.setFirstName("Morgan");

        List<TeacherDto> dtoList = List.of(dto1, dto2);

        // Conversion de la liste de TeacherDto en liste de Teacher
        List<Teacher> teachers = teacherMapper.toEntity(dtoList);

        // Assertions pour vérifier que la conversion est correcte
        assertNotNull(teachers, "La liste de Teacher ne doit pas être nulle");
        assertEquals(2, teachers.size(), "La taille de la liste de Teacher doit être 2");
        assertEquals(dto1.getId(), teachers.get(0).getId(), "L'ID du premier enseignant doit correspondre");
        assertEquals(dto2.getId(), teachers.get(1).getId(), "L'ID du deuxième enseignant doit correspondre");
    }

    // Test pour vérifier la conversion d'une entité Teacher en DTO Teacher
    @Test
    void shouldMapTeacherToTeacherDtoCorrectly() {
        // Préparation : création d'un objet Teacher avec des données de test
        Teacher teacher = Teacher.builder()
                .id(25L)
                .lastName("Brown")
                .firstName("Jamie")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Conversion de Teacher en TeacherDto
        TeacherDto dto = teacherMapper.toDto(teacher);

        // Assertions pour vérifier que la conversion est correcte
        assertNotNull(dto, "L'objet TeacherDto ne doit pas être null");
        assertEquals(teacher.getId(), dto.getId(), "L'ID doit correspondre");
        assertEquals(teacher.getLastName(), dto.getLastName(), "Le nom de famille doit correspondre");
        assertEquals(teacher.getFirstName(), dto.getFirstName(), "Le prénom doit correspondre");
        assertEquals(teacher.getCreatedAt(), dto.getCreatedAt(), "La date de création doit correspondre");
        assertEquals(teacher.getUpdatedAt(), dto.getUpdatedAt(), "La date de mise à jour doit correspondre");
    }

    // Test pour vérifier la conversion d'une liste d'entités Teacher en liste de DTO Teacher
    @Test
    void shouldMapTeacherListToTeacherDtoList() {
        // Création d'une liste d'objets Teacher
        Teacher teacher1 = Teacher.builder().id(7L).lastName("Harris").firstName("Taylor").build();
        Teacher teacher2 = Teacher.builder().id(8L).lastName("Clark").firstName("Riley").build();

        List<Teacher> teacherList = List.of(teacher1, teacher2);

        // Conversion de la liste de Teacher en liste de TeacherDto
        List<TeacherDto> dtoList = teacherMapper.toDto(teacherList);

        // Assertions pour vérifier que la conversion est correcte
        assertNotNull(dtoList, "La liste de TeacherDto ne doit pas être nulle");
        assertEquals(2, dtoList.size(), "La taille de la liste de TeacherDto doit être 2");
        assertEquals(teacher1.getId(), dtoList.get(0).getId(), "L'ID du premier TeacherDto doit correspondre");
        assertEquals(teacher2.getId(), dtoList.get(1).getId(), "L'ID du deuxième TeacherDto doit correspondre");
    }
}
