package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.ArgumentCaptor;

class AuthEntryPointJwtTest {

    // Injection des dépendances mocks dans l'objet de test
    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    // Mock de la requête HTTP
    @Mock
    private HttpServletRequest request;

    // Mock de la réponse HTTP
    @Mock
    private HttpServletResponse response;

    // Mock de l'exception d'authentification
    @Mock
    private AuthenticationException authException;

    // Mock du flux de sortie de la servlet
    @Mock
    private ServletOutputStream outputStream;

    // Initialisation des mocks avant chaque test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test pour vérifier la réponse non autorisée lors de l'appel de commence()
    @Test
    void shouldReturnUnauthorizedResponse_WhenCommenceIsCalled() throws IOException, ServletException {
        // Préparation : configuration des comportements des mocks
        when(authException.getMessage()).thenReturn("Access Denied");
        when(request.getServletPath()).thenReturn("/api/secure-endpoint");
        when(response.getOutputStream()).thenReturn(outputStream);

        // Capture des données JSON écrites dans la réponse
        ArgumentCaptor<byte[]> responseCaptor = ArgumentCaptor.forClass(byte[].class);

        // Action : appel de la méthode commence() pour simuler la gestion des exceptions
        authEntryPointJwt.commence(request, response, authException);

        // Assertions : vérification des interactions avec la réponse
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Capture et vérification du corps de la réponse
        verify(outputStream).write(responseCaptor.capture(), eq(0), anyInt());

        byte[] capturedResponseBytes = responseCaptor.getValue();
        String capturedResponseBody = new String(capturedResponseBytes);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> actualResponseBody = objectMapper.readValue(capturedResponseBody, Map.class);

        // Réponse attendue
        Map<String, Object> expectedResponseBody = Map.of(
                "status", HttpServletResponse.SC_UNAUTHORIZED,
                "error", "Unauthorized",
                "message", "Access Denied",
                "path", "/api/secure-endpoint");

        // Assertion : comparaison du corps de la réponse réelle avec la réponse attendue
        assertEquals(expectedResponseBody, actualResponseBody,
                "Le corps de la réponse ne correspond pas à la réponse attendue");
    }
}
