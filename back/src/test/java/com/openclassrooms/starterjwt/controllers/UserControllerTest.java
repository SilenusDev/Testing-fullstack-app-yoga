package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        // Initialise les mocks avant chaque test
        MockitoAnnotations.openMocks(this);

        // Configure le contexte de sécurité
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testFindById() {
        // Préparation des données de test
        Long userId = 1L;
        User user = new User();
        UserDto userDto = new UserDto();

        // Configuration des comportements des mocks
        when(userService.findById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Appel de la méthode à tester
        ResponseEntity<?> response = userController.findById(userId.toString());

        // Vérification des résultats
        assertEquals(ResponseEntity.ok(userDto), response);
        verify(userService, times(1)).findById(userId);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    public void testDelete() {
        // Préparation des données de test
        Long userId = 1L;
        User user = new User();
        user.setEmail("test@example.com");

        // Configuration des comportements des mocks
        when(userService.findById(userId)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        // Appel de la méthode à tester
        ResponseEntity<?> response = userController.save(userId.toString());

        // Vérification des résultats
        assertEquals(ResponseEntity.ok().build(), response);
        verify(userService, times(1)).findById(userId);
        verify(userService, times(1)).delete(userId);
    }

    @Test
    public void testDeleteUnauthorized() {
        // Préparation des données de test
        Long userId = 1L;
        User user = new User();
        user.setEmail("test@example.com");

        // Configuration des comportements des mocks
        when(userService.findById(userId)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("unauthorized@example.com");

        // Appel de la méthode à tester
        ResponseEntity<?> response = userController.save(userId.toString());

        // Vérification des résultats
        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(), response);
        verify(userService, times(1)).findById(userId);
        verify(userService, never()).delete(userId);
    }
}
