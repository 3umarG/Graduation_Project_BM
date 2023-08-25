package com.bm.graduationproject;

import com.bm.graduationproject.models.enums.Currency;
import com.bm.graduationproject.repositories.CurrencyRepository;
import com.bm.graduationproject.services.CurrencyServiceImpl;
import com.bm.graduationproject.web.controllers.CurrencyController;
import com.bm.graduationproject.web.response.ApiCustomResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTests {

    @MockBean
    private CurrencyServiceImpl service;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyRepository repository;

    @Test
    public void convert_timeoutException_returnGATEWAY_TIMEOUT_Response() throws Exception {
        // Arrange
        given(
                service.convert("KWD", "USD", 20.0)
        ).willAnswer(i -> {
            throw new TimeoutException();
        });

        // Act
        String uri = "/api/v1/currency/conversion";
        ApiCustomResponse<?> response = ApiCustomResponse.builder()
                .isSuccess(false)
                .message("The request timed out. Please try again later.")
                .statusCode(HttpStatus.GATEWAY_TIMEOUT.value())
                .data(null)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String expectedResponse = mapper.writeValueAsString(response).replace(",\"data\":null", "");


        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .param("from", "KWD")
                        .param("to1", "USD")
                        .param("amount", String.valueOf(20.0)))
                .andDo(print())
                .andExpect(status().isGatewayTimeout())
                .andExpect(content().json(expectedResponse));
    }


    @Test
    public void compare_timeoutException_returnGATEWAY_TIMEOUT_Response() throws Exception {
        // Arrange
        given(
                service.compare("KWD", "USD", "EUR", 20.0)
        ).willAnswer(i -> {
            throw new TimeoutException();
        });

        // Act
        String uri = "/api/v1/currency/conversion";
        ApiCustomResponse<?> response = ApiCustomResponse.builder()
                .isSuccess(false)
                .message("The request timed out. Please try again later.")
                .statusCode(HttpStatus.GATEWAY_TIMEOUT.value())
                .data(null)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String expectedResponse = mapper.writeValueAsString(response).replace(",\"data\":null", "");


        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .param("from", "KWD")
                        .param("to1", "USD")
                        .param("to2", "EUR")
                        .param("amount", String.valueOf(20.0)))
                .andDo(print())
                .andExpect(status().isGatewayTimeout())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void getFavorites_timeoutException_returnGATEWAY_TIMEOUT_Response() throws Exception {
        // Arrange
        List<Currency> favs = new ArrayList<>();
        favs.add(Currency.USD);
        favs.add(Currency.QAR);
        given(
                service.getExchangeRate(Currency.KWD, favs)
        ).willAnswer(i -> {
            throw new TimeoutException();
        });

        // Act
        String uri = "/api/v1/currency";
        ApiCustomResponse<?> response = ApiCustomResponse.builder()
                .isSuccess(false)
                .message("The request timed out. Please try again later.")
                .statusCode(HttpStatus.GATEWAY_TIMEOUT.value())
                .data(null)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String expectedResponse = mapper.writeValueAsString(response).replace(",\"data\":null", "");


        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(mapper.writeValueAsString(favs))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("base", "KWD"))
                .andDo(print())
                .andExpect(status().isGatewayTimeout())
                .andExpect(content().json(expectedResponse));
    }
}
