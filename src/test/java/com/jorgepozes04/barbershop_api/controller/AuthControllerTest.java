package com.jorgepozes04.barbershop_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorgepozes04.barbershop_api.dto.ClientSignupDTO;
import com.jorgepozes04.barbershop_api.dto.LoginDTO;
import com.jorgepozes04.barbershop_api.service.AuthService;
import com.jorgepozes04.barbershop_api.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientSignupDTO signupDTO;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        signupDTO = new ClientSignupDTO();
        signupDTO.setCpf("12345678901");
        signupDTO.setName("John Doe");
        signupDTO.setPhoneNumber("11999999999");
        signupDTO.setPassword("password123");

        loginDTO = new LoginDTO();
        loginDTO.setUsername("12345678901");
        loginDTO.setPassword("password123");
    }

    @Test
    @DisplayName("Should register client successfully")
    void testRegisterClientSuccess() throws Exception {
        // Arrange
        doNothing().when(authService).registerClient(any(), any(), any(), any());

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return 400 when registering with invalid CPF")
    void testRegisterClientWithInvalidCpf() throws Exception {
        // Arrange
        signupDTO.setCpf("");

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when registering with invalid password")
    void testRegisterClientWithInvalidPassword() throws Exception {
        // Arrange
        signupDTO.setPassword("short");

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should login user successfully")
    void testLoginSuccess() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk());
    }
}
