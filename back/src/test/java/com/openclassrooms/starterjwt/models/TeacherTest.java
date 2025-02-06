package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    private Teacher teacherAlpha;
    private Teacher teacherBeta;

    @BeforeEach
    public void initializeTeachers() {
        // Initialisation : Créer des objets Teacher avec des valeurs prédéfinies
        teacherAlpha = new Teacher(101L, "Johnson", "Alice", LocalDateTime.now(), LocalDateTime.now());
        teacherBeta = new Teacher(202L, "Brown", "Michael", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    public void shouldVerifyToStringContent() {
        // Action : Appeler la méthode toString sur teacherAlpha
        String output = teacherAlpha.toString();

        // Assertion : Vérifier que la sortie contient les valeurs attendues
        assertTrue(output.contains("id=101"), "La sortie de toString doit contenir 'id=101'");
        assertTrue(output.contains("lastName=Johnson"), "La sortie de toString doit contenir 'lastName=Johnson'");
        assertTrue(output.contains("firstName=Alice"), "La sortie de toString doit contenir 'firstName=Alice'");
        assertTrue(output.contains("createdAt="), "La sortie de toString doit contenir 'createdAt='");
        assertTrue(output.contains("updatedAt="), "La sortie de toString doit contenir 'updatedAt='");
    }

    @Test
    public void shouldHaveSameHashCodeForSameId() {
        // Initialisation : Créer un autre objet Teacher avec le même ID que teacherAlpha
        Teacher duplicateTeacher = new Teacher(101L, "Johnson", "Alice", teacherAlpha.getCreatedAt(), teacherAlpha.getUpdatedAt());

        // Assertion : Vérifier que les hashCodes sont identiques
        assertEquals(teacherAlpha.hashCode(), duplicateTeacher.hashCode(),
                "Les hashCodes doivent être identiques pour des enseignants ayant le même ID");
    }

    @Test
    public void shouldHaveDifferentHashCodesForDifferentIds() {
        // Assertion : Vérifier que les hashCodes diffèrent pour des ID différents
        assertNotEquals(teacherAlpha.hashCode(), teacherBeta.hashCode(),
                "Les hashCodes doivent différer pour des enseignants ayant des ID différents");
    }

    @Test
    public void shouldBeEqualWhenIdsMatch() {
        // Initialisation : Créer un autre objet Teacher avec le même ID que teacherAlpha
        Teacher duplicateTeacher = new Teacher(101L, "Johnson", "Alice", teacherAlpha.getCreatedAt(), teacherAlpha.getUpdatedAt());

        // Assertion : Vérifier que les objets sont égaux
        assertEquals(teacherAlpha, duplicateTeacher, "Les enseignants ayant le même ID doivent être considérés comme égaux");
    }

    @Test
    public void shouldNotBeEqualWhenIdsDiffer() {
        // Assertion : Vérifier que teacherAlpha et teacherBeta ne sont pas égaux en raison de leurs ID différents
        assertNotEquals(teacherAlpha, teacherBeta, "Les enseignants ayant des ID différents ne doivent pas être égaux");
    }

    @Test
    public void shouldNotEqualNullOrDifferentClass() {
        // Assertion : Vérifier que teacherAlpha n'est pas égal à null ou à un type d'objet différent
        assertNotEquals(null, teacherAlpha, "Un enseignant ne doit pas être égal à null");
        assertNotEquals(teacherAlpha, new Object(), "Un enseignant ne doit pas être égal à un objet d'un autre type");
    }

    @Test
    public void shouldUpdateCreatedAtCorrectly() {
        // Initialisation : Définir un nouvel horodatage pour createdAt
        LocalDateTime newTimestamp = LocalDateTime.now().minusDays(5);

        // Action : Mettre à jour la valeur de createdAt
        teacherAlpha.setCreatedAt(newTimestamp);

        // Assertion : Vérifier que la valeur de createdAt est mise à jour
        assertEquals(newTimestamp, teacherAlpha.getCreatedAt(),
                "L'horodatage createdAt doit être mis à jour avec la nouvelle valeur");
    }

    @Test
    public void shouldUpdateUpdatedAtCorrectly() {
        // Initialisation : Définir un nouvel horodatage pour updatedAt
        LocalDateTime newTimestamp = LocalDateTime.now().minusDays(3);

        // Action : Mettre à jour la valeur de updatedAt
        teacherAlpha.setUpdatedAt(newTimestamp);

        // Assertion : Vérifier que la valeur de updatedAt est mise à jour
        assertEquals(newTimestamp, teacherAlpha.getUpdatedAt(),
                "L'horodatage updatedAt doit être mis à jour avec la nouvelle valeur");
    }
}
