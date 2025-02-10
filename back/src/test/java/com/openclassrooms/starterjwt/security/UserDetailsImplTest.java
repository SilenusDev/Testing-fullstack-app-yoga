package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

    @Test
    public void shouldBeEqualToItself() {
        // Préparation : Création d'une instance de UserDetailsImpl
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(101L)
                .username("marie@example.com")
                .firstName("Marie")
                .lastName("Dupont")
                .admin(false)
                .password("motDePasseSecurise")
                .build();

        // Action & Assertion : Vérification que l'instance est égale à elle-même
        assertTrue(user.equals(user), "Une instance doit toujours être égale à elle-même.");
    }

    @Test
    public void shouldNotBeEqualToNull() {
        // Préparation : Création d'une instance de UserDetailsImpl
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(102L)
                .username("pierre@example.com")
                .firstName("Pierre")
                .lastName("Martin")
                .admin(false)
                .password("constructeur123")
                .build();

        // Action & Assertion : Vérification que la comparaison avec null retourne faux
        assertFalse(user.equals(null), "Une instance ne doit pas être égale à null.");
    }

    @Test
    public void shouldNotBeEqualToDifferentClass() {
        // Préparation : Création d'une instance de UserDetailsImpl
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(103L)
                .username("sophie@example.com")
                .firstName("Sophie")
                .lastName("Lefèvre")
                .admin(false)
                .password("ticketDoré")
                .build();

        // Action : Comparaison avec un objet d'une classe différente
        String differentClassObject = "IAmNotAUserDetails";

        // Assertion : Vérification que la comparaison avec un objet d'une classe différente retourne faux
        assertFalse(user.equals(differentClassObject),
                "Une instance ne doit pas être égale à un objet d'une classe différente.");
    }

    @Test
    public void shouldNotBeEqualIfIdsAreDifferent() {
        // Préparation : Création de deux instances de UserDetailsImpl avec des ID différents
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(104L)
                .username("jean@example.com")
                .firstName("Jean")
                .lastName("Durand")
                .admin(false)
                .password("aventure")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(105L)
                .username("claire@example.com")
                .firstName("Claire")
                .lastName("Moreau")
                .admin(false)
                .password("pirateMoi")
                .build();

        // Action & Assertion : Vérification que les instances avec des ID différents ne sont pas égales
        assertFalse(user1.equals(user2),
                "Deux instances avec des ID différents ne doivent pas être égales.");
    }

    @Test
    public void shouldCorrectlyIdentifyAdminStatus() {
        // Préparation : Création d'un utilisateur admin et d'un utilisateur régulier
        UserDetailsImpl adminUser = UserDetailsImpl.builder()
                .id(106L)
                .username("admin@platform.com")
                .firstName("Super")
                .lastName("Admin")
                .admin(true)
                .password("accesTotal")
                .build();

        UserDetailsImpl regularUser = UserDetailsImpl.builder()
                .id(107L)
                .username("julie@platform.com")
                .firstName("Julie")
                .lastName("Rousseau")
                .admin(false)
                .password("motDePasse123")
                .build();

        // Action & Assertion : Vérification de la propriété admin
        assertTrue(adminUser.getAdmin(), "La propriété admin doit être vraie pour un utilisateur admin.");
        assertFalse(regularUser.getAdmin(), "La propriété admin doit être fausse pour un utilisateur régulier.");
    }
}
