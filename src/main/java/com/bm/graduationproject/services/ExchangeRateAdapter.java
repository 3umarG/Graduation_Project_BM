package com.bm.graduationproject.services;

import com.bm.graduationproject.dtos.ExchangeRateOpenApiResponseDto;
import com.bm.graduationproject.models.FavoritesResponseDto;
import com.bm.graduationproject.models.enums.Currency;

import java.util.List;

public interface ExchangeRateAdapter {
    FavoritesResponseDto adapt(ExchangeRateOpenApiResponseDto apiResponse, Currency base, List<Currency> favorites);
}
