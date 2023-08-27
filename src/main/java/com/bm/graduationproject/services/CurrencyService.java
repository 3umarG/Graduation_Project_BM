package com.bm.graduationproject.services;


import com.bm.graduationproject.dtos.CompareResponseDto;
import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.dtos.CurrencyResponseDto;
import com.bm.graduationproject.models.enums.Currency;
import com.bm.graduationproject.models.FavoritesResponseDto;

import java.util.List;
import java.util.concurrent.TimeoutException;

public interface CurrencyService {

    ConversionResponseDto convert(String from, String to, double amount) throws TimeoutException;

    List<CurrencyResponseDto> getAllCurrencies();

    CompareResponseDto compare(String src, String des1 , String des2 , Double amount);

    FavoritesResponseDto getExchangeRate(Currency baseCurrency, List<Currency> favourites);

}
