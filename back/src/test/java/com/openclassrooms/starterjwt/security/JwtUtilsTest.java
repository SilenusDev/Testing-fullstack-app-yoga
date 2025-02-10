package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        // Initialisation des mocks et injection des valeurs nécessaires dans jwtUtils
        MockitoAnnotations.openMocks(this);

        // Configuration de jwtUtils avec une clé secrète et un temps d'expiration
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "customSecretKey");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 7200000); // 2 heures
    }

    @Test
    void shouldGenerateJwtToken() {
        // Préparation : Création d'un utilisateur mock pour le test
        UserDetailsImpl userDetails = new UserDetailsImpl(101L, "alphaUser", "Alice", "Smith", false, "pass123");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Action : Génération d'un jeton JWT
        String token = jwtUtils.generateJwtToken(authentication);

        // Assertion : Vérification que le jeton n'est pas nul et semble être un JWT valide
        assertNotNull(token, "Le jeton JWT généré ne doit pas être nul");
        assertTrue(token.startsWith("eyJ"), "Le jeton généré doit commencer par 'eyJ', indiquant un format JWT valide");
    }

    @Test
    void shouldExtractUsernameFromJwtToken() {
        // Préparation : Génération d'un JWT valide
        UserDetailsImpl userDetails = new UserDetailsImpl(102L, "betaUser", "Bob", "Johnson", true, "pass456");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);

        // Action : Extraction du nom d'utilisateur à partir du jeton
        String username = jwtUtils.getUserNameFromJwtToken(token);

        // Assertion : Vérification que le nom d'utilisateur extrait correspond à la valeur attendue
        assertEquals("betaUser", username, "Le nom d'utilisateur extrait du jeton doit correspondre à la valeur attendue");
    }

    @Test
    void shouldValidateJwtToken_WhenTokenIsValid() {
        // Préparation : Génération d'un JWT valide
        UserDetailsImpl userDetails = new UserDetailsImpl(103L, "gammaUser", "Charlie", "Brown", false, "pass789");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);

        // Action : Validation du jeton
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assertion : Vérification que le jeton est valide
        assertTrue(isValid, "Un jeton JWT valide doit passer la validation");
    }

    @Test
    void shouldInvalidateJwtToken_WhenTokenIsInvalid() {
        // Préparation : Définition d'un jeton invalide
        String invalidToken = "invalidJWT";

        // Action : Validation du jeton invalide
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // Assertion : Vérification que le jeton est considéré comme invalide
        assertFalse(isValid, "Un jeton JWT invalide ne doit pas passer la validation");
    }

    @Test
    void shouldInvalidateJwtToken_WhenMalformed() {
        // Préparation : Définition d'un jeton malformé
        String malformedToken = "malformedJWT";

        // Action : Validation du jeton malformé
        boolean isValid = jwtUtils.validateJwtToken(malformedToken);

        // Assertion : Vérification que le jeton malformé est considéré comme invalide
        assertFalse(isValid, "Un jeton JWT malformé ne doit pas passer la validation");
    }

    @Test
    void shouldInvalidateJwtToken_WhenEmpty() {
        // Préparation : Définition d'un jeton vide
        String emptyToken = "";

        // Action : Validation du jeton vide
        boolean isValid = jwtUtils.validateJwtToken(emptyToken);

        // Assertion : Vérification que le jeton vide est considéré comme invalide
        assertFalse(isValid, "Un jeton JWT vide ne doit pas passer la validation");
    }

    @Test
    void shouldInvalidateJwtToken_WhenExpired() {
        // Préparation : Configuration de jwtUtils avec une expiration négative pour simuler un jeton expiré
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", -5000); // Jeton expiré
        UserDetailsImpl userDetails = new UserDetailsImpl(104L, "deltaUser", "Diana", "Prince", true, "passSecret");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String expiredToken = jwtUtils.generateJwtToken(authentication);

        // Action : Validation du jeton expiré
        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        // Assertion : Vérification que le jeton expiré est considéré comme invalide
        assertFalse(isValid, "Un jeton JWT expiré ne doit pas passer la validation");
    }
}