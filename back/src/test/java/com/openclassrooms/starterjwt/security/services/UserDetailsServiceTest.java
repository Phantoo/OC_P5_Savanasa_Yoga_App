package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class UserDetailsServiceTest 
{
    @Mock
    private UserRepository repositoryMock;

    private UserDetailsServiceImpl service;

    @BeforeEach
    public void setup()
    {
        service = new UserDetailsServiceImpl(repositoryMock);
    }

    @Nested
    class LoadUserByUsername
    {
        @Test
        public void givenCorrectEmail_whenFindByUsername_thenReturnUser()
        {
            // GIVEN
            String email = "a@b.fr";
            User mockUser = new User(1L, email,"lastName","firstName","a",false,null,null);
            when(repositoryMock.findByEmail(email)).thenReturn(Optional.of(mockUser));

            // WHEN
            UserDetails foundUserDetails = service.loadUserByUsername(email);

            // THEN
            verify(repositoryMock, times(1)).findByEmail(email);
            assertThat(foundUserDetails)
                .isNotNull()
                .hasFieldOrPropertyWithValue("username", email);
        }

        @Test
        public void givenWrongEmail_whenFindByUsername_thenThrowsUsernameNotFoundException()
        {
            // GIVEN
            String email = "a@b.fr";
            when(repositoryMock.findByEmail(email)).thenReturn(Optional.empty());

            // WHEN
            Executable executable = () -> service.loadUserByUsername(email);

            // THEN
            assertThrows(UsernameNotFoundException.class, executable);
            verify(repositoryMock, times(1)).findByEmail(email);
        }
    }
}
