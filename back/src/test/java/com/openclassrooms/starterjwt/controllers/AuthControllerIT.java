package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class AuthControllerIT 
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenCorrectCredentials_whenAuthenticateUser_thenReturnJwtResponse() throws Exception
    {
        // GIVEN
        String email = "yogaTEST2@studio.com";
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword("test!1234");

        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(request);

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
            .content(jsonRequest)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString())
            .contains("\"token\":\"")
            .contains("\"username\":\"" + email + "\"");
    }

    @Test
    public void givenWrongCredentials_whenAuthenticateUser_thenThrowUnauthorizedException() throws Exception
    {
        // GIVEN
        String email = "wrong@studio.com";
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword("test!1234");

        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(request);

        // WHEN THEN
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
            .content(jsonRequest)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
            .andReturn();
    }

    @Test
    public void givenCorrectRequest_whenRegister_thenReturnOk() throws Exception
    {
        // GIVEN
        SignupRequest request = new SignupRequest();
        request.setEmail("yogaTEST3@studio.com");
        request.setFirstName("fred");
        request.setLastName("testo");
        request.setPassword("test!1234");

        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(request);

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/register")
            .content(jsonRequest)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString())
            .contains("User registered");
    }

    @Test
    public void givenExistingEmail_whenRegister_thenThrowBadRequestException() throws Exception
    {
        // GIVEN
        String email = "yogaTEST2@studio.com";
        SignupRequest request = new SignupRequest();
        request.setEmail(email);
        request.setPassword("test!1234");
        request.setFirstName("Testo");
        request.setLastName("Sterone");

        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(request);

        // WHEN
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/register")
            .content(jsonRequest)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

        // THEN
        assertThat(result.getResolvedException() instanceof BadRequestException);
    }
}
