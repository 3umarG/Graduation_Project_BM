package com.bm.graduationproject.services;

import com.bm.graduationproject.dtos.CompareResponseDto;
import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.dtos.CurrencyResponseDto;
import com.bm.graduationproject.dtos.ExchangeRateOpenApiResponseDto;
import com.bm.graduationproject.models.enums.Currency;
import com.bm.graduationproject.web.response.ConversionOpenApiResponse;
import com.bm.graduationproject.models.FavoritesResponseDto;
import com.bm.graduationproject.repositories.CurrencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(value = "conversionCache", key = "#from + '-' + #to + '-' + #amount")
    public ConversionResponseDto convert(String from, String to, double amount) {
        logger.info("Converting from "+ amount +" " + from + " to " + to);
        ConversionOpenApiResponse apiResponse = repository.getCurrencyPair(from, to, amount);
        ConversionResponseDto conversionResponseDto = new ConversionResponseDto();
        conversionResponseDto.setSource(from.toUpperCase());
        conversionResponseDto.setDestination(to.toUpperCase());
        conversionResponseDto.setAmount(apiResponse.getConversion_result() == null
                ? apiResponse.getConversion_rate()
                : apiResponse.getConversion_result());
        logger.info("Conversion completed successfully");
        logger.info("The result is: " + apiResponse.getConversion_result() +" " + to);
        return conversionResponseDto;
    }

    @Override
    public List<CurrencyResponseDto> getAllCurrencies() {
        logger.info("Getting currencies enum");
        logger.info("Total number of currencies: " + Currency.values().length);
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
        logger.info("Converting from "+ amount + " " + src + " to " + des1);
        logger.info("The result is: " + firstConvert.getConversion_result() +" " + des1);
        ConversionOpenApiResponse secondConvert = repository.getCurrencyPair(src, des2, amount);
        logger.info("Converting from "+ amount + " " + src + " to " + des2);
        logger.info("The result is: " + secondConvert.getConversion_result() +" " + des2);
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
            key ="#baseCurrency.name() + '-' + T(String).join(#favourites.toString())")
    public FavoritesResponseDto getExchangeRate(Currency baseCurrency, List<Currency> favourites) {
        logger.info("Getting the exchange rate:");
        String base = baseCurrency.name();
        logger.info("Base currency: " + base);
        List<CurrencyResponseDto> currencies = new ArrayList<>();
        ExchangeRateOpenApiResponseDto exchangeRateDto = repository.getExchangeRate(baseCurrency.name());
        Map<String, Double> currencies_rate = exchangeRateDto.getConversion_rates();
        favourites.forEach(f -> {
            Double currencyRate = getCurrencyValue(currencies_rate, f.name());
            Currency currency = Currency.valueOf(f.name());
            logger.info(f.name() + " exchange rate is: " + currencyRate);
            CurrencyResponseDto currencyInfo = new CurrencyResponseDto(currency.name(), currency.getCountry(),
                    currency.getFlagImageUrl(), currencyRate);
            currencies.add(currencyInfo);
        });

        FavoritesResponseDto response = new FavoritesResponseDto();
        response.setBase(base);
        response.setCurrencies(currencies);
        return response;
    }

    private Double getCurrencyValue(Map<String, Double> currencyRate, String fav) {
        return currencyRate.entrySet().stream().filter(c -> c.getKey().equals(fav.toUpperCase()))
                .map(Map.Entry::getValue).toList().get(0);
    }

    public Double getCurrencyValue(String base, String fav) {
        return convert(base, fav, 1).getAmount();
    }
}
