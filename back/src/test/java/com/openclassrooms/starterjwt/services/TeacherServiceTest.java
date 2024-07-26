package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@SpringBootTest
public class TeacherServiceTest 
{
    @Mock
    private TeacherRepository repositoryMock;

    private TeacherService service;

    @BeforeEach
    public void setup()
    {
        service = new TeacherService(repositoryMock);
    }

    @Nested
    class FindById
    {
        @Test
        public void givenCorrectId_whenFindById_thenReturnTeacher()
        {
            // GIVEN
            long id = 1;
            Teacher mockTeacher = new Teacher(id,"lastName","firstName",null,null);
            when(repositoryMock.findById(id)).thenReturn(Optional.of(mockTeacher));

            // WHEN
            Teacher foundTeacher = service.findById(id);

            // THEN
            verify(repositoryMock).findById(id);
            assertThat(foundTeacher)
                .isNotNull();
        }

        @Test
        public void givenWrongId_whenFindById_thenReturnNull()
        {
            // GIVEN
            long id = 1;
            when(repositoryMock.findById(id)).thenReturn(Optional.empty());

            // WHEN
            Teacher foundTeacher = service.findById(id);

            // THEN
            verify(repositoryMock).findById(id);
            assertThat(foundTeacher)
                .isNull();
        }
    }

    @Nested
    class FindAll
    {
        @Test
        public void givenTeacherList_whenFindAll_thenReturnTeachers()
        {
            // GIVEN
            Teacher mockTeacher = new Teacher(1L,"lastName","firstName",null,null);
            Teacher mockTeacher2 = new Teacher(2L,"lastName","firstName",null,null);
            Teacher mockTeacher3 = new Teacher(3L,"lastName","firstName",null,null);
            List<Teacher> list = new ArrayList<Teacher>();
            list.add(mockTeacher);
            list.add(mockTeacher2);
            list.add(mockTeacher3);
            when(repositoryMock.findAll()).thenReturn(list);

            // WHEN
            List<Teacher> foundTeachers = service.findAll();

            // THEN
            verify(repositoryMock).findAll();
            assertThat(foundTeachers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);
        }

        @Test
        public void givenEmptyList_whenFindAll_thenReturnEmptyList()
        {
            // GIVEN
            List<Teacher> list = new ArrayList<Teacher>();
            when(repositoryMock.findAll()).thenReturn(list);

            // WHEN
            List<Teacher> foundTeachers = service.findAll();

            // THEN
            verify(repositoryMock).findAll();
            assertThat(foundTeachers)
                .isNotNull()
                .isEmpty();
        }
    }
}
