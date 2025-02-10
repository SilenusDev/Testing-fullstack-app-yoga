
package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    // Instance du mapper d'utilisateurs
    private UserMapper userMapper;

    // Initialisation avant chaque test
    @BeforeEach
    void initMapper() {
        // Initialisation de l'instance de UserMapper générée par MapStruct
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    // Test pour vérifier la conversion d'un UserDto en entité User
    @Test
    void shouldMapUserDtoToUser() {
        // Préparation : création d'un UserDto avec des données personnalisées
        UserDto userDto = new UserDto();
        userDto.setId(10L);
        userDto.setEmail("alex.jones@sample.com");
        userDto.setFirstName("Alex");
        userDto.setLastName("Jones");
        userDto.setPassword("MyStrongPass@2024");
        userDto.setAdmin(true);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        // Action : conversion de UserDto en User
        User user = userMapper.toEntity(userDto);

        // Assertions : vérification que la conversion est correcte
        assertNotNull(user, "L'utilisateur converti ne doit pas être null");
        assertEquals(userDto.getId(), user.getId(), "Les ID doivent correspondre");
        assertEquals(userDto.getEmail(), user.getEmail(), "Les emails doivent correspondre");
        assertEquals(userDto.getFirstName(), user.getFirstName(), "Les prénoms doivent correspondre");
        assertEquals(userDto.getLastName(), user.getLastName(), "Les noms de famille doivent correspondre");
        assertEquals(userDto.getPassword(), user.getPassword(), "Les mots de passe doivent correspondre");
        assertEquals(userDto.isAdmin(), user.isAdmin(), "Le statut administrateur doit correspondre");
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt(), "Les dates de création doivent correspondre");
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt(), "Les dates de mise à jour doivent correspondre");
    }

    // Test pour vérifier la conversion d'une entité User en UserDto
    @Test
    void shouldMapUserToUserDto() {
        // Préparation : création d'un User avec des données personnalisées
        User user = User.builder()
                .id(20L)
                .email("jordan.lee@mockdata.com")
                .firstName("Jordan")
                .lastName("Lee")
                .password("SecurePass2024!")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Action : conversion de User en UserDto
        UserDto userDto = userMapper.toDto(user);

        // Assertions : vérification que la conversion est correcte
        assertNotNull(userDto, "Le UserDto converti ne doit pas être null");
        assertEquals(user.getId(), userDto.getId(), "Les ID doivent correspondre");
        assertEquals(user.getEmail(), userDto.getEmail(), "Les emails doivent correspondre");
        assertEquals(user.getFirstName(), userDto.getFirstName(), "Les prénoms doivent correspondre");
        assertEquals(user.getLastName(), userDto.getLastName(), "Les noms de famille doivent correspondre");
        assertEquals(user.getPassword(), userDto.getPassword(), "Les mots de passe doivent correspondre");
        assertEquals(user.isAdmin(), userDto.isAdmin(), "Le statut administrateur doit correspondre");
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt(), "Les dates de création doivent correspondre");
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt(), "Les dates de mise à jour doivent correspondre");
    }

    // Test pour vérifier la conversion d'une liste de UserDto en liste d'entités User
    @Test
    void shouldMapUserDtoListToUserList() {
        // Préparation : création d'une liste d'objets UserDto
        UserDto userDto1 = new UserDto();
        userDto1.setId(30L);
        userDto1.setEmail("chris.parker@demo.com");
        userDto1.setFirstName("Chris");
        userDto1.setLastName("Parker");
        userDto1.setPassword("Chris123!");

        UserDto userDto2 = new UserDto();
        userDto2.setId(31L);
        userDto2.setEmail("morgan.smith@demo.com");
        userDto2.setFirstName("Morgan");
        userDto2.setLastName("Smith");
        userDto2.setPassword("Morgan@456");

        List<UserDto> userDtoList = Arrays.asList(userDto1, userDto2);

        // Action : conversion de la liste de UserDto en liste de User
        List<User> userList = userMapper.toEntity(userDtoList);

        // Assertions : vérification que la conversion est correcte
        assertNotNull(userList, "La liste d'utilisateurs convertie ne doit pas être nulle");
        assertEquals(2, userList.size(), "La taille de la liste d'utilisateurs doit être 2");
        assertEquals(userDto1.getId(), userList.get(0).getId(), "L'ID du premier utilisateur doit correspondre");
        assertEquals(userDto2.getId(), userList.get(1).getId(), "L'ID du deuxième utilisateur doit correspondre");
    }

    // Test pour vérifier la conversion d'une liste d'entités User en liste de UserDto
    @Test
    void shouldMapUserListToUserDtoList() {
        // Préparation : création d'une liste d'objets User
        User user1 = User.builder()
                .id(40L)
                .email("alex.brown@testmail.com")
                .firstName("Alex")
                .lastName("Brown")
                .password("Alex@123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .id(41L)
                .email("jamie.green@testmail.com")
                .firstName("Jamie")
                .lastName("Green")
                .password("Jamie@789")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<User> userList = Arrays.asList(user1, user2);

        // Action : conversion de la liste de User en liste de UserDto
        List<UserDto> userDtoList = userMapper.toDto(userList);

        // Assertions : vérification que la conversion est correcte
        assertNotNull(userDtoList, "La liste de UserDto convertie ne doit pas être nulle");
        assertEquals(2, userDtoList.size(), "La taille de la liste de UserDto doit être 2");
        assertEquals(user1.getId(), userDtoList.get(0).getId(), "L'ID du premier UserDto doit correspondre");
        assertEquals(user2.getId(), userDtoList.get(1).getId(), "L'ID du deuxième UserDto doit correspondre");
    }
}
