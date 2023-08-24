package com.bm.graduationproject.services;


import com.bm.graduationproject.dtos.ConversionDto;
import com.bm.graduationproject.models.ConversionOpenApiResponse;

public interface BaseCurrencyService {

    ConversionOpenApiResponse getAll(String from, String to);

    ConversionDto convert(String from, String to, double amount);

}
