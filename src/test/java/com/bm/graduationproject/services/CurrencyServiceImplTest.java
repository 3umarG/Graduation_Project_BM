package com.bm.graduationproject.services;

import com.bm.graduationproject.config.CachingConfig;
import com.bm.graduationproject.dtos.CompareResponseDto;
import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.dtos.ExchangeRateOpenApiResponseDto;
import com.bm.graduationproject.models.FavoritesResponseDto;
import com.bm.graduationproject.models.enums.Currency;
import com.bm.graduationproject.repositories.CurrencyRepository;
import com.bm.graduationproject.web.response.ConversionOpenApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableCaching
@Import(CachingConfig.class)
@ActiveProfiles("test")
class CurrencyServiceImplTest {


    @Value("${expire_after_duration}")
    private long expireAfterDuration;

    @Value("${expire_after_time_unit}")
    private String expireAfterTimeUnit;

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
                .thenReturn(apiResponse1, apiResponse2);

        CompareResponseDto result = currencyService.compare("USD", "EUR", "KWD", 50.0);

        Assertions.assertEquals("USD", result.getSource());
        Assertions.assertEquals(15.5, result.getAmount2());
        Assertions.assertEquals(46.5, result.getAmount1());
        Assertions.assertEquals("EUR", result.getDestination1());
        Assertions.assertEquals("KWD", result.getDestination2());
    }

    @Test
    public void testGetExchangeRate() {
        //Arrange
        Currency baseCurrency = Currency.SAR;
        List<Currency> favourites = new ArrayList<>();
        favourites.add(Currency.KWD);
        favourites.add(Currency.AED);
        favourites.add(Currency.EUR);
        Map<String, Double> currencies_rates = new HashMap<>();
        favourites.forEach(f -> currencies_rates.put(f.name(), 0.082));
        ExchangeRateOpenApiResponseDto exchangeRateOpenApiResponseDto =
                ExchangeRateOpenApiResponseDto.builder().result("success")
                        .base_code(baseCurrency.name()).conversion_rates(currencies_rates).build();
        //Act
        when(currencyRepository.getExchangeRate(Mockito.anyString())).thenReturn(exchangeRateOpenApiResponseDto);
        FavoritesResponseDto favoritesResponseDto = currencyService.getExchangeRate(baseCurrency, favourites);
        //Assert
        Assertions.assertNotEquals(favoritesResponseDto, null);
        Assertions.assertEquals(favoritesResponseDto.getCurrencies().size(), 3);
        favoritesResponseDto.getCurrencies().forEach(f-> {
            List<Currency> currency = new ArrayList<>();
            currency.add(Currency.valueOf(f.code()));
            Assertions.assertEquals(f, currencyService.getExchangeRate(baseCurrency, currency).getCurrencies().get(0));
        });
    }


    @Test
    public void convert_testCacheBehavior() throws TimeoutException {

        // Arrange
        cache = cacheManager.getCache("conversionCache");
        when(currencyRepository.getCurrencyPair(any(), any(), any())).thenReturn(ConversionOpenApiResponse.builder()
                .base_code("KWD")
                .target_code("USD")
                .conversion_rate(105.0)
                .build());

        // Act
        currencyService.convert("KWD", "USD", 10.0);
        currencyService.convert("KWD", "USD", 10.0);

        // Assert
        //  that the repository method has only ONE call

        verify(currencyRepository, times(1)).getCurrencyPair(any(), any(), any());

    }

    @Test
    public void compare_testCacheBehavior() {

        // Arrange
        cache = cacheManager.getCache("compareCache");
        when(currencyRepository.getCurrencyPair(any(), any(), any())).thenReturn(ConversionOpenApiResponse.builder()
                .base_code("KWD")
                .target_code("USD")
                .conversion_rate(105.0)
                .build());

        // Act
        currencyService.compare("KWD", "USD", "EUR", 10.0);
        currencyService.compare("KWD", "USD", "EUR", 10.0);
        currencyService.compare("KWD", "USD", "EUR", 10.0);
        currencyService.compare("KWD", "USD", "EUR", 10.0);


        // Assert
        //  that the repository method has only TWO calls ,
        //  because each compare needs two calls for two currencies.

        verify(currencyRepository, times(2)).getCurrencyPair(any(), any(), any());

    }


    @Test
    public void getExchangeRate_testCacheBehavior() {

        // Arrange
        cache = cacheManager.getCache("exchangeRateCache");

        Map<String, Double> allCurrenciesRatesFromRepository = new HashMap<>();

        allCurrenciesRatesFromRepository.put("USD", 10.0);

        allCurrenciesRatesFromRepository.put("KWD", 10.0);

        when(currencyRepository.getExchangeRate("AED"))
                .thenReturn(ExchangeRateOpenApiResponseDto.builder()
                        .base_code("AED")
                        .conversion_rates(allCurrenciesRatesFromRepository)
                        .build());

        List<Currency> favs = new ArrayList<>();
        favs.add(Currency.USD);
        favs.add(Currency.KWD);


        // Act
        currencyService.getExchangeRate(Currency.AED, favs);
        currencyService.getExchangeRate(Currency.AED, favs);
        currencyService.getExchangeRate(Currency.AED, favs);
        currencyService.getExchangeRate(Currency.AED, favs);


        // Assert
        //  that the repository method has only ONE calls ,

        verify(currencyRepository, times(1)).getExchangeRate(any());

    }

    @Test
    public void convert_testCacheExpiration() throws TimeoutException,
            InterruptedException {
        // Arrange
        String from = "KWD";
        String to = "USD";
        double amount = 10.5;
        cache = cacheManager.getCache("conversionCache");
        when(currencyRepository.getCurrencyPair(any(), any(), any())).thenReturn(ConversionOpenApiResponse.builder()
                .base_code(from)
                .target_code(to)
                .conversion_rate(amount)
                .build());


        // Act
        currencyService.convert(from, to, amount);

        // Assert with the first call:before expiration
        // #from-#to-#amount
        String cacheKey = from + '-' + to + '-' + amount;
        assertNotNull(cache.get(cacheKey));

        // Wait for cache expiration
        TimeUnit.valueOf(expireAfterTimeUnit).sleep(expireAfterDuration);

        // Assert second time after the expiration
        assertNull(cache.get(cacheKey));
    }


    @Test
    public void compare_testCacheExpiration() throws InterruptedException {
        // Arrange
        String src = "KWD";
        String des1 = "USD";
        String des2 = "EUR";
        Double amount = 10.5;
        cache = cacheManager.getCache("compareCache");
        when(currencyRepository.getCurrencyPair(any(), any(), any())).thenReturn(ConversionOpenApiResponse.builder()
                .base_code("KWD")
                .target_code("USD")
                .conversion_rate(10.5)
                .build());


        // Act
        currencyService.compare(src, des1, des2, amount);

        // Assert with the first call:before expiration
        // #from-#to-#amount
        String cacheKey = src + '-' + des1 + '-' + des2 + '-' + amount;
        assertNotNull(cache.get(cacheKey));

        // Wait for cache expiration
        TimeUnit.valueOf(expireAfterTimeUnit).sleep(expireAfterDuration);

        // Assert second time after the expiration
        assertNull(cache.get(cacheKey));
    }


    @Test
    public void getFavoritesRates_testCacheExpiration() throws InterruptedException {
        // Arrange
        Currency base = Currency.EUR;

        List<Currency> favs = Arrays.asList(Currency.USD, Currency.KWD);

        Map<String, Double> allCurrenciesRatesFromRepository = new HashMap<>();
        allCurrenciesRatesFromRepository.put("USD", 10.0);
        allCurrenciesRatesFromRepository.put("KWD", 10.0);

        cache = cacheManager.getCache("exchangeRateCache");
        when(currencyRepository.getExchangeRate(any())).thenReturn(
                ExchangeRateOpenApiResponseDto.builder()
                        .base_code(base.name())
                        .conversion_rates(allCurrenciesRatesFromRepository)
                        .build());


        String listId = String.join(favs.toString());

        // Act
        currencyService.getExchangeRate(base, favs);

        // Assert with the first call:before expiration
        assertNotNull(cache.get(base.name() + '-' + listId));

        // Wait for cache expiration
        TimeUnit.valueOf(expireAfterTimeUnit).sleep(expireAfterDuration);

        // Assert second time after the expiration
        assertNull(cache.get(base.name() + '-' + listId));
    }
}
