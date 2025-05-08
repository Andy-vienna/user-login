
package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("andy");
        testUser.setVerificationToken("abc123");
        testUser.setTokenExpiryDate(LocalDateTime.now().plusHours(1));
        testUser.setEnabled(false);
    }

    @Test
    void testVerifyEmail_validToken_shouldEnableUser() {
        when(userRepository.findByVerificationToken("abc123"))
                .thenReturn(Optional.of(testUser));

        String result = userService.verifyEmail("abc123");

        assertEquals("E-Mail erfolgreich bestätigt!<br>Du kannst dich nun anmelden...", result);
        assertTrue(testUser.isEnabled());
        verify(userRepository).save(testUser);
    }

    @Test
    void testVerifyEmail_invalidToken_shouldReturnError() {
        when(userRepository.findByVerificationToken("invalid"))
                .thenReturn(Optional.empty());

        String result = userService.verifyEmail("invalid");

        assertEquals("Ungültiger oder abgelaufener Bestätigungslink.", result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testVerifyEmail_expiredToken_shouldReturnExpiredMessage() {
        testUser.setTokenExpiryDate(LocalDateTime.now().minusMinutes(1));
        when(userRepository.findByVerificationToken("abc123"))
                .thenReturn(Optional.of(testUser));

        String result = userService.verifyEmail("abc123");

        assertEquals("Der Verifizierungslink ist abgelaufen.<br>Bitte registriere dich erneut.", result);
        assertFalse(testUser.isEnabled());
        verify(userRepository, never()).save(any());
    }
}
