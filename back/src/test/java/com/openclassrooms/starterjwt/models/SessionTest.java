package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    @Test
    public void shouldVerifyEqualsAndHashCode() {
        // Initialisation : Créer deux objets Session différents
        Session firstSession = new Session();
        firstSession.setId(101L).setName("Morning Yoga");

        Session secondSession = new Session();
        secondSession.setId(202L).setName("Evening Meditation");

        // Action & Assertion : Vérifier que les sessions avec des ID différents ne sont pas égales
        assertNotEquals(firstSession, secondSession, "Les sessions avec des ID différents ne doivent pas être égales");

        // Initialisation : Mettre à jour l'ID de la deuxième session pour qu'il corresponde à celui de la première session
        secondSession.setId(101L);

        // Assertion : Vérifier que les sessions avec le même ID sont maintenant égales
        assertEquals(firstSession, secondSession, "Les sessions avec des ID correspondants doivent être considérées comme égales");
    }

    @Test
    public void shouldSetCreatedAtAndUpdatedAtCorrectly() {
        // Initialisation : Initialiser une nouvelle instance de session
        Session session = new Session();

        // Action : Définir l'horodatage createdAt
        LocalDateTime creationTimestamp = LocalDateTime.now();
        session.setCreatedAt(creationTimestamp);

        // Assertion : S'assurer que l'horodatage createdAt est correctement défini
        assertEquals(creationTimestamp, session.getCreatedAt(), "L'horodatage createdAt doit correspondre à la valeur assignée");

        // Action : Définir un horodatage updatedAt différent
        LocalDateTime updateTimestamp = LocalDateTime.now().plusDays(2);
        session.setUpdatedAt(updateTimestamp);

        // Assertion : S'assurer que l'horodatage updatedAt est correctement défini
        assertEquals(updateTimestamp, session.getUpdatedAt(), "L'horodatage updatedAt doit correspondre à la valeur assignée");
    }

    @Test
    public void shouldVerifyCanEqual() {
        // Initialisation : Créer deux instances de session
        Session sessionA = new Session();
        Session sessionB = new Session();

        // Action & Assertion : Vérifier que sessionA peut être comparée à sessionB
        assertTrue(sessionA.canEqual(sessionB), "sessionA doit pouvoir être comparée à sessionB");
    }
}
