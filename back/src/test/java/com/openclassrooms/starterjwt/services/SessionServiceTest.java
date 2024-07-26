package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class SessionServiceTest 
{
    @Mock
    private SessionRepository sessionRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    private SessionService service;

    @BeforeEach
    public void setup()
    {
        service = new SessionService(sessionRepositoryMock, userRepositoryMock);
    }

    @Nested
    class Create
    {
        @Test
        public void givenSession_whenCreate_thenReturnSession()
        {
            // GIVEN
            Session mockSession = new Session(1L, "session", null, "desc", null, new ArrayList<User>(), null, null);
            when(sessionRepositoryMock.save(mockSession)).thenReturn(mockSession);

            // WHEN
            Session sessionCreated = service.create(mockSession);

            // THEN
            verify(sessionRepositoryMock, times(1)).save(mockSession);
            assertThat(sessionCreated)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", mockSession.getName());
        }

        @Test
        public void givenNullSession_whenCreate_thenThrowIllegalArgumentException()
        {
            // GIVEN
            when(sessionRepositoryMock.save(null)).thenThrow(IllegalArgumentException.class);

            // WHEN 
            Executable exe = () -> service.create(null);
            
            // THEN
            assertThrows(IllegalArgumentException.class, exe);
            verify(sessionRepositoryMock).save(null);
        }
    }

    @Nested
    class Delete
    {
        @Test
        public void givenCorrectId_whenDeleteById_thenSessionDeleted()
        {
            // GIVEN
            long sessionId = 1L;
            Session mockSession = new Session(sessionId, "session", null, "desc", null, new ArrayList<User>(), null, null);
            when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.of(mockSession)).thenReturn(Optional.empty());

            
            // WHEN
            Session foundSession = service.getById(sessionId);
            service.delete(sessionId);
            Session foundSession2 = service.getById(sessionId);

            // THEN
            verify(sessionRepositoryMock).deleteById(sessionId);
            verify(sessionRepositoryMock, times(2)).findById(sessionId);
            assertThat(foundSession)
                .isNotNull();
            assertThat(foundSession2)
                .isNull();
        }

        @Test
        public void givenWrongId_whenDeleteById_thenNoExceptionIsThrown()
        {
            // GIVEN
            long id = 1;
            doNothing().when(sessionRepositoryMock).deleteById(id);

            // WHEN 
            Executable executable = () -> service.delete(id);

            // THEN
            assertDoesNotThrow(executable);
            verify(sessionRepositoryMock).deleteById(id);
        }

        @Test
        public void givenNullId_whenDeleteById_thenThrowIllegalArgumentException()
        {
            // GIVEN
            doThrow(IllegalArgumentException.class).when(sessionRepositoryMock).deleteById(null);

            // WHEN 
            Executable exe = () -> service.delete(null);
            
            // THEN
            // Should throw an Exception and does not, I don't really know why
            assertThrows(IllegalArgumentException.class, exe);
            verify(sessionRepositoryMock).deleteById(null);
        }
    }

    @Nested
    class FindAll
    {
        @Test
        public void givenSessionList_whenFindAll_thenReturnSessions()
        {
            // GIVEN
            Session mockSession = new Session(1L, "session", null, "desc", null, new ArrayList<User>(), null, null);
            Session mockSession2 = new Session(2L, "session", null, "desc", null, new ArrayList<User>(), null, null);
            List<Session> list = new ArrayList<Session>();
            list.add(mockSession);
            list.add(mockSession2);
            when(sessionRepositoryMock.findAll()).thenReturn(list);

            // WHEN
            List<Session> foundSessions = service.findAll();

            // THEN
            verify(sessionRepositoryMock).findAll();
            assertThat(foundSessions)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
        }

        @Test
        public void givenEmptyList_whenFindAll_thenReturnEmptyList()
        {
            // GIVEN
            List<Session> list = new ArrayList<Session>();
            when(sessionRepositoryMock.findAll()).thenReturn(list);

            // WHEN
            List<Session> foundSessions = service.findAll();

            // THEN
            verify(sessionRepositoryMock).findAll();
            assertThat(foundSessions)
                .isNotNull()
                .isEmpty();
        }
    }

    @Nested
    class GetById
    {
        @Test
        public void givenCorrectId_whenGetById_thenReturnSession()
        {
            // GIVEN
            Long sessionId = 1L;
            Session mockSession = new Session(sessionId, "session", null, "desc", null, new ArrayList<User>(), null, null);
            when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.of(mockSession));

            // WHEN
            Session foundSession = service.getById(sessionId);

            // THEN
            verify(sessionRepositoryMock).findById(sessionId);
            assertThat(foundSession)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", sessionId);
        }

        @Test
        public void givenWrongId_whenFindById_thenReturnNull()
        {
            // GIVEN
            long id = 1;
            when(sessionRepositoryMock.findById(id)).thenReturn(Optional.empty());

            // WHEN
            Session foundSession = service.getById(id);

            // THEN
            verify(sessionRepositoryMock).findById(id);
            assertThat(foundSession)
                .isNull();
        }
    }

    @Nested
    class Update 
    {
        @Test
        public void givenSession_whenUpdate_thenReturnUpdatedSession()
        {
            // GIVEN
            Long newId = 3L;
            Session mockSession = new Session(1L, "session", null, "desc", null, new ArrayList<User>(), null, null);
            when(sessionRepositoryMock.save(mockSession)).thenReturn(mockSession);

            // WHEN
            Session sessionCreated = service.update(newId, mockSession);

            // THEN
            verify(sessionRepositoryMock, times(1)).save(mockSession);
            assertThat(sessionCreated)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", newId);
        }

        @Test
        public void givenNullSession_whenUpdate_thenThrowNullPointerException()
        {
            // GIVEN
            when(sessionRepositoryMock.save(null)).thenThrow(IllegalArgumentException.class);

            // WHEN 
            Executable exe = () -> service.update(1l, null);
            
            // THEN
            assertThrows(NullPointerException.class, exe);
            verify(sessionRepositoryMock, never()).save(null);
        }
    }

    @Nested
    class Participate
    {
        @Test
        public void givenUserId_whenParticipate_thenAddToSessionUsers()
        {
            // GIVEN
            Long userId = 1L;
            Long sessionId = 1L;
            User mockUser = new User(userId, "a@b.fr","lastName","firstName","a",false,null,null);
            Session mockSession = new Session(sessionId, "session", null, "desc", null, new ArrayList<User>(), null, null);
            when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(mockUser));
            when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.of(mockSession));

            // WHEN
            service.participate(sessionId, userId);

            // THEN
            verify(sessionRepositoryMock).save(mockSession);
            assertThat(mockSession.getUsers())
                .contains(mockUser);
        }

        @Test
        public void givenWrongSessionId_whenParticipate_thenThrowNotFoundException()
        {
            // GIVEN
            Long userId = 1L;
            Long sessionId = 1L;
            when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.empty());

            // WHEN
            Executable exe = () -> service.participate(sessionId, userId);

            // THEN
            assertThrows(NotFoundException.class, exe);
            verify(sessionRepositoryMock).findById(sessionId);
        }

        @Test
        public void givenWrongUserId_whenParticipate_thenThrowNotFoundException()
        {
            // GIVEN
            Long userId = 1L;
            Long sessionId = 1L;
            when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

            // WHEN
            Executable exe = () -> service.participate(sessionId, userId);

            // THEN
            assertThrows(NotFoundException.class, exe);
            verify(userRepositoryMock).findById(userId);
        }
        
        @Test
        public void givenAlreadyParticipatingUserId_whenParticipate_thenThrowBadRequestException()
        {
            // GIVEN
            Long userId = 1L;
            Long sessionId = 1L;
            User mockUser = new User(userId, "a@b.fr","lastName","firstName","a",false,null,null);
            Session mockSession = new Session(sessionId, "session", null, "desc", null, Arrays.asList(mockUser), null, null);
            when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.of(mockSession));
            when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(mockUser));

            // WHEN
            Executable exe = () -> service.participate(mockSession.getId(), userId);

            // THEN
            assertThrows(BadRequestException.class, exe);
        }
    }

    @Nested
    class NoLongerParticipate
    {
        @Test
        public void givenCorrectSessionIdAndParticipatingUserId_whenNoLongerParticipate_thenRemoveUserFromSession()
        {
            // GIVEN
            Long userId = 1L;
            Long sessionId = 1L;
            User mockUser = new User(userId, "a@b.fr","lastName","firstName","a",false,null,null);
            Session mockSession = new Session(sessionId, "session", null, "desc", null, Arrays.asList(mockUser), null, null);
            when(sessionRepositoryMock.findById(mockSession.getId())).thenReturn(Optional.of(mockSession));
            when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(mockUser));

            // WHEN
            service.noLongerParticipate(mockSession.getId(), userId);

            // THEN
            verify(sessionRepositoryMock).save(mockSession);
            assertThat(mockSession.getUsers())
                .doesNotContain(mockUser);
        }

        @Test
        public void givenWrongSessionId_whenNoLongerParticipate_thenThrowNotFoundException()
        {
            // GIVEN
            Long userId = 1L;
            Long sessionId = 1L;
            when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.empty());

            // WHEN
            Executable exe = () -> service.noLongerParticipate(sessionId, userId);

            // THEN
            assertThrows(NotFoundException.class, exe);
            verify(sessionRepositoryMock).findById(sessionId);
        }

        @Test
        public void givenNotParticipatingUserId_whenNoLongerParticipate_thenThrowBadRequestException()
        {
            // GIVEN
            Long userId = 1L;
            Long sessionId = 1L;
            Session mockSession = new Session(sessionId, "session", null, "desc", null, new ArrayList<User>(), null, null);
            when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.of(mockSession));

            // WHEN
            Executable exe = () -> service.noLongerParticipate(mockSession.getId(), userId);

            // THEN
            assertThrows(BadRequestException.class, exe);
        }
    }
}
