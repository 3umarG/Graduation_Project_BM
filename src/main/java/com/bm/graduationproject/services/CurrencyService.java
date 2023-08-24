package com.bm.graduationproject.services;

import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.dtos.CurrencyResponseDto;
import com.bm.graduationproject.enums.Currency;
import com.bm.graduationproject.web.response.ConversionOpenApiResponse;
import com.bm.graduationproject.repositories.CurrencyRepository;
import com.bm.graduationproject.web.response.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public interface CurrencyService {
    ConversionResponseDto convert(String from, String to, double amount);
    List<CurrencyResponseDto> getAllCurrencies();
    ExchangeRateResponse getExchangeRate(String baseCurrency, List<String> favourites);
}
