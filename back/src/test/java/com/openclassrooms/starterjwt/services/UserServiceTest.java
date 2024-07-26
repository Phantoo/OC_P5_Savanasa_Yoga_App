package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class UserServiceTest 
{
    @Mock
    private UserRepository repositoryMock;

    private UserService service;

    @BeforeEach
    public void setup()
    {
        service = new UserService(repositoryMock);
    }

    @Nested
    class FindById
    {
        @Test
        public void givenCorrectId_whenFindById_thenReturnUser()
        {
            // GIVEN
            long id = 1;
            User mockUser = new User(id, "a@b.fr","lastName","firstName","a",false,null,null);
            when(repositoryMock.findById(id)).thenReturn(Optional.of(mockUser));

            // WHEN
            User foundUser = service.findById(id);

            // THEN
            verify(repositoryMock).findById(id);
            assertThat(foundUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
        }

        @Test
        public void givenWrongId_whenFindById_thenReturnNull()
        {
            // GIVEN
            long id = 1;
            when(repositoryMock.findById(id)).thenReturn(Optional.empty());

            // WHEN
            User foundUser = service.findById(id);

            // THEN
            verify(repositoryMock).findById(id);
            assertThat(foundUser)
                .isNull();
        }
    }

    @Nested
    class Delete
    {
        @Test
        public void givenCorrectId_whenDeleteById_thenUserDeleted()
        {
            // GIVEN
            long id = 1;
            User mockUser = new User(id, "a@b.fr","lastName","firstName","a",false,null,null);
            when(repositoryMock.findById(id)).thenReturn(Optional.of(mockUser)).thenReturn(Optional.empty());
            
            // WHEN
            User foundUser = service.findById(id);
            service.delete(id);
            User foundUser2 = service.findById(id);

            // THEN
            verify(repositoryMock).deleteById(id);
            verify(repositoryMock, times(2)).findById(id);
            assertThat(foundUser)
                .isNotNull();
            assertThat(foundUser2)
                .isNull();
        }

        @Test
        public void givenWrongId_whenDeleteById_thenNoExceptionIsThrown()
        {
            // GIVEN
            long id = 1;
            doNothing().when(repositoryMock).deleteById(id);

            // WHEN 
            Executable executable = () -> service.delete(id);

            // THEN
            assertDoesNotThrow(executable);
            verify(repositoryMock).deleteById(id);
        }

        @Test
        public void givenNullId_whenDeleteById_thenThrowIllegalArgumentException()
        {
            // GIVEN
            doThrow(IllegalArgumentException.class).when(repositoryMock).deleteById(null);

            // WHEN 
            Executable exe = () -> service.delete(null);
            
            // THEN
            // Should throw an Exception and does not, I don't really know why
            assertThrows(IllegalArgumentException.class, exe);
            verify(repositoryMock).deleteById(null);
        }
    }
}
