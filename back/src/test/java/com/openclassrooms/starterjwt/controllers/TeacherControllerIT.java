package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class TeacherControllerIT 
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void givenCorrectId_whenFindById_thenReturnTeacher() throws Exception
    {
        // GIVEN
        Long id = 1L;

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/teacher/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString())
            .contains("\"firstName\":\"Margot\"")
            .contains("\"lastName\":\"DELAHAYE\"");
    }

    @Test
    @WithMockUser
    public void givenWrongId_whenFindById_thenThrowNotFoundException() throws Exception
    {
        // GIVEN
        Long id = Long.MAX_VALUE;

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/teacher/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

        // THEN
        assertThat(result.getResolvedException() instanceof NotFoundException);
    }

    @Test
    @WithMockUser
    public void givenLiteralId_whenFindById_thenThrowBadRequestException() throws Exception
    {
        // GIVEN
        String id = "r0m41n";

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/teacher/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

        // THEN
        assertThat(result.getResolvedException() instanceof BadRequestException);
    }

    @Test
    @WithMockUser
    public void givenTeachers_whenFindAll_thenReturnAllTeachers() throws Exception
    {
        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/teacher")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString())
            .contains("\"lastName\":\"DELAHAYE\"")
            .contains("\"lastName\":\"THIERCELIN\"");
    }
}
