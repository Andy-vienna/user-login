package com.example.demo.controller;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminDashboardLoads() throws Exception {
        Mockito.when(userRepository.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/admin"))
               .andExpect(status().isOk())
               .andExpect(view().name("home"));
    }
}
