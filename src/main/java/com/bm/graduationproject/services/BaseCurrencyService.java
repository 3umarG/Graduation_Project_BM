package com.bm.graduationproject.services;


import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.models.ConversionOpenApiResponse;

public interface BaseCurrencyService {

    ConversionResponseDto convert(String from, String to, double amount);

}
