package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        // Initialisation : Initialiser les mocks avant chaque test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteUser() {
        // Arrange : Préparer l'ID de l'utilisateur à supprimer
        Long userId = 1L;

        // Act : Appeler la méthode delete du service
        userService.delete(userId);

        // Assert : Vérifier que la méthode deleteById de userRepository a été appelée avec le bon ID
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testFindUserById_UserExists() {
        // Arrange : Préparer un utilisateur et configurer le mock pour retourner cet utilisateur
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act : Appeler la méthode findById du service
        User result = userService.findById(userId);

        // Assert : Vérifier que l'utilisateur retourné est le même que celui configuré
        assertNotNull(result, "L'utilisateur doit être trouvé et ne doit pas être null");
        assertEquals(userId, result.getId(), "L'ID de l'utilisateur retourné doit correspondre à l'ID recherché");
    }

    @Test
    public void testFindUserById_UserDoesNotExist() {
        // Arrange : Configurer le mock pour retourner un Optional vide
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act : Appeler la méthode findById du service
        User result = userService.findById(userId);

        // Assert : Vérifier que le résultat est null lorsque l'utilisateur n'existe pas
        assertNull(result, "Le résultat doit être null lorsque l'utilisateur n'existe pas");
    }
}
