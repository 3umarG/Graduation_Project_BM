package com.bm.graduationproject.services;

import com.bm.graduationproject.dtos.*;
import com.bm.graduationproject.enums.Currency;
import com.bm.graduationproject.repositories.CurrencyRepository;
import com.bm.graduationproject.web.response.ConversionOpenApiResponse;
import com.bm.graduationproject.web.response.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
@Service
public class CurrencyServiceImpl implements CurrencyService{
    private final CurrencyRepository currencyRepo;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository repository) {
        this.currencyRepo = repository;
    }

    @Override
    public ConversionResponseDto convert(String from, String to, double amount) {
        ConversionOpenApiResponse apiResponse = currencyRepo.getCurrencyPair(from, to);

        ConversionResponseDto dataDto = new ConversionResponseDto();
        dataDto.setSource(from.toUpperCase());
        dataDto.setDestination(to.toUpperCase());
        dataDto.setAmount(apiResponse.getConversion_rate() * amount);
        return dataDto;
    }

    @Override
    public List<CurrencyResponseDto> getAllCurrencies() {
        return Arrays.stream(Currency.values())
                .map(c -> new CurrencyResponseDto(c.name(), c.getCountry(), c.getFlagImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public ExchangeRateResponse getExchangeRate(String baseCurrency, List<String> favourites) {
        String base = Currency.valueOf(baseCurrency.toUpperCase()).name();
        List<CurrenciesRateDto> currencies = new ArrayList<>();
        ExchangeRateDto exchangeRateDto = currencyRepo.getExchangeRate(baseCurrency);
        Map<String, Double> currencies_rate = exchangeRateDto.getConversion_rates();
        favourites.forEach(f-> {
            Double currencyRate = getCurrencyValue(currencies_rate, f);
            Currency currency = Currency.valueOf(f);
                CurrenciesRateDto currencyInfo = new CurrenciesRateDto(currency.name(), currency.getCountry(),
                        currency.getFlagImageUrl(), currencyRate);
                currencies.add(currencyInfo);
        });

        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setBase(base);
        response.setCurrencies(currencies);
        return response;
    }
    private Double getCurrencyValue(Map<String, Double> currencyRate, String fav){
        return currencyRate.entrySet().stream().filter(c-> c.getKey().equals(fav.toUpperCase()))
                .map(Map.Entry::getValue).toList().get(0);
    }
}
