package com.devsuperior.dslearnbds.services;

import com.devsuperior.dslearnbds.dto.UserDTO;
import com.devsuperior.dslearnbds.entities.User;
import com.devsuperior.dslearnbds.repositories.UserRepository;
import com.devsuperior.dslearnbds.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    private long existingId;
    private long nonExistingId;
    private String existingUsername;
    private String nonExistingUsername;
    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 100L;
        existingUsername = "jvolima2004@gmail.com";
        nonExistingUsername = "jvdoparaguai@gmail.com";
        user = new User();
        user.setName("João Vitor");
        user.setEmail("jvolima2004@gmail.com");
        user.setPassword("123456");

        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(userRepository.findByEmail(existingUsername)).thenReturn(user);
        Mockito.when(userRepository.findByEmail(nonExistingUsername)).thenReturn(null);

        Mockito.doNothing().when(authService).validateSelfOrAdmin(existingId);
    }

    @Test
    public void findByIdShouldReturnUserDTOWhenIdExists() {
        UserDTO dto = userService.findById(existingId);

        Assertions.assertEquals(user.getEmail(), dto.getEmail());
        Mockito.verify(userRepository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(nonExistingId);
        });
        Mockito.verify(userRepository).findById(nonExistingId);
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUsernameExists() {
        UserDetails userDetails = userService.loadUserByUsername(existingUsername);

        Assertions.assertEquals(user.getEmail(), userDetails.getUsername());
        Mockito.verify(userRepository).findByEmail(existingUsername);
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUsernameDoesNotExist() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(nonExistingUsername);
        });
        Mockito.verify(userRepository).findByEmail(nonExistingUsername);
    }
}
