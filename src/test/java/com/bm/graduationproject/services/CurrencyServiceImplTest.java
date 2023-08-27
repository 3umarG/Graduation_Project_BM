package com.bm.graduationproject.services;
import com.bm.graduationproject.config.CachingConfig;
import com.bm.graduationproject.dtos.CompareResponseDto;
import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.dtos.CurrencyResponseDto;
import com.bm.graduationproject.dtos.ExchangeRateOpenApiResponseDto;
import com.bm.graduationproject.models.enums.Currency;
import com.bm.graduationproject.repositories.CurrencyRepository;
import com.bm.graduationproject.web.response.ConversionOpenApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableCaching
@Import(CachingConfig.class)
class CurrencyServiceImplTest {

    @Autowired
    private CurrencyService currencyService;

    @MockBean
    private CurrencyRepository currencyRepository;

    @Autowired
    private CacheManager cacheManager;

    private Cache cache;

    @Test
    public void testConvert() throws TimeoutException {
        CurrencyRepository repository = Mockito.mock(CurrencyRepository.class);
        CurrencyServiceImpl currencyService = new CurrencyServiceImpl(repository);

        ConversionOpenApiResponse apiResponse = new ConversionOpenApiResponse();

        apiResponse.setConversion_rate(0.93);
        apiResponse.setConversion_result(46.5);

        when(repository.getCurrencyPair(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble()))
                .thenReturn(apiResponse);

        ConversionResponseDto result = currencyService.convert("USD", "EUR", 50.0);

        Assertions.assertEquals("USD", result.getSource());
        Assertions.assertEquals("EUR", result.getDestination());
        Assertions.assertEquals(46.5, result.getAmount());

    }

    @Test
    public void testcompare() {
        CurrencyRepository repository = Mockito.mock(CurrencyRepository.class);
        CurrencyServiceImpl currencyService = new CurrencyServiceImpl(repository);

        ConversionOpenApiResponse apiResponse1 = new ConversionOpenApiResponse();
        ConversionOpenApiResponse apiResponse2 = new ConversionOpenApiResponse();


        apiResponse1.setConversion_result(46.5);
        apiResponse1.setTarget_code("EUR");
        apiResponse2.setTarget_code("KWD");
        apiResponse2.setConversion_result(15.5);

        when(repository.getCurrencyPair(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble()))
                .thenReturn(apiResponse1,apiResponse2);

        CompareResponseDto result = currencyService.compare("USD", "EUR", "KWD", 50.0);

        Assertions.assertEquals("USD", result.getSource());
        Assertions.assertEquals(15.5, result.getAmount2());
        Assertions.assertEquals(46.5, result.getAmount1());
        Assertions.assertEquals("EUR", result.getDestination1());
        Assertions.assertEquals("KWD", result.getDestination2());
    }


    @Test
    public void convert_testCacheBehavior() throws TimeoutException {

        // Arrange
        cache = cacheManager.getCache("conversionCache");
        when(currencyRepository.getCurrencyPair(any(),any(),any())).thenReturn(ConversionOpenApiResponse.builder()
                        .base_code("KWD")
                        .target_code("USD")
                        .conversion_rate(105.0)
                .build());

        // Act
        currencyService.convert("KWD","USD",10.0);
        currencyService.convert("KWD","USD",10.0);

        // Assert
        //  that the repository method has only ONE call

        verify(currencyRepository, times(1)).getCurrencyPair(any(), any(), any());

    }

    @Test
    public void compare_testCacheBehavior() {

        // Arrange
        cache = cacheManager.getCache("compareCache");
        when(currencyRepository.getCurrencyPair(any(),any(),any())).thenReturn(ConversionOpenApiResponse.builder()
                .base_code("KWD")
                .target_code("USD")
                .conversion_rate(105.0)
                .build());

        // Act
        currencyService.compare("KWD","USD","EUR",10.0);
        currencyService.compare("KWD","USD","EUR",10.0);
        currencyService.compare("KWD","USD","EUR",10.0);
        currencyService.compare("KWD","USD","EUR",10.0);


        // Assert
        //  that the repository method has only TWO calls ,
        //  because each compare needs two calls for two currencies.

        verify(currencyRepository, times(2)).getCurrencyPair(any(), any(), any());

    }


    @Test
    public void getExchangeRate_testCacheBehavior(){

        // Arrange
        cache = cacheManager.getCache("exchangeRateCache");

        Map<String, Double> allCurrenciesRatesFromRepository = new HashMap<>();

        allCurrenciesRatesFromRepository.put("USD",10.0);

        allCurrenciesRatesFromRepository.put("KWD",10.0);

        when(currencyRepository.getExchangeRate("AED"))
                .thenReturn(ExchangeRateOpenApiResponseDto.builder()
                        .base_code("AED")
                        .conversion_rates(allCurrenciesRatesFromRepository)
                        .build());

        List<Currency> favs= new ArrayList<>();
        favs.add(Currency.USD);
        favs.add(Currency.KWD);


        // Act
        currencyService.getExchangeRate(Currency.AED,favs);
        currencyService.getExchangeRate(Currency.AED,favs);
        currencyService.getExchangeRate(Currency.AED,favs);
        currencyService.getExchangeRate(Currency.AED,favs);



        // Assert
        //  that the repository method has only ONE calls ,

        verify(currencyRepository, times(1)).getExchangeRate(any());

    }
    @Test
    public void testgetAllCurrencies() {

        //Arrange
        List<Currency> currencies = List.of(Currency.values());
        List<CurrencyResponseDto> actualResponse = new ArrayList<>();
        currencies.forEach(r->{
            CurrencyResponseDto currencyResponseDto= new CurrencyResponseDto(r.name(),r.getCountry(),r.getFlagImageUrl(), null);
            actualResponse.add(currencyResponseDto);

        });

        //Act
        List<CurrencyResponseDto> expectedResponse = currencyService.getAllCurrencies();

        //Assert

        Assertions.assertEquals(expectedResponse.size(), actualResponse.size());
        Assertions.assertEquals(actualResponse, expectedResponse);

    }
}
