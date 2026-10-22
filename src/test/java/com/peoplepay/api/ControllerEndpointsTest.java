package com.peoplepay.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Smoke/integration tests for the public controller routes and their security rules. */
@SpringBootTest
@AutoConfigureMockMvc
class ControllerEndpointsTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void loginEndpointAcceptsValidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"hr\",\"password\":\"hr123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("hr"))
            .andExpect(jsonPath("$.role").value("HR"));
    }

    @Test
    void protectedControllerRoutesAreReachable() throws Exception {
        String hr = basic("hr", "hr123");
        String admin = basic("admin", "admin123");

        mockMvc.perform(get("/api/employees").header("Authorization", hr)).andExpect(status().isOk());
        mockMvc.perform(get("/api/employees/EMP-00001").header("Authorization", hr)).andExpect(status().isOk());
        mockMvc.perform(post("/api/employees")
                .header("Authorization", hr).contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson("TEST-CTRL-1", "ctrl1@example.com")))
            .andExpect(status().isCreated());
        mockMvc.perform(put("/api/employees/TEST-CTRL-1")
                .header("Authorization", hr).contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson("TEST-CTRL-1", "ctrl1-updated@example.com")))
            .andExpect(status().isOk());
        mockMvc.perform(patch("/api/employees/TEST-CTRL-1/deactivate").header("Authorization", hr))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("Inactive"));
        mockMvc.perform(post("/api/employees/import").header("Authorization", hr)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[" + employeeJson("TEST-CTRL-2", "ctrl2@example.com") + "]"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.imported").value(1));

        mockMvc.perform(get("/api/countries").header("Authorization", hr)).andExpect(status().isOk());
        JsonNode countries = objectMapper.readTree(mockMvc.perform(get("/api/countries").header("Authorization", hr))
                .andReturn().getResponse().getContentAsString());
        String countryCode = countries.get(0).get("code").asText();
        mockMvc.perform(put("/api/countries/" + countryCode).header("Authorization", admin)
                .contentType(MediaType.APPLICATION_JSON).content("{\"rate\":1.2345}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(countryCode));

        mockMvc.perform(get("/api/salary-revisions").header("Authorization", hr)).andExpect(status().isOk());
        mockMvc.perform(post("/api/salary-revisions").header("Authorization", hr)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"employeeId\":\"EMP-00001\",\"newSalary\":\"90000.00\",\"effectiveDate\":\"2026-10-01\",\"reason\":\"Endpoint test\"}"))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.employeeId").value("EMP-00001"));

        mockMvc.perform(get("/api/audit").header("Authorization", admin)).andExpect(status().isOk());
        mockMvc.perform(post("/api/audit").header("Authorization", admin).contentType(MediaType.APPLICATION_JSON)
                .content("{\"action\":\"TEST\",\"detail\":\"controller endpoint test\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    void roleRestrictionsAreEnforced() throws Exception {
        String hr = basic("hr", "hr123");
        mockMvc.perform(get("/api/audit").header("Authorization", hr)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/countries/IN").header("Authorization", hr).contentType(MediaType.APPLICATION_JSON)
                .content("{\"rate\":1.2}"))
            .andExpect(status().isForbidden());
    }

    private String basic(String username, String password) {
        return "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    private String employeeJson(String id, String email) {
        return "{\"id\":\"" + id + "\",\"name\":\"Test Controller\",\"email\":\"" + email
                + "\",\"department\":\"Engineering\",\"designation\":\"Developer\",\"country\":\"India\",\"currency\":\"inr\",\"salary\":\"75000.00\",\"status\":\"Active\",\"joined\":\"2024-01-01\"}";
    }
}
