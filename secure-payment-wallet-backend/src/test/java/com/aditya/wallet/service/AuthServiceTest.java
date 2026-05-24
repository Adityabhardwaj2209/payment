package com.aditya.wallet.service;

import com.aditya.wallet.dto.RegisterRequest;
import com.aditya.wallet.entity.User;
import com.aditya.wallet.entity.Wallet;
import com.aditya.wallet.repository.UserRepository;
import com.aditya.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService; // assume this service exists with register method

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_Success() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("test@example.com");
        req.setPassword("Password123");
        req.setRoles(Set.of("USER"));

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(req.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArgument(0));

        User created = authService.register(req);
        assertNotNull(created);
        assertEquals(req.getEmail(), created.getEmail());
        verify(walletRepository).save(any(Wallet.class));
    }
}
