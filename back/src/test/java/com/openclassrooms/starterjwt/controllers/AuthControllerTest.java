package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    // Injection de dépendances pour MockMvc, UserRepository et PasswordEncoder
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Méthode exécutée avant chaque test pour nettoyer la base de données
    @BeforeEach
    public void setUp() {
        userRepository.deleteAll(); // Supprime tous les utilisateurs de la base de données
    }

    // Test pour vérifier l'authentification réussie d'un utilisateur
    @Test
    public void testAuthenticateUser_Success() throws Exception {
        // Préparation : création d'un utilisateur dans la base de données
        User user = new User("sam@test.com", "test", "sam", passwordEncoder.encode("sam!1234"), false);
        userRepository.save(user); // Sauvegarde l'utilisateur dans la base de données

        // Action : envoi d'une requête POST pour se connecter
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"sam@test.com\", \"password\": \"sam!1234\" }"));

        // Assertion : vérification que l'authentification est réussie
        result.andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 OK
                .andExpect(jsonPath("$.token").exists()) // Vérifie que le token est présent dans la réponse
                .andExpect(jsonPath("$.username").value("sam@test.com")) // Vérifie le nom d'utilisateur
                .andExpect(jsonPath("$.id").value(user.getId())) // Vérifie l'ID de l'utilisateur
                .andExpect(jsonPath("$.firstName").value("sam")) // Vérifie le prénom
                .andExpect(jsonPath("$.lastName").value("test")) // Vérifie le nom de famille
                .andExpect(jsonPath("$.admin").value(false)); // Vérifie le statut administrateur
    }

    // Test pour vérifier l'échec de l'authentification avec des identifiants incorrects
    @Test
    public void testAuthenticateUser_Failure() throws Exception {
        // Action : tentative de connexion avec des identifiants incorrects
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"wrong@example.com\", \"password\": \"wrongpassword\" }"));

        // Assertion : vérification que l'authentification échoue
        result.andExpect(status().isUnauthorized()); // Vérifie que le statut de la réponse est 401 Unauthorized
    }

    // Test pour vérifier l'enregistrement réussi d'un utilisateur
    @Test
    public void testRegisterUser_Success() throws Exception {
        // Action : envoi d'une requête POST pour enregistrer un utilisateur
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"sam@test.com\", \"password\": \"sam!1234\", \"firstName\": \"sam\", \"lastName\": \"test\" }"));

        // Assertion : vérification que l'utilisateur est bien enregistré
        result.andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 OK
                .andExpect(jsonPath("$.message").value("User registered successfully!")); // Vérifie le message de succès

        // Vérification supplémentaire dans la base de données
        User savedUser = userRepository.findByEmail("sam@test.com").orElse(null);
        assertNotNull(savedUser, "L'utilisateur devrait être enregistré dans la base de données");
    }

    // Test pour vérifier l'échec de l'enregistrement avec une adresse e-mail déjà prise
    @Test
    public void testRegisterUser_EmailAlreadyTaken() throws Exception {
        // Préparation : création d'un utilisateur avec une adresse e-mail déjà existante
        User existingUser = new User("sam@test.com", "test", "sam", passwordEncoder.encode("sam!1234"), false);
        userRepository.save(existingUser); // Sauvegarde l'utilisateur dans la base de données

        // Action : envoi d'une requête POST avec une adresse e-mail déjà existante
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"sam@test.com\", \"password\": \"sam!1234\", \"firstName\": \"sam\", \"lastName\": \"test\" }"));

        // Assertion : vérification que l'enregistrement échoue
        result.andExpect(status().isBadRequest()) // Vérifie que le statut de la réponse est 400 Bad Request
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!")); // Vérifie le message d'erreur
    }
}