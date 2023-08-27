package com.bm.graduationproject.services;

import com.bm.graduationproject.dtos.CompareResponseDto;
import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.dtos.CurrencyResponseDto;
import com.bm.graduationproject.dtos.ExchangeRateOpenApiResponseDto;
import com.bm.graduationproject.models.enums.Currency;
import com.bm.graduationproject.web.response.ConversionOpenApiResponse;
import com.bm.graduationproject.models.FavoritesResponseDto;
import com.bm.graduationproject.repositories.CurrencyRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;
    private final ExchangeRateAdapter adapter;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository repository , ExchangeRateAdapter adapter) {
        this.repository = repository;
        this.adapter = adapter;
    }

    @Override
    @Cacheable(value = "conversionCache", key = "#from + '-' + #to + '-' + #amount")
    public ConversionResponseDto convert(String from, String to, double amount) {
        ConversionOpenApiResponse apiResponse = repository.getCurrencyPair(from, to, amount);

        ConversionResponseDto conversionResponseDto = new ConversionResponseDto();
        conversionResponseDto.setSource(from.toUpperCase());
        conversionResponseDto.setDestination(to.toUpperCase());
        conversionResponseDto.setAmount(apiResponse.getConversion_result() == null
                ? apiResponse.getConversion_rate()
                : apiResponse.getConversion_result());
        return conversionResponseDto;
    }

    @Override
    public List<CurrencyResponseDto> getAllCurrencies() {
        return Arrays.stream(Currency.values())
                .map(c -> new CurrencyResponseDto(c.name(), c.getCountry(), c.getFlagImageUrl(), null))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(
            value = "compareCache",
            key = "#src + '-' + #des1 + '-' + #des2 + '-' + #amount")
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


    @Override
    @Cacheable(
            value = "exchangeRateCache",
            key = "#baseCurrency.name() + '-' + @cachingService.concatEnumNames(#favourites)")
    public FavoritesResponseDto getExchangeRate(Currency baseCurrency, List<Currency> favourites) {
        ExchangeRateOpenApiResponseDto exchangeRateDto = repository.getExchangeRate(baseCurrency.name());
        return adapter.adapt(exchangeRateDto, baseCurrency, favourites);
    }


}
