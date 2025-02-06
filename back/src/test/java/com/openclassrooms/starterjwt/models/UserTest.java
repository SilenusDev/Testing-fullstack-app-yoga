package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User primaryUser;
    private User secondaryUser;

    @BeforeEach
    public void initializeUsers() {
        // Initialisation : Créer des objets User avec des valeurs prédéfinies
        primaryUser = new User(1001L, "alpha@domain.com", "Smith", "Alice", "securePass123", false,
                LocalDateTime.now(), LocalDateTime.now());
        secondaryUser = new User(1002L, "beta@domain.com", "Johnson", "Bob", "anotherPass456", true,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    public void shouldReturnCorrectStringRepresentation() {
        // Initialisation : Préparer la chaîne de caractères attendue
        String expected = "User(id=1001, email=alpha@domain.com, lastName=Smith, firstName=Alice, password=securePass123, admin=false, createdAt="
                + primaryUser.getCreatedAt() + ", updatedAt=" + primaryUser.getUpdatedAt() + ")";

        // Action : Obtenir la chaîne de caractères réelle à partir de la méthode toString
        String actual = primaryUser.toString();

        // Assertion : Comparer la sortie réelle avec la chaîne de caractères attendue
        assertEquals(expected, actual, "La méthode toString doit retourner la représentation correcte sous forme de chaîne de caractères de l'objet User");
    }

    @Test
    public void shouldHaveConsistentHashCodeForSameAttributes() {
        // Initialisation : Créer un autre objet User avec les mêmes attributs que primaryUser
        User duplicateUser = new User(1001L, "alpha@domain.com", "Smith", "Alice", "securePass123", false,
                primaryUser.getCreatedAt(), primaryUser.getUpdatedAt());

        // Assertion : Vérifier que les hashCodes sont identiques pour des utilisateurs ayant les mêmes attributs
        assertEquals(primaryUser.hashCode(), duplicateUser.hashCode(),
                "Les hashCodes doivent être identiques pour des utilisateurs ayant les mêmes attributs");

        // Assertion : Vérifier que les hashCodes diffèrent pour des utilisateurs ayant des ID différents
        assertNotEquals(primaryUser.hashCode(), secondaryUser.hashCode(),
                "Les hashCodes doivent différer pour des utilisateurs ayant des ID différents");
    }

    @Test
    public void shouldBeEqualForSameAttributes() {
        // Initialisation : Créer un autre objet User avec les mêmes attributs que primaryUser
        User duplicateUser = new User(1001L, "alpha@domain.com", "Smith", "Alice", "securePass123", false,
                primaryUser.getCreatedAt(), primaryUser.getUpdatedAt());

        // Action & Assertion : Vérifier que primaryUser est égal à un autre User ayant les mêmes attributs
        assertTrue(primaryUser.equals(duplicateUser), "Les utilisateurs ayant le même ID et les mêmes attributs doivent être considérés comme égaux");

        // Action & Assertion : Vérifier que primaryUser n'est pas égal à secondaryUser (ID différents)
        assertFalse(primaryUser.equals(secondaryUser), "Les utilisateurs ayant des ID différents ne doivent pas être considérés comme égaux");

        // Action & Assertion : Vérifier que primaryUser n'est pas égal à null
        assertFalse(primaryUser.equals(null), "Un utilisateur ne doit pas être égal à null");

        // Action & Assertion : Vérifier que primaryUser n'est pas égal à un objet d'un autre type
        assertFalse(primaryUser.equals(new Object()), "Un utilisateur ne doit pas être égal à un objet d'un autre type");
    }
}
