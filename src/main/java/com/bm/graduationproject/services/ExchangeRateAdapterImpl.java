package com.bm.graduationproject.services;

import com.bm.graduationproject.dtos.CurrencyResponseDto;
import com.bm.graduationproject.dtos.ExchangeRateOpenApiResponseDto;
import com.bm.graduationproject.models.FavoritesResponseDto;
import com.bm.graduationproject.models.enums.Currency;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRateAdapterImpl implements ExchangeRateAdapter  {

    @Override
    public FavoritesResponseDto adapt(ExchangeRateOpenApiResponseDto apiResponse, Currency base, List<Currency> favorites) {
        String baseCurrency = base.name();
        List<CurrencyResponseDto> currencies = new ArrayList<>();
        Map<String, Double> currencies_rate = apiResponse.getConversion_rates();

        extractFavoritesCurrenciesFromApiResponse(favorites, currencies_rate, currencies);

        return buildFavoritesResponseDto(baseCurrency, currencies);
    }

    private static FavoritesResponseDto buildFavoritesResponseDto(String baseCurrency, List<CurrencyResponseDto> currencies) {
        FavoritesResponseDto adaptedResponse = new FavoritesResponseDto();
        adaptedResponse.setBase(baseCurrency);
        adaptedResponse.setCurrencies(currencies);
        return adaptedResponse;
    }

    private void extractFavoritesCurrenciesFromApiResponse(List<Currency> favorites, Map<String, Double> currencies_rate, List<CurrencyResponseDto> currencies) {
        favorites.forEach(f -> {
            Double currencyRate = getCurrencyValue(currencies_rate, f.name());
            Currency currency = Currency.valueOf(f.name());
            CurrencyResponseDto currencyInfo = new CurrencyResponseDto(currency.name(), currency.getCountry(),
                    currency.getFlagImageUrl(), currencyRate);
            currencies.add(currencyInfo);
        });
    }

    private Double getCurrencyValue(Map<String, Double> currencyRate, String fav) {
        return currencyRate.entrySet().stream().filter(c -> c.getKey().equals(fav.toUpperCase()))
                .map(Map.Entry::getValue).toList().get(0);
    }
}
