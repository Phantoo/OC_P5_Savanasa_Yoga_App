package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
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
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class UserControllerIT 
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void givenCorrectId_whenFindById_thenReturnUser() throws Exception
    {
        // GIVEN
        Long id = 1L;

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/user/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString())
            .contains("\"email\":\"yogaTEST2@studio.com\"")
            .contains("\"lastName\":\"TestAdmin\"");
    }

    @Test
    @WithMockUser
    public void givenWrongId_whenFindById_thenThrowNotFoundException() throws Exception
    {
        // GIVEN
        Long id = Long.MAX_VALUE;

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/user/{id}", id)
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
            MockMvcRequestBuilders.get("/api/user/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

        // THEN
        assertThat(result.getResolvedException() instanceof BadRequestException);
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "yogaTEST2@studio.com")
    public void givenCorrectId_whenSave_thenReturn200() throws Exception
    {
        // GIVEN
        Long id = 1L;

        // WHEN THEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/user/{id}", id)
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

        // WHEN THEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/user/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

        // THEN
        assertThat(result.getResolvedException() instanceof NotFoundException);
    }

    @Test
    @WithMockUser
    public void givenCorrectIdButNotCurrentlyLoggedIn_whenSave_thenThrowUnauthorizedException() throws Exception
    {
        // GIVEN
        Long id = 1L;

        // WHEN THEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/user/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
            .andReturn();

        // THEN
        assertThat(result.getResolvedException() instanceof Unauthorized);
    }

    @Test
    @WithMockUser
    public void givenLiteralId_whenSave_thenThrowBadRequestException() throws Exception
    {
        // GIVEN
        String id = "r0m41n";

        // WHEN THEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/user/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

        // THEN
        assertThat(result.getResolvedException() instanceof BadRequestException);
    }
}
