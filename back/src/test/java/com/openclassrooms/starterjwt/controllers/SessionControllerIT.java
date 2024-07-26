package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class SessionControllerIT 
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void givenCorrectId_whenFindById_thenReturnSession() throws Exception
    {
        // GIVEN
        Long id = 1L;

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/session/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString())
            .contains("\"name\":\"Initiation au Yoga\"")
            .contains("\"description\":\"Description\"");
    }

    @Test
    @WithMockUser
    public void givenWrongId_whenFindById_thenThrowNotFoundException() throws Exception
    {
        // GIVEN
        Long id = Long.MAX_VALUE;

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/session/{id}", id)
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
            MockMvcRequestBuilders.get("/api/session/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

        // THEN
        assertThat(result.getResolvedException() instanceof BadRequestException);
    }

    @Test
    @WithMockUser
    public void givenSessions_whenFindAll_thenReturnAllSessions() throws Exception
    {
        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/session")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString())
        .contains("\"name\":\"Initiation au Yoga\"")
        .contains("\"description\":\"Description\"")
        .contains("\"name\":\"Cours novice de Yoga\"")
        .contains("\"description\":\"Description2\"");
    }

    @Test
    @WithMockUser
    public void givenCorrectIds_whenParticipate_thenReturnOk() throws Exception
    {
        // GIVEN 
        Long sessionId = 1L;
        Long userId = 1L;

        // WHEN THEN
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/session/{sessionId}/participate/{userId}", sessionId, userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();        
    }

    @Test
    @WithMockUser
    public void givenWrongIds_whenParticipate_thenThrowNotFoundException() throws Exception
    {
        // GIVEN
        Long sessionId = Long.MAX_VALUE;
        Long userId = Long.MAX_VALUE;

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/session/{sessionId}/participate/{userId}", sessionId, userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

            // THEN
            assertThat(result.getResolvedException() instanceof NotFoundException);
    }

    @Test
    @WithMockUser
    public void givenLiteralIds_whenParticipate_thenThrowbadRequestException() throws Exception
    {
        // GIVEN
        String sessionId = "r0m41n";
        Long userId = Long.MAX_VALUE;

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/session/{sessionId}/participate/{userId}", sessionId, userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

            // THEN
            assertThat(result.getResolvedException() instanceof BadRequestException);
    }

    @Test
    @WithMockUser
    public void givenCorrectIds_whenNoLongerParticipate_thenReturnOk() throws Exception
    {
        // GIVEN 
        Long sessionId = 2L;
        Long userId = 1L;

        // WHEN THEN
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/session/{sessionId}/participate/{userId}", sessionId, userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();        
    }

    @Test
    @WithMockUser
    public void givenWrongIds_whenNoLongerParticipate_thenThrowNotFoundException() throws Exception
    {
        // GIVEN
        Long sessionId = Long.MAX_VALUE;
        Long userId = Long.MAX_VALUE;

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/session/{sessionId}/participate/{userId}", sessionId, userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

            // THEN
            assertThat(result.getResolvedException() instanceof NotFoundException);
    }

    @Test
    @WithMockUser
    public void givenLiteralIds_whenNoLongerParticipate_thenThrowbadRequestException() throws Exception
    {
        // GIVEN
        String sessionId = "r0m41n";
        Long userId = Long.MAX_VALUE;

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/session/{sessionId}/participate/{userId}", sessionId, userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

            // THEN
            assertThat(result.getResolvedException() instanceof BadRequestException);
    }

    @Test
    @DirtiesContext
    @WithMockUser
    public void givenSession_whenCreate_thenReturnCreatedSession() throws Exception
    {
        // GIVEN
        SessionDto session = new SessionDto();
        session.setName("New Session");
        session.setDescription("Description");
        session.setDate(Date.from(Instant.now()));
        session.setTeacher_id(1L);

        ObjectMapper mapper = new ObjectMapper();
        String jsonSession = mapper.writeValueAsString(session);

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/session")
            .content(jsonSession)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString())
        .contains("\"name\":\"" + session.getName() + "\"")
        .contains("\"description\":\"" + session.getDescription() + "\"");
    }

    @Test
    @DirtiesContext
    @WithMockUser
    public void givenSession_whenUpdate_thenReturnUpdatedSession() throws Exception
    {
        // GIVEN
        SessionDto session = new SessionDto();
        session.setId(1L);
        session.setName("Updated Session");
        session.setDescription("Updated Description");
        session.setDate(Date.from(Instant.now()));
        session.setTeacher_id(1L);

        ObjectMapper mapper = new ObjectMapper();
        String jsonSession = mapper.writeValueAsString(session);

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/session/{id}", session.getId())
            .content(jsonSession)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString())
        .contains("\"name\":\"" + session.getName() + "\"")
        .contains("\"description\":\"" + session.getDescription() + "\"");
    }

    @Test
    @WithMockUser
    public void givenNullSession_whenUpdate_thenThrowBadRequestException() throws Exception
    {
    // GIVEN No Session

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/session/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

        // THEN
        assertThat(result.getResolvedException() instanceof BadRequestException);
    }

    @Test
    @WithMockUser
    public void givenSessionAndLiteralId_whenUpdate_thenReturnBadRequestException() throws Exception
    {
        // GIVEN 
        String id = "r0m41n";
        SessionDto session = new SessionDto();
        session.setId(1L);
        session.setName("Updated Session");
        session.setDescription("Updated Description");
        session.setDate(Date.from(Instant.now()));
        session.setTeacher_id(1L);

        ObjectMapper mapper = new ObjectMapper();
        String jsonSession = mapper.writeValueAsString(session);

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/session/{id}", id)
            .content(jsonSession)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

        // THEN
        assertThat(result.getResolvedException() instanceof BadRequestException);
    }

    @Test
    @DirtiesContext
    @WithMockUser
    public void givenCorrectId_whenSave_thenReturnOk() throws Exception
    {
        // GIVEN 
        Long id = 1L;

        // WHEN THEN
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/session/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();        
    }

    @Test
    @WithMockUser
    public void givenWrongId_whenSave_thenThrowNotFoundException() throws Exception
    {
        // GIVEN
        Long id = Long.MAX_VALUE;

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/session/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

            // THEN
            assertThat(result.getResolvedException() instanceof NotFoundException);
    }

    @Test
    @WithMockUser
    public void givenLiteralId_whenSave_thenThrowBadRequestException() throws Exception
    {
        // GIVEN
        String id = "r0m41n";

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/session/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

            // THEN
            assertThat(result.getResolvedException() instanceof BadRequestException);
    }
}
