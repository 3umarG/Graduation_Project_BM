package com.bm.graduationproject.services;

import com.bm.graduationproject.dtos.CompareResponseDto;
import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.dtos.CurrencyResponseDto;
import com.bm.graduationproject.enums.Currency;
import com.bm.graduationproject.models.ConversionOpenApiResponse;
import com.bm.graduationproject.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService implements BaseCurrencyService {

    private final CurrencyRepository repository;

    @Autowired
    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    @Override
    public ConversionResponseDto convert(String from, String to, double amount) {
        ConversionOpenApiResponse apiResponse = repository.getCurrencyPair(from, to,amount);

        ConversionResponseDto dataDto = new ConversionResponseDto();
        dataDto.setSource(from.toUpperCase());
        dataDto.setDestination(to.toUpperCase());
        dataDto.setAmount(apiResponse.getConversion_result() == null
                ? apiResponse.getConversion_rate()
                : apiResponse.getConversion_result());
        return dataDto;
    }

    @Override
    public List<CurrencyResponseDto> getAllCurrencies() {
        return Arrays.stream(Currency.values())
                .map(c -> new CurrencyResponseDto(c.name(), c.getCountry(), c.getFlagImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public CompareResponseDto compare(String src, String des1, String des2, Double amount) {
        ConversionOpenApiResponse firstConvert = repository.getCurrencyPair(src, des1, amount);
        ConversionOpenApiResponse secondConvert = repository.getCurrencyPair(src, des2, amount);

        return CompareResponseDto.builder()
                .source(src)
                .destination1(firstConvert.getTarget_code())
                .destination2(secondConvert.getTarget_code())
                .amount1(firstConvert.getConversion_result() == null
                        ? firstConvert.getConversion_rate()
                        : firstConvert.getConversion_result())
                .amount2(secondConvert.getConversion_result() == null
                        ? secondConvert.getConversion_rate()
                        : secondConvert.getConversion_result())
                .build();

    }
}
